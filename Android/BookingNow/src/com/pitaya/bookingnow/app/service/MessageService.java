package com.pitaya.bookingnow.app.service;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.message.FoodMessage;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.message.TableMessage;

public class MessageService extends Service implements Runnable {
	
	private static String TAG = "MessageService";
	private final IBinder mBinder = new MessageBinder();
	private NotificationManager mNM;
	private ConnectivityManager mCM;
	private NotificationCompat.Builder mBuilder;
	private IntentFilter mIntentFilter;
	
	private Socket socket;
    private String ip;
    private int port;
    private BufferedReader in;
    private OutputStreamWriter out;
    private BufferedWriter bwriter;
    private Long userid;
    private volatile boolean isConnecting = false;
    
	//private Client clientAgent;
	private Map<String, List<Handler>> handlers;
	private int mUpdateNotifyID = 1;
	private int lastNotifyID = 2;
	
	public MessageService(){
		this.handlers = new ConcurrentHashMap<String, List<Handler>>();
		this.ip = HttpService.IP;
		this.port = HttpService.PORT;
	}
	
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mCM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        this.start();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(new ConnectionReceiver(), mIntentFilter);
        Log.i(TAG,  "In message service oncreate");
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        this.start();
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
    	Log.i(TAG, "In message service on destroy");
        mNM.cancelAll();
        this.shutdown();
        Toast.makeText(this, "BookingNow message service stopped", Toast.LENGTH_SHORT).show();
    }
    
    private synchronized void start(){
    	NetworkInfo activeNetwork = mCM.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if(!this.isReady() && !this.isConnecting() && isConnected){
        	new Thread(this).start();
        }
    }

    public void setUserId(Long id){
    	this.userid = id;
    }
    
    public Long getUserId(){
		return this.userid;
    }
    
	public void registerHandler(String key, Handler handler){
		List<Handler> handlerList = this.handlers.get(key);
		if(handlerList == null){
			handlerList = new ArrayList<Handler>();
			this.handlers.put(key, handlerList);
		}
		for(Handler h : handlerList){
			if(h == handler){
				return;
			}
		}
		handlerList.add(handler);
		Log.i(TAG, "Register message handler with key: " + key);
	}
	
	public void unregisterHandler(Handler handler){
		for(Entry<String, List<Handler>> entry : this.handlers.entrySet()){
			List<Handler> handlers = entry.getValue(); 
			for(int i = 0; i < handlers.size(); i++){
				if(handlers.get(i) == handler){
					Log.i(TAG, "Unregister message handler");
					handlers.remove(i);
					break;
				}
			}
		}
	}
	
	public boolean isConnecting(){
		return this.isConnecting;
	}
	
	private void shutdown(){
		 if(in != null){
				try {
	        		 in.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}	 
	    	 }
		     if(bwriter != null){
			    try {
			    	bwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		     }
			 if (socket != null && !socket.isClosed()){
				try {
					socket.close();
					Log.i(TAG, "Success to shutdown the connection to server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
			 socket = null;
	}
	
	public boolean sendMessage(Message message){
		return this.sendMessage(parseMessage(message));
	}
	
	public boolean isReady(){
		return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
	}
	
	void onMessage(Message message){
		if(message == null){
			return;
		}
		List<Handler> handlerList = this.handlers.get(message.getCategory());
		if(handlerList != null && handlerList.size() > 0){
	        for(Handler handler : handlerList){
				android.os.Message amsg = new android.os.Message();  
		        Bundle bundle = new Bundle();  
		        bundle.putSerializable("message", message);
		        amsg.setData(bundle);
	        	handler.sendMessage(amsg);
	        }
		} else {
			Log.i(TAG, "This category message has not been registered: " + message.getCategory());
			//Handle message which is not processed by other handlers here
			if(message.getCategory().equals(Constants.FOOD_MESSAGE) && message instanceof FoodMessage){
				showUpdateNotification();
			}
		}
		//Handle message no matter it has been processed by other handlers here
		if(message.getCategory().equals(Constants.TABLE_MESSAGE) && message instanceof TableMessage){
			TableMessage msg = (TableMessage)message;
			String text = msg.getOrder().getCustomerName() + "(" + msg.getOrder().getPhoneNumber()+ ")" 
					+ "的订单有符合人数的座位:" + msg.getTable().getLabel();
			showNotification("新的空位", text);
		}
	}
	
	void onMessage(String message){
		Message msg = unparseMessage(message);
		onMessage(msg);
	}
	
	public static String parseMessage(Message message){
		JSONObject jsonObj = new JSONObject();
		message.toJSONObject(jsonObj);
		return jsonObj.toString();
	}
	
	public static Message unparseMessage(String message){
		Message msg = null;
		String type = null;
		try {
			JSONObject jsonMsg = new JSONObject(message);
			type = jsonMsg.getString("type");
			if(type != null){
				Class<?> msgcls = Class.forName(type);
				msg = (Message) msgcls.newInstance();
				msg.fromJSONObject(jsonMsg);
			} else {
				Log.e(TAG, "Message type is null: " + message);
			}
		} catch (JSONException e) {
			Log.e(TAG, "Fail to parse from message to json" + message);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Unsupported message type: " + type);
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.e(TAG, "Fail to parse message type:" + type);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Fail to parse message type:" + type);
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG,  "In message service onBind");
		return this.mBinder;
	}
	
	public class MessageBinder extends Binder {
	    
		public MessageService getService() {
		      return MessageService.this;
	    }
	}
	
    private void showUpdateNotification() {
        CharSequence text = "菜单有更新，请打开应用完成审升级";
        mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("BookingNow")
        .setContentText(text);
        mNM.notify(mUpdateNotifyID, mBuilder.build());
    }
    
    private void showNotification(String title, CharSequence text) {
        mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(title)
        .setContentText(text);
        mNM.notify(lastNotifyID++, mBuilder.build());
    }

	@Override
	public void run() {
		isConnecting = true;
    	boolean error = false;
        try {
			setupConnection();
			isConnecting = false;
			Log.i(TAG, "Success to connect to web server");
        	String message = null;
			this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.SUCCESS, "连接服务器成功"));
        	while((message = in.readLine()) != null){
    			this.onMessage(message);
        		if(message.equals("bye")){
        			shutdown();
        			break;
        		}
        	}
        } catch (UnknownHostException e) {
        	UserManager.setLoginUser(this, null);
			this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "无法识别的服务器"));
        	error = true;
        	Log.e(TAG, "Fail to connect to web server");
            e.printStackTrace();
        } catch (IOException e) {
        	UserManager.setLoginUser(this, null);
			this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "无法连接到服务器或连接中断"));
        	error = true;
        	Log.e(TAG, "Fail to connect to web server");
			e.printStackTrace();
		} finally {
			if(in != null){
	        	try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
       		if(bwriter != null){
       			try {
					bwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
       		} 
       		if (socket != null && !socket.isClosed()){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
       		}
       		socket = null;
	    	isConnecting = false;
        	if(error == false){
        		UserManager.setLoginUser(this, null);
				this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "与服务器连接中断"));
			}
        }
	}

    public synchronized boolean sendMessage(String msg) {
        try {
        	this.bwriter.write(msg + "\r\n");
        	this.bwriter.flush();
        	return true;
        } catch (Exception e) {
            Log.e(TAG, "Fail to send message:" + msg);
            return false;
        }
    }
	
    private void setupConnection() throws UnknownHostException, IOException {
        socket = new Socket(ip, port);
    	in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    	out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");  
		bwriter = new BufferedWriter(out);  
    }

}

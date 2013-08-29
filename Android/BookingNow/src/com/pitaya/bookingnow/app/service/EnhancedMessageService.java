package com.pitaya.bookingnow.app.service;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.util.Constants;

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
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.message.FoodMessage;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.message.TableMessage;

public class EnhancedMessageService extends Service implements Runnable {
	
	private static String TAG = "EnhancedMessageService";
	private final IBinder mBinder = new MessageBinder();
	private NotificationManager mNM;
	private ConnectivityManager mCM;
	private NotificationCompat.Builder mBuilder;
	private IntentFilter mIntentFilter;
	
	private ServerSocket mServerSocket;
    private UDPService mUDPService;
    private Thread mServerThread;
    private Thread mUPDServerThread;
    private String remote_addr;
    private int remote_port;
    private int port;
    private boolean flag;
    private final ExecutorService mServerAgentPool =  Executors.newFixedThreadPool(5);
    private volatile boolean isConnecting = false;
    
	private Map<String, List<Handler>> handlers;
	private int mUpdateNotifyID = 1;
	private int lastNotifyID = 2;
	
	
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
	
	public EnhancedMessageService(){
		this.handlers = new ConcurrentHashMap<String, List<Handler>>();
		this.remote_addr = HttpService.IP;
		this.remote_port = HttpService.REMOTE_PORT;
		this.port = HttpService.PORT;
	}

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mCM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(new ConnectionReceiver(), mIntentFilter);
        this.start();
        Log.i(TAG,  "In message service oncreate");
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        if(intent != null && intent.getExtras() != null){
        	Bundle bundle = intent.getExtras();
//        	if(bundle.getBoolean("connected") == false){
//        		Log.i(TAG, "Connection status changed to false");
//        		this.onDisconnect();
//        		return START_STICKY;
//        	}
        }
        this.start();
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
    	Log.i(TAG, "In message service on destroy");
        mNM.cancelAll();
        this.onDisconnect();
    }
    
    private synchronized void start(){
    	NetworkInfo activeNetwork = mCM.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if((this.mServerThread == null ||
        		(!this.isReady() && !this.isConnecting())) && isConnected){
        	//Start a server socket to receive message
        	this.isConnecting = true;
        	this.shutdown();
        	this.mServerThread = new Thread(this);
        	this.mServerThread.start();
        }
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
		this.flag = false;
		if(this.mServerSocket != null){
			try {
				this.mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.mServerSocket = null;
	}
	
	public void sendMessage(Message message){
		new ClientAgent(this.remote_addr, this.remote_port, parseMessage(message), this).start();
	}
	
	public boolean isReady(){
		return this.mServerSocket != null && this.mServerSocket.isBound()
				&& this.mUDPService != null && this.mUDPService.isRunnning();
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

	void onMessage(String message, ServerAgent serverAgent){
		Message msg = unparseMessage(message);
		onMessage(msg);
		Log.d(TAG, "Receive message: " + msg);
	}
	
	void onMessage(String message, ClientAgent clientAgent){
		Message msg = unparseMessage(message);
		onMessage(msg);
		Log.d(TAG, "Server reply message: " + msg);
		clientAgent.shutdown();
	}
	
	void onConnectServer(){
    	if(this.mUDPService == null || !this.mUDPService.isRunnning()){
    		this.mUDPService = new UDPService(this);
    		mUPDServerThread = new Thread(this.mUDPService);
    		mUPDServerThread.start();
    	}
	}
	
	void onUDPServiceStart(){
		this.isConnecting = false;
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.SUCCESS, "连接服务器成功"));
	}

	void onDisconnect(){
		UserManager.setLoginUser(this, null);
		this.isConnecting = false;
		if(this.mServerThread != null){
			this.shutdown();
			this.mServerThread.interrupt();
		}
		if(this.mUDPService != null){
			this.mUDPService.shutdown();
		}
		if(this.mUPDServerThread != null){
			this.mUPDServerThread.interrupt();
		}
		Log.i(TAG, "Message service is disconnected");
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "与服务器的连接断开"));
	}
	
	void onSendMsgFail(){
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "发送请求失败"));
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG,  "In message service onBind");
		return this.mBinder;
	}
	
	public class MessageBinder extends Binder {
	    
		public EnhancedMessageService getService() {
		      return EnhancedMessageService.this;
	    }
	}

	@Override
	public void run() {
		this.flag = true;
		try {
			this.mServerSocket = new ServerSocket(this.port);
			this.connectServer();
			Log.i(TAG, "Start message server on port:" + this.port);
        	while(flag){
        		Socket client_socket = this.mServerSocket.accept();
				this.mServerAgentPool.execute(new ServerAgent(client_socket, this));
        	}
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	mServerAgentPool.shutdown();
        	try {
        		mServerAgentPool.shutdownNow();
    	    	 if (!mServerAgentPool.awaitTermination(60, TimeUnit.SECONDS)){
    	    		 Log.e(TAG, "mClientThreadPool did not terminate");  
    	    	 }
        	 } catch (InterruptedException ie) {
        		 mServerAgentPool.shutdownNow();
        	 }
        	this.onDisconnect();
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
    
	private void connectServer(){
		//Connect to server so it can remember me
    	new ClientAgent(this.remote_addr, this.remote_port, "", this).start();
	}

}

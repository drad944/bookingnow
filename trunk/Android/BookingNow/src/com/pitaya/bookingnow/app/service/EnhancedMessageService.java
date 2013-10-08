package com.pitaya.bookingnow.app.service;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;

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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.message.FoodMessage;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.message.TableMessage;

public class EnhancedMessageService extends Service implements Runnable, IMessageService {
	
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
    private ConnectionReceiver mConnectionReceiver;
    private boolean flag;
    private ExecutorService mMessageReceiverPool;
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
		this.mMessageReceiverPool =  new ThreadPoolExecutor(5,10,30L, TimeUnit.MINUTES, 
				new ArrayBlockingQueue<Runnable>(5), 
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mCM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mConnectionReceiver = new ConnectionReceiver();
        this.registerReceiver(mConnectionReceiver, mIntentFilter);
        this.start();
        Log.i(TAG,  "In message service oncreate");
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        if(intent != null && intent.getExtras() != null){
        	Bundle bundle = intent.getExtras();
        	if(bundle.getBoolean("connected") == false){
        		Log.i(TAG, "Connection status changed to false");
        		this.onDisconnect("网络中断");
        	} else {
        		this.start();
        	}
        } else {
        	this.start();
        }
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
    	Log.i(TAG, "In message service on destroy");
        mNM.cancelAll();
        this.unregisterReceiver(mConnectionReceiver);
        this.onDisconnect("消息服务意外终止");
    }
    
    public synchronized void start(){
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
	
	@Override
	public boolean sendMessage(Message message){
		new MessageSender(HttpService.IP, HttpService.REMOTE_PORT, parseMessage(message), this).start();
		return true;
	}
	
	@Override
	public boolean sendMessage(String msg) {
		if(msg == null || msg.equals("")){
			return false;
		} else {
			new MessageSender(HttpService.IP, HttpService.REMOTE_PORT, msg, this).start();
			return true;
		}
	}
	
	public boolean isReady(){
		return this.mServerSocket != null && this.mServerSocket.isBound();
				//Disable udp check for now
				//&& this.mUDPService != null && this.mUDPService.isRunnning();
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

	//Message sent by server
	void onMessage(String message){
		if(message.equals("bye")){
			this.onDisconnect("服务器终止了连接");
		} else if(message.equals("relogin")){
			this.onDisconnect("您已在别处登录");
		} else {
			Message msg = unparseMessage(message);
			onMessage(msg);
			Log.d(TAG, "Receive message: " + msg);
		}
	}
	
	//Reply message from server
	void onMessage(String message, MessageSender sender){
		Message msg = unparseMessage(message);
		onMessage(msg);
		sender.shutdown();
	}
	
	void onConnectServer(){
//		Disable udp check
//    	if(this.mUDPService == null || !this.mUDPService.isRunnning()){
//    		this.mUDPService = new UDPService(this);
//    		mUPDServerThread = new Thread(this.mUDPService);
//    		mUPDServerThread.start();
//    	}
		this.isConnecting = false;
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.SUCCESS, "连接服务器成功"));
	}
	
	void onUDPServiceStart(){
		this.isConnecting = false;
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.SUCCESS, "连接服务器成功"));
	}

	synchronized void onDisconnect(String msg){
		UserManager.setLoginUser(this, null);
		this.isConnecting = false;
		this.shutdown();
		if(this.mServerThread != null){
			this.mServerThread.interrupt();
			this.mServerThread = null;
		}
		if(this.mUDPService != null){
			this.mUDPService.shutdown();
		}
		if(this.mUPDServerThread != null){
			this.mUPDServerThread.interrupt();
		}
		Log.w(TAG, "Message service is disconnected");
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, msg));
	}
	
	void onSendMsgFail(){
		this.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "发送请求失败"));
	}
	
	private void shutdown(){
		this.flag = false;
		if(this.mServerSocket != null && !this.mServerSocket.isClosed()){
			try {
				this.mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.mServerSocket = null;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG,  "In message service onBind");
		return this.mBinder;
	}
	
	public class MessageBinder extends Binder {
		public IMessageService getService() {
			return EnhancedMessageService.this;
	    }
	}

	@Override
	public void run() {
		this.flag = true;
		try {
			this.mServerSocket = new ServerSocket(HttpService.PORT);
			this.connectServer();
			Log.i(TAG, "Start message server on port:" + HttpService.PORT);
        	while(flag){
        		Socket client_socket = this.mServerSocket.accept();
				//this.mMessageReceiverPool.execute(new MessageReceiver(client_socket, this));
        		new Thread(new MessageReceiver(client_socket, this)).start();
        	}
        } catch(IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Server socket error");
        } finally {
        	mMessageReceiverPool.shutdown();
        	try {
        		 mMessageReceiverPool.shutdownNow();
    	    	 if (!mMessageReceiverPool.awaitTermination(60, TimeUnit.SECONDS)){
    	    		 Log.e(TAG, "mMessageReceiverPool did not terminate");  
    	    	 }
        	} catch (InterruptedException ie) {
        		 mMessageReceiverPool.shutdownNow();
        	}
        	this.onDisconnect("与服务器的连接中断，请检查网络");
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
    	new MessageSender(HttpService.IP, HttpService.REMOTE_PORT, "", this).start();
	}

}

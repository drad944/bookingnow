package com.pitaya.bookingnow.app.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.pitaya.bookingnow.app.data.FileDownloadHandler;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.ProgressHandler;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.FileUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class HttpService {
	
	private static final String TAG = "HttpService";
	public static String IP = "10.111.3.116";
	//Local socket
	public static int PORT = 25252;
	//Server socket 
	public static int REMOTE_PORT = 19191;
	//Server and local UDP checking socket
	public static int UDPPORT = 19192;
	public static int HTTP_PORT = 18080;
	
	private static String URL = "http://10.111.3.116:9999/Booking/";
	private static ExecutorService pool;
	
	static {
		pool = new ThreadPoolExecutor(10, 30, 30L, TimeUnit.MINUTES, 
					new ArrayBlockingQueue<Runnable>(10), 
					new ThreadPoolExecutor.CallerRunsPolicy());
		IP = "192.168.0.102";
		PORT = 25252;
		REMOTE_PORT = 19191;
		UDPPORT = 19192;
		URL = "http://" + IP + ":"+HTTP_PORT+"/Booking/";
	}
	
	public static void setUrl(String url){
		URL = url;
	}
	
	public static void setIp(String ip){
		IP = ip.split(":")[0];
		URL = "http://" + ip + "/Booking/";
	}
	
	public static HttpResponse get(){
		HttpGet httpRequest = new HttpGet(URL);  
        HttpClient httpclient = new DefaultHttpClient();
        try {
			return httpclient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static void getFileViaPost(final String action, final StringEntity jsonparam, final FileDownloadHandler handler){
		pool.execute(new Thread(){
			@Override
			public void run(){
				jsonparam.setContentEncoding(HTTP.UTF_8);
				HttpPost httpRequest = new HttpPost(URL + action);
				httpRequest.addHeader(HTTP.CONTENT_TYPE, "application/json");
		        httpRequest.setEntity(jsonparam);
		        HttpClient httpclient = new DefaultHttpClient();
		        ByteArrayOutputStream out =null;
		        InputStream fileInStream = null;
	        	android.os.Message amsg = new android.os.Message();
		        Bundle bundle = new Bundle();
		        bundle.putString(HttpHandler.ACTION_TYPE, action);
		        try {
		        	HttpResponse response = httpclient.execute(httpRequest);
		        	int statuscode = response.getStatusLine().getStatusCode();
		        	if(response != null && statuscode == HttpStatus.SC_OK){
		        		HttpEntity respEnt = response.getEntity();
			        	if(respEnt.isStreaming()){
			        		fileInStream = respEnt.getContent();
			        		out = new ByteArrayOutputStream();
	        			 	byte[] buffer = new byte[1024];
			                int len = 0;
			                while((len = fileInStream.read(buffer)) != -1){
			                    out.write(buffer, 0, len);
			                }
			                byte[] result = out.toByteArray();
			                handler.setStream(result);
					        bundle.putInt(HttpHandler.RESULT, Constants.SUCCESS);
					        bundle.putString(HttpHandler.RESPONSE, "Success to get file, size is " + result.length);
			        	} else {
			        		Log.e(TAG, "The action does not return a stream");
			        		bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
			        		bundle.putInt(HttpHandler.ERROR_CODE, Constants.WRONG_ACTION_ERROR);
			        	}
		        	} else {
		        		bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
		        		bundle.putInt(HttpHandler.ERROR_CODE, statuscode);
		        	}
		         } catch (ClientProtocolException e) {
					e.printStackTrace();
	        		bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
	        		bundle.putInt(HttpHandler.ERROR_CODE, Constants.PROTOCOL_EXCEPTION_ERROR);
		         } catch (IOException e) {
					e.printStackTrace();
					bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
	        		bundle.putInt(HttpHandler.ERROR_CODE, Constants.IO_EXCEPTION_ERROR);
		         } finally {
        			FileUtil.close(out);
        			FileUtil.close(fileInStream);
		         }
	        	amsg.setData(bundle);
		        handler.sendMessage(amsg);
			}
		});
	}
	
	public static void post(final String action, final StringEntity jsonparam, final HttpHandler handler){
		pool.execute(new Thread(){
			
			@Override
			public void run(){
				jsonparam.setContentEncoding(HTTP.UTF_8);
				HttpPost httpRequest = new HttpPost(URL + action);
				httpRequest.addHeader(HTTP.CONTENT_TYPE, "application/json");
		        httpRequest.setEntity(jsonparam);
		        HttpClient httpclient = new DefaultHttpClient();
	        	android.os.Message amsg = new android.os.Message();
		        Bundle bundle = new Bundle();
	        	bundle.putString(HttpHandler.ACTION_TYPE, action);
		        try {
		        	HttpResponse response = httpclient.execute(httpRequest);
		        	int statuscode = response.getStatusLine().getStatusCode();
		        	if(response != null && statuscode == HttpStatus.SC_OK){
				        bundle.putInt(HttpHandler.RESULT, Constants.SUCCESS);
				        bundle.putString(HttpHandler.RESPONSE, EntityUtils.toString(response.getEntity()));
		        	} else {
		        		bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
		        		bundle.putInt(HttpHandler.ERROR_CODE, statuscode);
		        	}
		         } catch (ClientProtocolException e) {
					e.printStackTrace();
	        		bundle.putInt("result", Constants.FAIL);
	        		bundle.putInt(HttpHandler.ERROR_CODE, Constants.PROTOCOL_EXCEPTION_ERROR);
		         } catch (IOException e) {
					e.printStackTrace();
					bundle.putInt("result", Constants.FAIL);
	        		bundle.putInt(HttpHandler.ERROR_CODE, Constants.IO_EXCEPTION_ERROR);
		         }
	        	amsg.setData(bundle);
		        handler.sendMessage(amsg);
			}
			
		});

	}
	
}

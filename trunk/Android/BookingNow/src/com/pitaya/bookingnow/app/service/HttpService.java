package com.pitaya.bookingnow.app.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.ProgressHandler;
import com.pitaya.bookinnow.app.util.Constants;
import com.pitaya.bookinnow.app.util.FileUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class HttpService {
	
	private static String URL = "http://192.168.0.102:18080/Booking/";
	
	public static void setUrl(String url){
		URL = url;
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
	
	public static void testDownload(final Context context, final String filename, final Handler handler){
		new Thread(){
			@Override
			public void run(){
				HttpPost httpRequest = new HttpPost(URL+"Image/"+filename);
		        HttpClient httpclient = new DefaultHttpClient();
		        ByteArrayOutputStream out =null;
		        InputStream fileInStream = null;
		        try {
		        	HttpResponse response = httpclient.execute(httpRequest);
		        	android.os.Message amsg = new android.os.Message();
		        	Bundle bundle = new Bundle();
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
			                FileUtil.writeFile(context, filename, out.toByteArray());
					        bundle.putInt("result", Constants.SUCCESS);
					        bundle.putString("response", "success");
			        	} else {
					        bundle.putInt("result", Constants.SUCCESS);
					        bundle.putString("response", "Wrong action: not a stream");
			        	}
		        	} else {
		        		bundle.putInt("result", Constants.FAIL);
		        		bundle.putInt("statusCode", statuscode);
		        	}
		        	amsg.setData(bundle);
			        handler.sendMessage(amsg);
		         } catch (ClientProtocolException e) {
					e.printStackTrace();
		         } catch (IOException e) {
					e.printStackTrace();
		         } finally {
        			FileUtil.close(out);
        			FileUtil.close(fileInStream);
		         }
			}
			
		}.start();
	}
	
	public static void downloadFile(final String action, final Context context, final String filename, 
			final StringEntity jsonparam, final ProgressHandler handler){
		new Thread(){
			@Override
			public void run(){
				jsonparam.setContentEncoding("utf-8");
				HttpPost httpRequest = new HttpPost(URL + action);
				httpRequest.addHeader(HTTP.CONTENT_TYPE, "application/json");
		        httpRequest.setEntity(jsonparam);
		        HttpClient httpclient = new DefaultHttpClient();
		        ByteArrayOutputStream out =null;
		        InputStream fileInStream = null;
		        try {
		        	HttpResponse response = httpclient.execute(httpRequest);
		        	android.os.Message amsg = new android.os.Message();
		        	Bundle bundle = new Bundle();
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
			                
			                if(FileUtil.writeFile(context, filename, out.toByteArray())){
						        bundle.putInt("result", Constants.SUCCESS);
						        bundle.putString("detail", "Success to download file " + filename);
			                } else {
						        bundle.putInt("result", Constants.FAIL);
						        bundle.putString("detail", "Fail to save file " + filename);
			                }
			        	} else {
					        bundle.putInt("result", Constants.FAIL);
					        bundle.putString("detail", "Wrong action: not a stream");
			        	}
		        	} else {
		        		bundle.putInt("result", Constants.FAIL);
		        		bundle.putString("detail", "http");
		        		bundle.putInt("statusCode", statuscode);
		        	}
		        	amsg.setData(bundle);
			        handler.sendMessage(amsg);
		         } catch (ClientProtocolException e) {
					e.printStackTrace();
		         } catch (IOException e) {
					e.printStackTrace();
		         } finally {
        			FileUtil.close(out);
        			FileUtil.close(fileInStream);
		         }
			}
			
		}.start();
	}
	
	public static void post(final String action, final StringEntity jsonparam, final Handler handler){
		new Thread(){
			
			@Override
			public void run(){
				jsonparam.setContentEncoding("utf-8");
				HttpPost httpRequest = new HttpPost(URL + action);
				httpRequest.addHeader(HTTP.CONTENT_TYPE, "application/json");
		        httpRequest.setEntity(jsonparam);
		        HttpClient httpclient = new DefaultHttpClient();
		        try {
		        	HttpResponse response = httpclient.execute(httpRequest);
		        	android.os.Message amsg = new android.os.Message();
		        	Bundle bundle = new Bundle();
		        	int statuscode = response.getStatusLine().getStatusCode();
		        	if(response != null && statuscode == HttpStatus.SC_OK){
				        bundle.putInt("result", Constants.SUCCESS);
				        bundle.putString("detail", EntityUtils.toString(response.getEntity()));
		        	} else {
		        		bundle.putInt("result", Constants.FAIL);
		        		bundle.putInt("detail", statuscode);
		        	}
		        	amsg.setData(bundle);
			        handler.sendMessage(amsg);
		         } catch (ClientProtocolException e) {
					e.printStackTrace();
		         } catch (IOException e) {
					e.printStackTrace();
		         }
			}
			
		}.start();

	}
	
	
	
}

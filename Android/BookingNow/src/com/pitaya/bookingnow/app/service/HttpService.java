package com.pitaya.bookingnow.app.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

import android.os.Bundle;
import android.os.Handler;

public class HttpService {
	
	private static String URL;
	
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
	
	public static void post(final StringEntity param, final Handler handler){
		new Thread(){
			@Override
			public void run(){
				 param.setContentEncoding("utf-8");
				 HttpPost httpRequest = new HttpPost(URL);
				 httpRequest.addHeader(HTTP.CONTENT_TYPE, "application/json");
		         httpRequest.setEntity(param);
		         HttpClient httpclient = new DefaultHttpClient();
		         try {
		        	HttpResponse response = httpclient.execute(httpRequest);
					android.os.Message amsg = new android.os.Message();  
			        Bundle bundle = new Bundle();  
			        bundle.putSerializable("result", EntityUtils.toString(response.getEntity()));
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

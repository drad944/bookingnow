package com.pitaya.bookingnow.app;

import com.pitaya.bookingnow.app.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewer extends Activity {

	private static Handler handler;
	
	private WebView wv;
	private ProgressDialog pd;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		init();
        loadurl(wv, url);
        if(handler == null){
	        handler = new Handler(){
	        	public void handleMessage(Message msg)
	    	    {
	    	      if (!Thread.currentThread().isInterrupted())
	    	      {
	    	        switch (msg.what)
	    	        {
	    	        case 0:
	    	        	pd.show();        	
	    	        	break;
	    	        case 1:
	    	        	pd.hide();
	    	        }
	    	      }
	    	      super.handleMessage(msg);
	    	    }
	        };
        }
	}
	
	public void init(){
    	wv = (WebView)findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(0);
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	loadurl(view,url);
                return true;   
            }
 
        });
        
        wv.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){
             	if(progress==100){
            		handler.sendEmptyMessage(1);
            	}   
                super.onProgressChanged(view, progress);   
            }   
        });
 
    	pd = new ProgressDialog(WebViewer.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Waiting");
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.booking, menu);
		return true;
	}
	
	public void loadurl(final WebView view,final String url){
    	new Thread(){
        	public void run(){
        		handler.sendEmptyMessage(0);
        		view.loadUrl(url);
        	}
        }.start();
    }

}

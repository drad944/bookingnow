package com.pitaya.bookinnow.app.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.Closeable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtil {
	
	public static boolean writeFile(Context context, String fileName, byte[] bytes){
		FileOutputStream fout = null;  
		try{   
			fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		    fout.write(bytes);
		    return true;
		}  catch(IOException e) {   
			e.printStackTrace();
			return false;
		}  finally {
			close(fout); 
		}
	}
	
	public static boolean writeFile(Context context, String fileName, byte[] bytes, int Mode){
		FileOutputStream fout = null;  
		try{   
			fout = context.openFileOutput(fileName, Mode);
		    fout.write(bytes);
		    return true;
		}  catch(IOException e) {   
			e.printStackTrace();
			return false;
		}  finally {
			close(fout); 
		}
	}
	
	public static void removeFile(Context context, String fileName){
		context.deleteFile(fileName);
	}
	
	public static Bitmap readBitmap(Context context, String fileName){
		FileInputStream input = null;    
		try {
	    	input = context.openFileInput(fileName);
	        return BitmapFactory.decodeStream(input);
		} catch (Exception e) {
		    return null;
		} finally {
			close(input);
		}
	}
	
	public static Bitmap Bytes2Bimap(byte[] b){
        if(b.length!=0){  
            return BitmapFactory.decodeByteArray(b, 0, b.length);  
        }  else {  
            return null;  
        }  
 	}  
	
	public static byte[] getImageBytes(Bitmap bitmap){
	    int size = bitmap.getWidth()*bitmap.getHeight()*4;
	    ByteArrayOutputStream out = new ByteArrayOutputStream(size);
	    try {
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return out.toByteArray();
	}
	
	public static void close(Closeable closeable) {
	    try {
		      if (closeable != null) {
		        closeable.close();
		      }
	    } catch (IOException e) {
	    	  e.printStackTrace();
	    }
	}
	
}

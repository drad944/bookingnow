package com.pitaya.bookingnow.util;

import java.io.File;
import java.util.Date;

import org.apache.struts2.ServletActionContext;

public class FileUtil {
	
	public static String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	

	public static void removeFiles(String dir, String pattern){
		File directory = new File(dir);
		if(!directory.isDirectory()){
			return;
		} else {
			for(File file : directory.listFiles()){
				if(file.getName().matches(pattern)){
					file.delete();
				}
			}
		}
	}
	
	public static String getType(String path) {
		if (path != null && path.length() > 0) {
			return path.substring(path.lastIndexOf(".") + 1);
		}else {
			return null;
		}
	}
	
	public static String generateRandomString(int length) {
		if (length > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < length; i++) {
				sb.append(allChars.charAt((int) (Math.random() * allChars.length())));
			}
			return sb.toString();
		}else {
			return null;
		}
	}
	
	public static String[] generateFilePath(String basePath, String fileType) {
		if(!basePath.endsWith(SystemUtils.getSystemDelimiter())){
			basePath += SystemUtils.getSystemDelimiter();
		}
		String id = new Date().getTime() + "_"  + generateRandomString(10) + "."  + fileType;
		return new String[]{basePath + id, id};
	}
	
	public static String getSavePath(String basePath, String id){
		if(!basePath.endsWith(SystemUtils.getSystemDelimiter())){
			basePath += SystemUtils.getSystemDelimiter();
		}
		return basePath + id;
	}
	
	public static String generateRelativePath(String absolutePath) {
		
		File file = new File(absolutePath);
		if (file.exists()) {
			String rootpath = ServletActionContext.getServletContext().getRealPath("/");
			String relativePath = absolutePath.substring(rootpath.length());
			return relativePath;
		}
		
		return null;
	}
}

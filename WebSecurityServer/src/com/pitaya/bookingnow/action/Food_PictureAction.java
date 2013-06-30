package com.pitaya.bookingnow.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.service.impl.Food_PictureService;
import com.pitaya.bookingnow.util.Constants;

public class Food_PictureAction extends ActionSupport{
	
	private static final long serialVersionUID = 2899868808373852803L;
	
	private Food_PictureService food_PictureService;
	
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	private Food food;
	

	private String downloadFileName;//下载时显示的下载文件名
	private File realDownloadFile;//下载的真实文件
	
	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Food_PictureService getFood_PictureService() {
		return food_PictureService;
	}

	public void setFood_PictureService(Food_PictureService food_PictureService) {
		this.food_PictureService = food_PictureService;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public File getRealDownloadFile() {
		return realDownloadFile;
	}

	public void setRealDownloadFile(File realDownloadFile) {
		this.realDownloadFile = realDownloadFile;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}


	
	//下载文件时的输出流入口
	public InputStream getDownloadFileStream() { 
		//* 注意使用getResourceAsStream方法时，文件路径必须是以“/”开头，且是相对路径。这个路径是相对于项目根目录的。 
		//* 可以用return new FileInputStream(fileName)的方法来得到绝对路径的文件。
		
		//return ServletActionContext.getServletContext().getResourceAsStream("/" + downloadFileName); 
		
		try {
			return new FileInputStream(realDownloadFile);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public InputStream getSmallImageFood_Picture(){
		if(this.food != null && food.getId() != null){
			Food_Picture picture = food_PictureService.searchPictureByFoodId(food.getId());
			
			return  new ByteArrayInputStream(picture.getSmall_image());
		} else {
			String resultStr = "{ result : "+Constants.FAIL+", detail: \"Can't get food parameter or the id is missing\"}";
			return new ByteArrayInputStream(resultStr.getBytes());
		}
	}
	
	public InputStream getLargeImageFood_Picture(){
		if(this.food != null && food.getId() != null){
			Food_Picture picture = food_PictureService.searchPictureByFoodId(food.getId());
			
			return  new ByteArrayInputStream(picture.getBig_image());
		} else {
			String resultStr = "{ result : "+Constants.FAIL+", detail: \"Can't get food parameter or the id is missing\"}";
			return new ByteArrayInputStream(resultStr.getBytes());
		}
	}
	
	//上传文件
	public String uploadFood_Picture() {
		try {
			
			FileInputStream fis = new FileInputStream(upload);//上传目标文件

			
			byte[] buffer = new byte[1024];
			byte[] pictureImage = new byte[]{};
			
			int len = 0;
			int startIndex = 0;
			while((len = fis.read(buffer)) > 0) {
				
				System.arraycopy(buffer, 0, pictureImage, startIndex, len);
				startIndex = startIndex + len;
			}
			fis.close();
			
			
			
			return "uploadSuccess";
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "uploadFail";		
	}
	
	//下载文件
	public String downloadFood_Picture() {
		return "downloadSuccess";
	}
	
	public String getDownloadFileName() {
		try { 
			if(downloadFileName != null) {
				downloadFileName = new String(downloadFileName.getBytes(), "iso-8859-1"); 
			}
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace(); 
		} 

		return downloadFileName;
	}
}

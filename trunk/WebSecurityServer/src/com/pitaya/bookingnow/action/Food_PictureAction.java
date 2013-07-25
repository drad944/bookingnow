package com.pitaya.bookingnow.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.service.IFood_PictureService;
import com.pitaya.bookingnow.util.Constants;

public class Food_PictureAction extends BaseAction{
	
	private static final long serialVersionUID = 2899868808373852803L;
	
	private IFood_PictureService food_pictureService;
	
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	private Food food;
	private InputStream largeImageStream;
	private InputStream smallImageStream;

	private String downloadFileName;//下载时显示的下载文件名
	private File realDownloadFile;//下载的真实文件
	
	
	public InputStream getLargeImageStream() {
		return largeImageStream;
	}

	public void setLargeImageStream(InputStream largeImageStream) {
		this.largeImageStream = largeImageStream;
	}

	public InputStream getSmallImageStream() {
		return smallImageStream;
	}

	public void setSmallImageStream(InputStream smallImageStream) {
		this.smallImageStream = smallImageStream;
	}

	public IFood_PictureService getFood_pictureService() {
		return food_pictureService;
	}

	public void setFood_pictureService(IFood_PictureService food_pictureService) {
		this.food_pictureService = food_pictureService;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
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

	public String findSmallFood_Picture() {
		if(this.food != null && food.getId() != null){
			Food_Picture picture = food_pictureService.searchSmallPictureByFoodId(food.getId());
			if(picture != null && picture.getSmall_image() != null) {
				smallImageStream = new ByteArrayInputStream(picture.getSmall_image());
				return "findSmallImageSuccess";
			}
			
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}

	public String findLargeFood_Picture(){
		if(this.food != null && food.getId() != null){
			Food_Picture picture = food_pictureService.searchLargePictureByFoodId(food.getId());
			if(picture != null && picture.getLarge_image() != null) {
				largeImageStream = new ByteArrayInputStream(picture.getLarge_image());
				return "findLargeImageSuccess";
			}
			
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
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
}

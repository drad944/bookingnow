package com.pitaya.bookingnow.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtil {
	public static String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	public static String parseImageType(String imagePath) {
		if (imagePath != null && imagePath.length() > 0) {
			return imagePath.substring(imagePath.lastIndexOf(".") + 1);
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
	
	public static String generateImagePath(String basicPath,String imageType) {
		if (basicPath.endsWith("/") || basicPath.endsWith("\\")) {
			return basicPath + new Date().getTime() + "_" + generateRandomString(10) + "." + imageType;
		}else {
			return basicPath + SystemUtils.getSystemDelimiter() + new Date().getTime() + "_" + generateRandomString(10) + "." + imageType;
		}
	}
	
	public static String generateRelativePath(String absolutePath) {
		File file = new File(absolutePath);
		if (file.exists()) {
			
			String parentPath = file.getParentFile().getParentFile().getParentFile().getAbsolutePath();
			String relativePath = absolutePath.substring(parentPath.length() + 1);
			return relativePath;
		}
		
		return null;
	}
			
	/** */
	/**
	 * 图像类型转换 GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
	 */
	public static void convert(String source, String targetPath,String imageType) {
		try {
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, imageType, new File(targetPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean cut(String srcPath,String imageType,String targetPath,int x,int y,int width,int height) {
		ImageInputStream iis = null;
		FileInputStream fis = null;
		File srcFile = null;
		File targetFile = null;
		try {
			
			srcFile = new File(srcPath);
			targetFile = new File(targetPath);
			if (srcFile.exists()) {
				fis = new FileInputStream(srcFile);
				Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(imageType);
				ImageReader reader = it.next();
				iis = ImageIO.createImageInputStream(fis);
				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();
				Rectangle rect = new Rectangle(x, y, width, height);
				param.setSourceRegion(rect);
				BufferedImage bi = reader.read(0, param);
				ImageIO.write(bi, imageType, targetFile);
			}else {
				return false;
			}
			
			
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (fis != null){
					fis.close();
				}
				if (iis != null){
					iis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		srcFile.delete();
		return true;
	}
	
	public static boolean cut(InputStream srcInputStream,String imageType,String targetPath,int x,int y,int width,int height) {
		ImageInputStream iis = null;
		try {
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(imageType);
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(srcInputStream);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, imageType, new File(targetPath));
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (srcInputStream != null){
					srcInputStream.close();
				}
				if (iis != null){
					iis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
}

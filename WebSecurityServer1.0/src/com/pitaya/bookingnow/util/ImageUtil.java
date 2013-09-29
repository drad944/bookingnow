package com.pitaya.bookingnow.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
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

import org.apache.struts2.ServletActionContext;

public class ImageUtil {
	
	public static String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public static String[] imageTypes = {"JPEG", "JPG", "PNG", "BMP"};
	
	public static class ImageInfo {
		
		private int width;
		private int height;
		
		public ImageInfo(){}
		
		public void setWidth(int w){
			this.width = w;
		}
		
		public int getWidth(){
			return this.width;
		}
		
		public void setHeight(int h){
			this.height = h;
		}
		
		public int getHeight(){
			return this.height;
		}
	} 
	
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
			String rootpath = ServletActionContext.getServletContext().getRealPath("/");
			String relativePath = absolutePath.substring(rootpath.length() + 1);
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
	
	public void cutImage(String imagePath, String imageType, int x, int y,
			int w, int h) {
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(imagePath));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度

			// 若原图大小大于切片大小，则进行切割
			if (srcWidth >= w && srcHeight >= h) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);

				int x1 = x * srcWidth / 400;
				int y1 = y * srcHeight / 270;
				int w1 = w * srcWidth / 400;
				int h1 = h * srcHeight / 270;

				cropFilter = new CropImageFilter(x1, y1, w1, h1);
				img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(w1, h1,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, imageType, new File(imagePath));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean cut(String srcPath,String imageType,String targetPath,int x,int y,int width,int height,Double scaleRatio) {
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
				if(scaleRatio != null && scaleRatio > 0) {
					x = new Double(x * scaleRatio).intValue();
					y = new Double(y * scaleRatio).intValue();
					width = new Double(width * scaleRatio).intValue();
					height = new Double(height * scaleRatio).intValue();
				}
				
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
	
	public static ImageInfo getImageInfo(File imagefile, float rate){
		ImageInfo imageInfo = null;
		Iterator<ImageReader> readers = null;
		ImageReader reader = null;
		ImageInputStream iis = null;
		try {
		   readers = ImageIO.getImageReadersByFormatName(FileUtil.getType(imagefile.getName()));
		   reader = (ImageReader)readers.next();
		   iis = ImageIO.createImageInputStream(imagefile);
		   reader.setInput(iis, true);
		   imageInfo = new ImageInfo();
		   imageInfo.setWidth((int)(reader.getWidth(0) * rate));
		   imageInfo.setHeight((int)(reader.getHeight(0) * rate));
		} catch (IOException e) {
		   e.printStackTrace();
	    } finally {
	       try {
	    	   iis.close();
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	    }
		return imageInfo;
	}
	
	public static boolean scale(String srcfile, String destfile, String type, int widths, int heights) {
		boolean result = true;
        try {  
            BufferedImage src = ImageIO.read(new File(srcfile));
            Image image = src.getScaledInstance(widths, heights, Image.SCALE_DEFAULT);  
            BufferedImage tag = new BufferedImage(widths, heights, BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();  
            ImageIO.write(tag, type.toUpperCase(), new File(destfile));
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } 
        return result;  
    }
	
	public static boolean isImage(String type){
		for(String t : imageTypes){
			if(t.equalsIgnoreCase(type)){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String args[]){
		String path = "D:\\pitaya\\BookingNow\\WebSecurityServer\\WebContent\\images\\";
//		ImageUtil.scale(path + "food\\1_l_123241422.jpg", path + "test.jpg", 640, 400);
		ImageInfo infos = ImageUtil.getImageInfo(new File(path + "test.jpg"), 1f);
		if(infos != null){
			System.out.println(infos.width +  " " + infos.height);
		}
	}
}

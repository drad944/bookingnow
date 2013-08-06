package com.pitaya.bookingnow.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class OperateImage
{
	private String srcpath;
	private String subpath;
	private int x;
	private int y;
	private int width;
	private int height;

	public OperateImage()
	{
	}

	public OperateImage(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public String getSrcpath()
	{
		return srcpath;
	}
	public void setSrcpath(String srcpath)
	{
		this.srcpath = srcpath;
	}
	public String getSubpath()
	{
		return subpath;
	}
	public void setSubpath(String subpath)
	{
		this.subpath = subpath;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
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
		}finally
		{
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

	public void cut() throws IOException
	{
		FileInputStream is = null;
		ImageInputStream iis = null;
		try
		{
			is = new FileInputStream(srcpath);
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("PNG");
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "PNG", new File(subpath));
		}
		finally
		{
			if (is != null){
				is.close();
			}
			if (iis != null){
				iis.close();
			}
		}
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
	
	public static void main(String[] args) throws Exception
	{
		String name = "D:\\2.png";
		OperateImage o = new OperateImage(10, 32, 16, 16);
		o.setSrcpath(name);
		o.setSubpath("D:\\21.png");
		o.cut();
	}
	
}

package com.pitaya.bookingnow.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtil {
	
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

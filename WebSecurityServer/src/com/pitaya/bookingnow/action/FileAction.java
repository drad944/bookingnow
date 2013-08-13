package com.pitaya.bookingnow.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.pitaya.bookingnow.util.FileUtil;
import com.pitaya.bookingnow.util.ImageUtil;
import com.pitaya.bookingnow.util.ImageUtil.ImageInfo;

public class FileAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1073101635374140668L;
	
	private File uploadFile; //上传的文件
    private String uploadFileFileName; //文件名称
    private String uploadFileContentType; //文件类型
    
    private String fileId;
    private int height;
    private int width;
    private ImageInfo imageInfo;
    
	public File getUploadFile() {
		return this.uploadFile;
	}
	
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	public String getUploadFileFileName() {
		return uploadFileFileName;
	}
	
	public void setUploadFileFileName(String fileName) {
		this.uploadFileFileName = fileName;
	}
	
	public String getUploadFileContentType() {
		return uploadFileContentType;
	}
	
	public void setUploadFileContentType(String type) {
		this.uploadFileContentType = type;
	}
	
	public void setFileId(String id){
		this.fileId = id;
	}
	
	public String getFileId(){
		return this.fileId;
	}
	
	public void setImageInfo(ImageInfo info){
		this.imageInfo = info;
	}
	
	public ImageInfo getImageInfo(){
		return this.imageInfo;
	}
	
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
	
    public String uploadFile() {
		String basepath = ServletActionContext.getServletContext().getRealPath("/images/temp");
        if (uploadFile != null) {
        	File savefile  = null;
        	String filetype = null;
        	if(this.fileId == null || this.fileId.equals("")){
            	filetype = FileUtil.getType(uploadFileFileName);
            	String [] saveinfos = FileUtil.generateFilePath(basepath, filetype);
                savefile = new File(saveinfos[0]);
                this.fileId = saveinfos[1];
        	} else {
        		filetype = FileUtil.getType(this.fileId);
        		savefile = new File(FileUtil.getSavePath(basepath, fileId));
        		if(savefile.exists()){
        			savefile.delete();
        		}
        	}

            if (!savefile.getParentFile().exists()){
            	savefile.getParentFile().mkdirs();
            }
            try {
				FileUtils.copyFile(uploadFile, savefile);
			} catch (IOException e) {
				e.printStackTrace();
				this.setFileId("null");
				return "Fail";
			}
            uploadFile.delete();
            if(ImageUtil.isImage(filetype)){
            	this.imageInfo = ImageUtil.getImageInfo(savefile, 0.5f);
            	if(this.imageInfo != null){
                	this.width = this.imageInfo.getWidth();
                	this.height = this.imageInfo.getHeight();
            	}
        		return "uploadImageSuccess";
            } else {
            	return "uploadSuccess";
            }
        }else{
        	this.setFileId("null");
            return "Fail";  
        }  
	}
	
}

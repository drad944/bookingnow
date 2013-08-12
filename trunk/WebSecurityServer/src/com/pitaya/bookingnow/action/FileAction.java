package com.pitaya.bookingnow.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.pitaya.bookingnow.util.FileUtil;

public class FileAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1073101635374140668L;
	
	private File uploadFile; //上传的文件
    private String uploadFileFileName; //文件名称
    private String uploadFileContentType; //文件类型
    
    private String fileId;
    
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
    
    public String uploadFile() {
		String basepath = ServletActionContext.getServletContext().getRealPath("/images/temp");
        if (uploadFile != null) {
        	File savefile  = null;
        	if(this.fileId == null || this.fileId.equals("")){
            	String filetype = FileUtil.getType(uploadFileFileName);
            	String [] saveinfos = FileUtil.generateFilePath(basepath, filetype);
                savefile = new File(saveinfos[0]);
                this.fileId = saveinfos[1];
        	} else {
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
			return "uploadSuccess";  
        }else{
        	this.setFileId("null");
            return "Fail";  
        }  
	}
	
}

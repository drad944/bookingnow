package com.pitaya.bookingnow.app.data;

public class FileDownloadHandler extends HttpHandler {
	
	protected byte[] fileBytes;
	
	public void setStream(byte[] bytes){
		this.fileBytes = bytes;
	}

}

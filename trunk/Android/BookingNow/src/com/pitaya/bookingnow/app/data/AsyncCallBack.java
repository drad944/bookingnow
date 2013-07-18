package com.pitaya.bookingnow.app.data;

public interface AsyncCallBack {
	public void onSuccess(String action, String response);
	public void onFail(String action, int statuscode);
}

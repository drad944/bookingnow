package com.pitaya.bookingnow.action;


import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.service.IAccountService;


public class AccountAction extends ActionSupport{
	private static final long serialVersionUID = 1921350238570284375L;
	
	private IAccountService accountService;
	private Account account;
	public IAccountService getAccountService() {
		return accountService;
	}
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public String findAccount() {
		if(account != null) {
        	System.out.println("userName:"+account.getName()+"\n"+"password:"+account.getPassword());
        }
        Account realAccount = accountService.selectForLogin(account);
        if(realAccount != null){  
            return "findSuccess";  
        }else{  
            return "findFail";  
        }  
	}
	
	public String registerAccount() {
		if(account != null) {
        	System.out.println("userName:"+account.getName()+"\n"+"password:"+account.getPassword() +"\n"+"role:"+account.getRole());
        }
        if(accountService.insert(account) == 1){  
            return "registerSuccess";  
        }else{  
            return "registerFail";  
        }  
	}
	
	public String updateAccount() {
		if(account != null) {
        	System.out.println("userName:"+account.getName()+"\n"+"password:"+account.getPassword() +"\n"+"role:"+account.getRole());
        }
        if(accountService.updateByPrimaryKeySelective(account) == 1){  
            return "updateSuccess";  
        }else{  
            return "updateFail";  
        }  
	}
	
	public String removeAccount() {
		if(account != null) {
        	System.out.println("userName:"+account.getName()+"\n"+"password:"+account.getPassword() +"\n"+"role:"+account.getRole());
        }
        if(accountService.deleteByPrimaryKey(1) == 1){  
            return "removeSuccess";  
        }else{  
            return "removeFail";  
        }  
	}
	
}

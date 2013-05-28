package com.pitaya.bookingnow.action;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.service.IAccountService;


@Controller
@Scope("prototype")
public class AccountAction extends ActionSupport{
	private static final long serialVersionUID = 1921350238570284375L;
	
	@Resource
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
	
	public String findAccount(Account account) {
		
		if(accountService.findAccount(account.getName(), account.getPassword()) != null) {
			return "findSuccess";
		}else {
			return "findFail";
		}
		
	}
	
	
}

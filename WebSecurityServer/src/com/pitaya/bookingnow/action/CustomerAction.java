package com.pitaya.bookingnow.action;


import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.service.ICustomerService;


public class CustomerAction extends BaseAction{
	private static final long serialVersionUID = 1921350238570284375L;
	
	private ICustomerService customerService;
	
	private Customer customer;
	
	private Customer loginCustomer;
	
	public ICustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(ICustomerService customerService) {
		this.customerService = customerService;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getLoginCustomer() {
		return loginCustomer;
	}

	public void setLoginCustomer(Customer loginCustomer) {
		this.loginCustomer = loginCustomer;
	}

	public String loginCustomer() {
		
		if(customer != null) {
        	loginCustomer = customerService.login(customer);
        }
        
        if(loginCustomer != null){ 
        	Map<String,Object> session = ActionContext.getContext().getSession();
    		session.put("loginCustomer", loginCustomer);
    		
            return "loginSuccess";  
        }else{  
            return "loginFail";  
        }  
	}
	
	public String logoutCustomer() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		
		session.remove("loginCustomer");
		return "logoutSuccess";
	}
	
	public String findCustomer() {
		List<Customer> loginCustomers = null;
		if(customer != null) {
        	loginCustomers = customerService.searchCustomers(customer);
        }
        
        if(loginCustomers != null){  
            return "findSuccess";  
        }else{  
            return "findFail";  
        }  
	}
	
	public String registerCustomer() {
		if(customer != null) {
			if(customerService.add(customer)){  
	            return "registerSuccess";  
	        }else{  
	            return "registerFail";  
	        }  
        }
		return "registerFail";  
	}
	
	public String updateCustomer() {
		if(customer != null) {
			if(customerService.modify(customer)){  
	            return "updateSuccess";  
	        }else{  
	            return "updateFail";  
	        }  
        }
            return "updateFail";  
	}
	
	
	
	public String removeCustomer() {
		if(customer != null) {
			if(customerService.remove(customer)){  
	            return "removeSuccess";  
	        }else{  
	            return "removeFail";  
	        }  
        }
            return "removeFail";  
	}
	
}

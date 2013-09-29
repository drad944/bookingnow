package com.pitaya.bookingnow.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.service.ICustomerService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;


public class CustomerAction extends BaseAction{
	private static final long serialVersionUID = 1921350238570284375L;
	
	private ICustomerService customerService;
	
	private SearchParams params;
	
	private Map<String, List<Customer>> matchedCustomers;
	
	private Customer customer;
	
	private Customer loginCustomer;
		
	public SearchParams getParams() {
		return params;
	}

	public void setParams(SearchParams params) {
		this.params = params;
	}

	public Map<String, List<Customer>> getMatchedCustomers() {
		return matchedCustomers;
	}

	public void setMatchedCustomers(Map<String, List<Customer>> matchedCustomers) {
		this.matchedCustomers = matchedCustomers;
	}

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
        	result = customerService.login(customer);
        }
        
        if(result.isExecuteResult()){ 
        	Map<String,Object> session = ActionContext.getContext().getSession();
        	loginCustomer = result.getCustomer();
    		session.put("loginCustomer", loginCustomer);
    		
            return "loginCustomerSuccess";  
        }else{  
            return "Fail";  
        }  
	}
	
	public String logoutCustomer() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		
		session.remove("loginCustomer");
		return "logoutCustomerSuccess";
	}
	
	public String findCustomer() {
		if(customer != null) {
        	List<Customer> customers = customerService.searchCustomers(customer);
        	if (matchedCustomers == null) {
				matchedCustomers = new HashMap<String,List<Customer>>();
			}
        	matchedCustomers.put("result", customers);
        	return "findCustomerSuccess";
        }else {
			if (result == null) {
				result = new MyResult();
			}
			result.setErrorType(Constants.FAIL);
			result.setExecuteResult(false);
			result.getErrorDetails().put("customer_exist", "can not find customer in client data.");
			return "Fail";
		}
	}
	
	public String registerCustomer() {
		if(customer != null) {
			result = customerService.add(customer);
			
			if(result.isExecuteResult()){ 
				customer = result.getCustomer();
	            return "registerCustomerSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail"; 
	}
	
	public String updateCustomer() {
		if(customer != null) {
			result = customerService.modify(customer);
			
			if(result.isExecuteResult()){ 
				customer = result.getCustomer();
	            return "updateCustomerSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail";
	}
	
	
	
	public String removeCustomer() {
		if(customer != null) {
			result = customerService.removeCustomerById(customer.getId());
			
			if(result.isExecuteResult()){ 
				customer = result.getCustomer();
	            return "removeCustomerSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "removeFail";
	}
	
	
	public String findAllCustomer() {
		
		List<Customer> customers = customerService.searchAllCustomers();
    	if (matchedCustomers == null) {
			matchedCustomers = new HashMap<String,List<Customer>>();
		}
    	matchedCustomers.put("result", customers);
    	return "findAllCustomerSuccess";
	}
}

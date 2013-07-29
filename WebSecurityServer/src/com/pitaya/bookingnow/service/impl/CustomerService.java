package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.CustomerMapper;
import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.service.ICustomerService;
import com.pitaya.bookingnow.util.MyResult;

public class CustomerService implements ICustomerService{

	private CustomerMapper customerDao;

	public CustomerMapper getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerMapper customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	public MyResult add(Customer customer) {
		MyResult result = new MyResult();
		if (customer != null) {
			if (customerDao.insert(customer) == 1) {
				result.setExecuteResult(true);
				result.setCustomer(customerDao.selectByPrimaryKey(customer.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to insert customer to DB.");
			}
		}else {
			result.getErrorDetails().put("customer_exist", "can not find customer info in client data.");
		}
		return result;
	}

	@Override
	public MyResult removeCustomerById(Long id) {
		MyResult result = new MyResult();
		if (id != null) {
			if (customerDao.deleteByPrimaryKey(id) == 1) {
				
				result.setExecuteResult(true);
				return result;
			}else {
				throw new RuntimeException("fail to delete customer in DB.");
			}
		}else {
			result.getErrorDetails().put("customer_exist", "can not find customer id in client data.");
		}
		return result;
	}

	@Override
	public MyResult modify(Customer customer) {
		MyResult result = new MyResult();
		if (customer != null && customer.getId() != null) {
			if (customerDao.updateByPrimaryKeySelective(customer) == 1) {
				
				result.setExecuteResult(true);
				result.setCustomer(customerDao.selectByPrimaryKey(customer.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to update customer info in DB.");
			}
		}else {
			result.getErrorDetails().put("customer_exist", "can not find customer id in client data.");
		}
		return result;
	}

	@Override
	public List<Customer> searchCustomers(Customer customer) {
		MyResult result = new MyResult();
		if (customer != null) {
			return customerDao.searchCustomers(customer);
		}else {
			result.getErrorDetails().put("customer_exist", "can not find customer in client data.");
		}
		return null;
	}

	@Override
	public MyResult login(Customer customer) {
		MyResult result = new MyResult();
		if (customer != null) {
			Customer loginCustomer = customerDao.login(customer);
			if (loginCustomer != null && loginCustomer.getId() != null) {
				result.setExecuteResult(true);
				return result;
			}else {
				result.getErrorDetails().put("customer_exist", "can not find customer in DB data.");
			}
		}else {
			result.getErrorDetails().put("customer_exist", "can not find customer in client data.");
		}
		
		return result;
	}

	@Override
	public Customer searchCustomerById(Long id) {
		if (id != null) {
			return customerDao.selectByPrimaryKey(id);
		}
		return null;
	}

	@Override
	public List<Customer> searchCustomersWithRole(Customer customer) {
		MyResult result = new MyResult();
		if (customer != null) {
			return customerDao.searchCustomersWithRole(customer);
		}else {
			result.getErrorDetails().put("customer_exist", "can not find customer in client data.");
		}
		return null;
	}

	@Override
	public List<Customer> searchAllCustomers() {
		return customerDao.searchAllCustomers();
	}

}

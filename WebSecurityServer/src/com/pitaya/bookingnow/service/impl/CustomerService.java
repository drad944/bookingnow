package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.CustomerMapper;
import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.service.ICustomerService;

public class CustomerService implements ICustomerService{

	private CustomerMapper customerDao;

	public CustomerMapper getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerMapper customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	public boolean modify(Customer customer) {
		int result = customerDao.updateByPrimaryKeySelective(customer);
		if(result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean add(Customer customer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCustomerById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Customer customer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Customer> searchCustomers(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer login(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

}

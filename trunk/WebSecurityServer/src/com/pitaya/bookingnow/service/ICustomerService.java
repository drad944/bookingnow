package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Customer;


public interface ICustomerService {
	
	boolean add(Customer customer);
	
	boolean removeCustomerById(Long id);
	
	boolean remove(Customer customer);

	boolean modify(Customer customer);
	
	List<Customer> searchCustomers(Customer customer);
	
	Customer login(Customer customer);
	
	Customer searchCustomerById(Long id);
}
package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.util.MyResult;


public interface ICustomerService {
	
	MyResult add(Customer customer);
	
	MyResult removeCustomerById(Long id);
	
	MyResult modify(Customer customer);
	
	List<Customer> searchCustomers(Customer customer);
	
	List<Customer> searchCustomersWithRole(Customer customer);
	
	List<Customer> searchAllCustomers();
	
	MyResult login(Customer customer);
	
	Customer searchCustomerById(Long id);
}
package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Customer_Picture;


public interface ICustomer_PictureService {
	
	boolean add(Customer_Picture customer_picture);
	
	boolean removeCustomer_PictureById(Long id);
	
	boolean remove(Customer_Picture customer_picture);

	boolean modify(Customer_Picture customer_picture);
	
	List<Customer_Picture> searchCustomer_Pictures(Customer_Picture customer_picture);
	
	
}
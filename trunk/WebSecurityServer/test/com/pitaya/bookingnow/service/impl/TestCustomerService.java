package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.service.ICustomerService;
import com.pitaya.bookingnow.util.Constants;


public class TestCustomerService {
	private ICustomerService customerService;

	public ICustomerService getCustomerService() {
		return customerService;
	}
	
	public void setCustomerService(ICustomerService customerService) {
		this.customerService = customerService;
	}
	
	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext(
				"src/applicationContext.xml");
		ICustomerService customerService = aCtx.getBean(ICustomerService.class);
		assertNotNull(customerService);
		this.customerService = customerService;
	}
	 public void showCustomer(Customer customer) {
		 if (customer != null) {
			System.out.println("Id : " + customer.getId());
			System.out.println("Account : " + customer.getAccount());
			System.out.println("Address : " + customer.getAddress());
			System.out.println("Email : " + customer.getEmail());
			System.out.println("Image_absolute_path : " + customer.getImage_absolute_path());
			System.out.println("Image_relative_path : " + customer.getImage_relative_path());
			System.out.println("Name : " + customer.getName());
			System.out.println("Password : " + customer.getPassword());
			System.out.println("Phone : " + customer.getPhone());
			System.out.println("Birthday : " + customer.getBirthday());
			System.out.println("Image_size : " + customer.getImage_size());
			System.out.println("ModifyTime : " + customer.getModifyTime());
			System.out.println("Sex : " + customer.getSex());
			System.out.println("Enabled : " + customer.getEnabled());
			System.out.println();
		}
	 }
	 
	 @Test
	 public void testSearchById() {
		 Long customerId = 1l;
		 showCustomer(customerService.searchCustomerById(customerId));
	 }
	 
	 @Test
		public void testAddCustomer() {

			Customer newCustomer = new Customer();

			newCustomer.setAccount("shax");
			newCustomer.setAddress("lsdjf sldf sdlfkj sldkf");
			newCustomer.setBirthday(new Date().getTime());
			newCustomer.setEmail("shax@qq.com");
			newCustomer.setName("shax");
			newCustomer.setPassword("1234");
			newCustomer.setPhone("13245678910");
			newCustomer.setSex(1);

			customerService.add(newCustomer);

			Customer customerFromDb = customerService.login(newCustomer);
			assertNotNull(customerFromDb);
			showCustomer(customerFromDb);
		}

		@Test
		public void testLoginCustomer() {
			Customer newCustomer = new Customer();
			newCustomer.setAccount("lili");
			newCustomer.setPassword("123456");
			Customer loginCustomer = customerService.login(newCustomer);
			showCustomer(loginCustomer);
		}
	 
	 
	 @Test
	 public void testRemove() {
		 //customer have not id
		 Customer newCustomer = new Customer();
		 newCustomer.setAddress("P1");
		 Customer realCustomer = customerService.searchCustomers(newCustomer).get(0);
		 
		 showCustomer(realCustomer);
		 
		 customerService.remove(realCustomer);
	 }
	 
	 @Test
	 public void testModify() {
		 Customer newCustomer = new Customer();
		 newCustomer.setAccount("zhangadd");
		 Customer realCustomer = customerService.searchCustomers(newCustomer).get(0);
		 
		 showCustomer(realCustomer);
		 realCustomer.setAddress("XXX");
		 customerService.modify(realCustomer);
		 showCustomer(customerService.searchCustomers(realCustomer).get(0));
	 }
	 
	 @Test
	 public void testSearchCustomers() {
		 Customer newCustomer = new Customer();
		 newCustomer.setSex(Constants.USER_FAMALE);
		 
		 List<Customer> realCustomers = customerService.searchCustomers(newCustomer);
		 for (int i = 0; i < realCustomers.size(); i++) {
			showCustomer(realCustomers.get(i));
		}
		 
	 }
	 
	 @Test
	 public void testRemoveCustomerById() {
		 customerService.removeCustomerById((long)12);
		 
	 }
	 
	 
}

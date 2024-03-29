package com.pitaya.bookingnow.service.impl.security;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.entity.security.User_Role_Detail;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.service.security.IUser_Role_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

public class TestUserService {
	private IUserService userService;
	private IUser_Role_DetailService roleDetailService;


	public IUser_Role_DetailService getRoleDetailService() {
		return roleDetailService;
	}

	public void setRoleDetailService(IUser_Role_DetailService roleDetailService) {
		this.roleDetailService = roleDetailService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext(
				"src/applicationContext.xml");
		IUserService userService = aCtx.getBean(IUserService.class);
		assertNotNull(userService);
		this.userService = userService;
		
		IUser_Role_DetailService roleDetailService = aCtx.getBean(IUser_Role_DetailService.class);
		assertNotNull(roleDetailService);
		this.roleDetailService = roleDetailService;
	}

	public void showUser(User user) {
		if (user != null) {
			System.out.println("Id : " + user.getId());
			System.out.println("Account : " + user.getAccount());
			System.out.println("Address : " + user.getAddress());
			System.out.println("Description : " + user.getDescription());
			System.out.println("Email : " + user.getEmail());
			System.out.println("Image_absolute_path : "
					+ user.getImage_absolute_path());
			System.out.println("Image_relative_path : "
					+ user.getImage_relative_path());
			System.out.println("Name : " + user.getName());
			System.out.println("Password : " + user.getPassword());
			System.out.println("Phone : " + user.getPhone());
			System.out.println("Birthday : " + user.getBirthday());
			System.out.println("Department : " + user.getDepartment());
			System.out.println("Image_size : " + user.getImage_size());
			System.out.println("ModifyTime : " + user.getModifyTime());
			System.out.println("Sex : " + user.getSex());
			System.out.println("Sub_system : " + user.getSub_system());
			System.out.println("Enabled : " + user.getEnabled());
			System.out.println();
		}
	}

	@Test
	public void testSearchById() {
		Long userId = 1l;
		showUser(userService.searchUserById(userId));
	}

	@Test
	public void testAddUser() {

		for (int i = 50; i < 60; i++) {
			User newUser = new User();
			if(i < 9) {
				newUser.setAccount("user00" + (i + 1));
			}else if(i<99 && i >= 9) {
				newUser.setAccount("user0" + (i + 1));
			}else{
				newUser.setAccount("user" + (i + 1));
			}
			newUser.setAddress("lsdjf sldf sdlfkj sldkf");
			newUser.setBirthday(new Date().getTime());
			newUser.setDepartment(Constants.USER_DEPARTMENT_MANAGEMENT);
			newUser.setDescription("good man");
			newUser.setEmail("shax@qq.com");
			newUser.setName("shax");
			newUser.setPassword("123456");
			newUser.setPhone("13245678910");
			newUser.setSex(Constants.USER_FAMALE);
			newUser.setSub_system(2);

			MyResult result = userService.add(newUser);
			if (result.isExecuteResult()) {

				System.out.println("add new user successfully!");
				User_Role_Detail roleDetail = new User_Role_Detail();
				roleDetail.setEnabled(true);
				roleDetail.setUser_id(result.getUser().getId());
				if(((i + 1 ) % 10) == 0) {
					roleDetail.setRole_id(10l);
				}else {
					roleDetail.setRole_id((long) ((i + 1 ) % 10));
				}
				roleDetailService.add(roleDetail);
			} else {
				System.out.println("add new user failed!");
				Map<String, String> falseResults = result.getErrorDetails();
				Iterator iter = falseResults.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					System.out.println(entry.getKey().toString() + " : "
							+ entry.getValue().toString());
				}
			}
		}
		
	}

	@Test
	public void testLoginUser() {
		User newUser = new User();
		newUser.setAccount("lili");
		newUser.setPassword("123456");
		MyResult result = userService.login(newUser);
		if (result.isExecuteResult()) {

			System.out.println("add new user successfully!");
		} else {
			System.out.println("add new user failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				System.out.println(entry.getKey().toString() + " : "
						+ entry.getValue().toString());
			}
		}
	}

	@Test
	public void testRemove() {
		// user have not id
		User newUser = new User();
		newUser.setAddress("P1");
		User realUser = userService.searchUsers(newUser).get(0);

		showUser(realUser);

		MyResult result = userService.removeUserById(realUser.getId());
		if (result.isExecuteResult()) {

			System.out.println("add new user successfully!");
		} else {
			System.out.println("add new user failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				System.out.println(entry.getKey().toString() + " : "
						+ entry.getValue().toString());
			}
		}
	}

	@Test
	public void testModify() {
		User newUser = new User();
		newUser.setAccount("zhangadd");
		User realUser = userService.searchUsers(newUser).get(0);

		showUser(realUser);
		realUser.setAddress("XXX");
		userService.modify(realUser);
		showUser(userService.searchUsers(realUser).get(0));
	}

	@Test
	public void testSearchUsers() {
		User newUser = new User();
		newUser.setSex(Constants.USER_FAMALE);

		List<User> realUsers = userService.searchUsers(newUser);
		for (int i = 0; i < realUsers.size(); i++) {
			showUser(realUsers.get(i));
		}

	}

	@Test
	public void testRemoveUserById() {
		userService.removeUserById((long) 10);

	}

}

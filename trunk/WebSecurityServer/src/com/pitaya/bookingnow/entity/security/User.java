package com.pitaya.bookingnow.entity.security;

import java.util.Date;
import java.util.List;

public class User {
    private Long id;

    private String account;

    private String name;

    private String password;

    private String phone;

    private Integer sex;

    private String email;

    private String address;

    private Date birthday;

    private String description;

    private Integer department;

    private Integer sub_system;

    private List<User_Role_Detail> role_details;
    
    private User_Picture picture;
    

    public User_Picture getPicture() {
		return picture;
	}

	public void setPicture(User_Picture picture) {
		this.picture = picture;
	}

	public List<User_Role_Detail> getRole_details() {
		return role_details;
	}

	public void setRole_details(List<User_Role_Detail> role_details) {
		this.role_details = role_details;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getSub_system() {
        return sub_system;
    }

    public void setSub_system(Integer sub_system) {
        this.sub_system = sub_system;
    }
}
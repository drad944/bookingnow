package com.pitaya.bookingnow.entity.security;

import java.util.List;

public class User {
	public User() {
		
	}
	
	public User(String account,String password) {
		this.account = account;
		this.password = password;
	}
	
    private Long id;

    private Long picture_id;

    private String account;

    private String name;

    private String password;

    private String phone;

    private Integer sex;

    private String email;

    private String address;

    private Long birthday;

    private String description;

    private Integer department;

    private Integer sub_system;
    
    private Boolean enabled;
    
    List<User_Role_Detail> role_Details;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<User_Role_Detail> getRole_Details() {
		return role_Details;
	}

	public void setRole_Details(List<User_Role_Detail> role_Details) {
		this.role_Details = role_Details;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(Long picture_id) {
        this.picture_id = picture_id;
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

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
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
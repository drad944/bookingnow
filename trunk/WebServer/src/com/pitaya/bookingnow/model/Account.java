package com.pitaya.bookingnow.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Account implements Serializable{
    public Account() {   
        //super(); 
    }  
    
	public Account(int aid, String name, String password, String role,
			String sex,Timestamp createDateTime, Date birthday) {
		super();
		this.aid = aid;
		this.name = name;
		this.password = password;
		this.role = role;
		this.sex = sex;
		this.createDateTime = createDateTime;
		this.birthday = birthday;
	}
	public Account(String name, String password, String role,
			String sex, Date birthday) {
		super();
		this.name = name;
		this.password = password;
		this.role = role;
		this.sex = sex;
		this.birthday = birthday;
	}
	
	public Account(String name, String password, String role) {
		super();
		this.name = name;
		this.password = password;
		this.role = role;
	}
	
	public Account(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
	public Account(int aid) {
		super();
		this.aid = aid;
	}

	private static final long serialVersionUID = 1L;
	private int aid;
	private String name;
	private String password;
    private String role;
    private String sex;
    private Timestamp createDateTime;
    private Date birthday;

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    public String getName() {   
       return name;   
    }   
    public void setName(String name) {   
       this.name = name;   
    }   
   


	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj != null && obj.getClass() == Account.class){
			if(((Account)obj).getAid() == this.getAid())
				return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return this.aid + "#" + this.name + "#" + this.password + "#" + this.role; 
	}
}

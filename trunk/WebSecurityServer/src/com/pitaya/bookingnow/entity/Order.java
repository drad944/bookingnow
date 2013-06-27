package com.pitaya.bookingnow.entity;

import java.util.Date;
import java.util.List;

public class Order {
    private Long id;

    private Integer status;

    private Date modifyTime;

    private Integer allowance;

    private Double total_price;

    private Double prePay;

    private Date submit_time;

    private Boolean enabled;
    
    private List<Order_Table_Detail> table_details; 
    
    private Order_User_Detail user_detail; 
    
    private Order_Customer_Detail customer_detail;
    
    private List<Order_Food_Detail> food_details;
    
    

    public List<Order_Table_Detail> getTable_details() {
		return table_details;
	}

	public void setTable_details(List<Order_Table_Detail> table_details) {
		this.table_details = table_details;
	}

	public Order_User_Detail getUser_detail() {
		return user_detail;
	}

	public void setUser_detail(Order_User_Detail user_detail) {
		this.user_detail = user_detail;
	}

	public Order_Customer_Detail getCustomer_detail() {
		return customer_detail;
	}

	public void setCustomer_detail(Order_Customer_Detail customer_detail) {
		this.customer_detail = customer_detail;
	}

	public List<Order_Food_Detail> getFood_details() {
		return food_details;
	}

	public void setFood_details(List<Order_Food_Detail> food_details) {
		this.food_details = food_details;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getAllowance() {
        return allowance;
    }

    public void setAllowance(Integer allowance) {
        this.allowance = allowance;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    public Double getPrePay() {
        return prePay;
    }

    public void setPrePay(Double prePay) {
        this.prePay = prePay;
    }

    public Date getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(Date submit_time) {
        this.submit_time = submit_time;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
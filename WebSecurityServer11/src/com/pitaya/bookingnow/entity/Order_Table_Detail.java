package com.pitaya.bookingnow.entity;

public class Order_Table_Detail {
    private Long id;

    private Integer realCustomerCount;

    private Boolean enabled;

    private Long table_id;

    private Long order_id;
    
    private Table table;

    public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRealCustomerCount() {
        return realCustomerCount;
    }

    public void setRealCustomerCount(Integer realCustomerCount) {
        this.realCustomerCount = realCustomerCount;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getTable_id() {
        return table_id;
    }

    public void setTable_id(Long table_id) {
        this.table_id = table_id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }
}
package com.pitaya.bookingnow.entity;

public class Order_Food_Detail_Table {
    private Long id;

    private Integer status;

    private Integer count;

    private Integer preference;
    
    private Long table_id;
    
    private String table_address;

    private Long last_modify_time;

    private Boolean enabled;
    
    private Boolean isFree;

    private Long food_id;

    private Long order_id;
    
    private Food food;

    public Long getTable_id() {
		return table_id;
	}

	public void setTable_id(Long table_id) {
		this.table_id = table_id;
	}

	public String getTable_address() {
		return table_address;
	}

	public void setTable_address(String table_address) {
		this.table_address = table_address;
	}

	public Boolean getIsFree() {
		return isFree;
	}

	public void setIsFree(Boolean isFree) {
		this.isFree = isFree;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPreference() {
        return preference;
    }

    public void setPreference(Integer preference) {
        this.preference = preference;
    }

    public Long getLast_modify_time() {
        return last_modify_time;
    }

    public void setLast_modify_time(Long last_modify_time) {
        this.last_modify_time = last_modify_time;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }
}
package com.pitaya.bookingnow.entity;

public class Table {
    private Long id;

    private Boolean enabled;
    
    private Integer status;

    private Integer minCustomerCount;

    private Integer maxCustomerCount;

    private String address;
    
    private Double indoorPrice;

    public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getMinCustomerCount() {
		return minCustomerCount;
	}

	public void setMinCustomerCount(Integer minCustomerCount) {
		this.minCustomerCount = minCustomerCount;
	}

	public Double getIndoorPrice() {
		return indoorPrice;
	}

	public void setIndoorPrice(Double indoorPrice) {
		this.indoorPrice = indoorPrice;
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


    public Integer getMaxCustomerCount() {
        return maxCustomerCount;
    }

    public void setMaxCustomerCount(Integer maxCustomerCount) {
        this.maxCustomerCount = maxCustomerCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
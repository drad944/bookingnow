package com.pitaya.bookingnow.entity;

public class Table {
    private Long id;

    private Integer status;

    private Integer seatCount;

    private Integer maxCustomerCount;

    private String address;
    
    private Double indoorPrice;

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

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
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
package com.pitaya.bookingnow.model;

public class DiningTable {
    private Integer tid;

    private Integer seatCount;

    private Integer maxCustomerCount;

    private Integer realCustomerCount;

    private Integer tablestatus;

    private Integer place;
    
    public DiningTable() {
    	
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
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

    public Integer getRealCustomerCount() {
        return realCustomerCount;
    }

    public void setRealCustomerCount(Integer realCustomerCount) {
        this.realCustomerCount = realCustomerCount;
    }

    public Integer getTablestatus() {
        return tablestatus;
    }

    public void setTablestatus(Integer tablestatus) {
        this.tablestatus = tablestatus;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }
}
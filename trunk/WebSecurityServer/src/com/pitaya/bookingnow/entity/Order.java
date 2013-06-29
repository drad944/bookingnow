package com.pitaya.bookingnow.entity;

public class Order {
    private Long id;

    private Integer status;

    private Long user_id;

    private Long customer_id;

    private Integer customer_count;

    private Long modifyTime;

    private Double allowance;

    private Double total_price;

    private Double prePay;

    private Long submit_time;

    private Boolean enabled;

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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getCustomer_count() {
        return customer_count;
    }

    public void setCustomer_count(Integer customer_count) {
        this.customer_count = customer_count;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
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

    public Long getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(Long submit_time) {
        this.submit_time = submit_time;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
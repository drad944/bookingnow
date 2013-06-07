package com.pitaya.bookingnow.model;

import java.math.BigDecimal;
import java.util.Date;

public class Food {
    private Integer fid;

    private String name;

    private String material;

    private String version;

    private Date period;

    private Integer chef_id;

    private BigDecimal price;

    private byte[] image;
    
    public Food() {
    	
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Integer getChef_id() {
        return chef_id;
    }

    public void setChef_id(Integer chef_id) {
        this.chef_id = chef_id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
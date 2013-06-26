package com.pitaya.bookingnow.entity;

import java.util.Date;

public class Food_Picture {
    private Long id;

    private String name;

    private Date last_modify_time;

    private Boolean enabled;

    private Long food_id;
    
    private byte[] big_image;

    private byte[] small_image;

    public byte[] getBig_image() {
        return big_image;
    }

    public void setBig_image(byte[] big_image) {
        this.big_image = big_image;
    }

    public byte[] getSmall_image() {
        return small_image;
    }

    public void setSmall_image(byte[] small_image) {
        this.small_image = small_image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLast_modify_time() {
        return last_modify_time;
    }

    public void setLast_modify_time(Date last_modify_time) {
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
}
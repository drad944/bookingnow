package com.pitaya.bookingnow.entity.security;

public class User_Picture {
    private Long id;

    private String name;

    private Long last_modify_time;

    private Boolean enabled;

    private byte[] image;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
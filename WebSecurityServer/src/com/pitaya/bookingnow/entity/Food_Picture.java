package com.pitaya.bookingnow.entity;

public class Food_Picture {
    private Long id;

    private String name;

    private Long version;

    private Boolean enabled;
    
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
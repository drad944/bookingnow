package com.pitaya.bookingnow.entity;

public class Food_PictureWithBLOBs extends Food_Picture {
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
}
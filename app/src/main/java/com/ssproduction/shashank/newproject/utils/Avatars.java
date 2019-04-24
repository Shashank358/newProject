package com.ssproduction.shashank.newproject.utils;

public class Avatars {

    private String avatar_image;
    private String avatar_name;
    private String avatar_thumbImage;

    public Avatars(String avatar_image, String avatar_name, String avatar_thumbImage) {
        this.avatar_image = avatar_image;
        this.avatar_name = avatar_name;
        this.avatar_thumbImage = avatar_thumbImage;
    }

    public Avatars(){

    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public String getAvatar_name() {
        return avatar_name;
    }

    public void setAvatar_name(String avatar_name) {
        this.avatar_name = avatar_name;
    }

    public String getAvatar_thumbImage() {
        return avatar_thumbImage;
    }

    public void setAvatar_thumbImage(String avatar_thumbImage) {
        this.avatar_thumbImage = avatar_thumbImage;
    }
}

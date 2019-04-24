package com.ssproduction.shashank.newproject.utils;

public class follow_request_user {

    public String userName;
    public String avatar_image;
    public String avatar_thumbImage;
    public String id;

    public follow_request_user(String userName, String avatar_image,
                               String avatar_thumbImage, String id) {
        this.userName = userName;
        this.avatar_image = avatar_image;
        this.avatar_thumbImage = avatar_thumbImage;
        this.id = id;
    }

    public follow_request_user(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public String getAvatar_thumbImage() {
        return avatar_thumbImage;
    }

    public void setAvatar_thumbImage(String avatar_thumbImage) {
        this.avatar_thumbImage = avatar_thumbImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

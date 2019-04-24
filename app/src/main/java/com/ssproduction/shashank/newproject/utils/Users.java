package com.ssproduction.shashank.newproject.utils;

public class Users {

    private String firstName;
    private String lastName;
    private String profileDP;
    private String status;
    private String profileThumbDP;
    private String online;
    private String id;
    private String search;
    private String avatar_image;
    private String avatar_name;
    private String avatar_thumbImage;

    public Users(){}

    public Users(String firstName, String lastName, String profileDP, String status,
                 String profileThumbDP, String online, String id, String search,
                 String avatar_image, String avatar_name, String avatar_thumbImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileDP = profileDP;
        this.status = status;
        this.profileThumbDP = profileThumbDP;
        this.online = online;
        this.id = id;
        this.search = search;
        this.avatar_image = avatar_image;
        this.avatar_name = avatar_name;
        this.avatar_thumbImage = avatar_thumbImage;

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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileDP() {
        return profileDP;
    }

    public void setProfileDP(String profileDP) {
        this.profileDP = profileDP;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileThumbDP() {
        return profileThumbDP;
    }

    public void setProfileThumbDP(String profileThumbDP) {
        this.profileThumbDP = profileThumbDP;
    }
}

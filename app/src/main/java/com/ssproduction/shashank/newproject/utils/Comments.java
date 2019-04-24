package com.ssproduction.shashank.newproject.utils;

public class Comments {
    private String comment;
    private String mId;
    private String postId;
    private String profile_image;

    public Comments(){}

    public Comments(String comment, String mId, String postId, String profile_image) {
        this.comment = comment;
        this.mId = mId;
        this.postId = postId;
        this.profile_image = profile_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

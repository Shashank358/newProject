package com.ssproduction.shashank.newproject.utils;

public class Posts {

    private String Id;
    private String posting_time;
    private String userPost;
    private String userThumbPost;
    private String user_image;
    private String user_name;
    private String user_posted_text;
    private int lineCount;
    private String pushId;
    private String post_type;

    public Posts(){}


    public Posts(String id, String posting_time, String userPost,
                 String userThumbPost, String user_image,String pushId,
                 String user_name, String user_posted_text, int lineCount, String post_type) {
        Id = id;
        this.posting_time = posting_time;
        this.userPost = userPost;
        this.userThumbPost = userThumbPost;
        this.user_image = user_image;
        this.user_name = user_name;
        this.user_posted_text = user_posted_text;
        this.lineCount = lineCount;
        this.pushId = pushId;
        this.post_type = post_type;

    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPosting_time() {
        return posting_time;
    }

    public void setPosting_time(String posting_time) {
        this.posting_time = posting_time;
    }

    public String getUserPost() {
        return userPost;
    }

    public void setUserPost(String userPost) {
        this.userPost = userPost;
    }

    public String getUserThumbPost() {
        return userThumbPost;
    }

    public void setUserThumbPost(String userThumbPost) {
        this.userThumbPost = userThumbPost;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_posted_text() {
        return user_posted_text;
    }

    public void setUser_posted_text(String user_posted_text) {
        this.user_posted_text = user_posted_text;
    }
}

package com.ssproduction.shashank.newproject.utils;

public class Follow {

    public String follow_back;
    public String followerId;
    public String followingId;

    public Follow(){}

    public Follow(String follow_back, String followedId, String followingId) {
        this.follow_back = follow_back;
        this.followerId = followedId;
        this.followingId = followingId;
    }

    public String getFollow_back() {
        return follow_back;
    }

    public void setFollow_back(String follow_back) {
        this.follow_back = follow_back;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }
}

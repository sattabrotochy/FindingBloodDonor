package com.okcodex.findingblooddonor.Model;

public class PostList {
    public String date;
    public  String postBloodGroup;
    public  String postId;
    public String postUserAddress;
    public String postUserImage;
    public String postUserNUmber;
    public String postUsername;
    public String time;


    public PostList() {
    }


    public PostList(String date, String postBloodGroup, String postId, String postUserAddress, String postUserImage, String postUserNUmber, String postUsername, String time) {
        this.date = date;
        this.postBloodGroup = postBloodGroup;
        this.postId = postId;
        this.postUserAddress = postUserAddress;
        this.postUserImage = postUserImage;
        this.postUserNUmber = postUserNUmber;
        this.postUsername = postUsername;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getPostBloodGroup() {
        return postBloodGroup;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostUserAddress() {
        return postUserAddress;
    }

    public String getPostUserImage() {
        return postUserImage;
    }

    public String getPostUserNUmber() {
        return postUserNUmber;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public String getTime() {
        return time;
    }
}



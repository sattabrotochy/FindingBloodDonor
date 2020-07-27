package com.okcodex.findingblooddonor.Model;

public class Commentlist {

    String commentText,commentUserImage,commentUserName,time;

    public Commentlist() {
    }

    public Commentlist(String commentText, String commentUserImage, String commentUserName, String time) {
        this.commentText = commentText;
        this.commentUserImage = commentUserImage;
        this.commentUserName = commentUserName;
        this.time = time;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getCommentUserImage() {
        return commentUserImage;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public String getTime() {
        return time;
    }
}

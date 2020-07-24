package com.okcodex.findingblooddonor.Model;

public class UserList {



    String name,address,image,date;

    public UserList() {
    }

    public UserList(String name, String address, String image, String date) {
        this.name = name;
        this.address = address;
        this.image = image;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

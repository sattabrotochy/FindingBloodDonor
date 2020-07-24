package com.okcodex.findingblooddonor.Model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddmemberList {


    String id;
    String name;
    String address;
    String image;
    String date;
    String number;
    String blood;
    String gender;

    public AddmemberList() {
    }

    public AddmemberList(String id, String name, String address, String image, String date, String number, String blood, String gender) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;
        this.date = date;
        this.number = number;
        this.blood = blood;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

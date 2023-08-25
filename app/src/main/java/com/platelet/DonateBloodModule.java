package com.platelet;

import com.google.firebase.database.Exclude;

public class DonateBloodModule {

    private String userName, bagNumber, phoneNumber, address, description, date, email, userId;



    private String nodeKey;

    public DonateBloodModule() {


    }

    public DonateBloodModule(String userName, String bagNumber, String phoneNumber, String address, String description, String date, String email, String userId, String nodeKey) {
        this.userName = userName;
        this.bagNumber = bagNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
        this.date = date;
        this.email = email;
        this.userId = userId;
        this.nodeKey = nodeKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBagNumber() {
        return bagNumber;
    }

    public void setBagNumber(String bagNumber) {
        this.bagNumber = bagNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getNodeKey() {
        return nodeKey;
    }

    @Exclude
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
}

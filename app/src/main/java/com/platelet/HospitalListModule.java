package com.platelet;

import com.google.firebase.database.Exclude;

public class HospitalListModule {


    private String userName, longitude, latitude, email, location;

    private String nodeKey;



    public HospitalListModule() {

    }

    public HospitalListModule(String userName, String longitude, String latitude, String email, String location) {
        this.userName = userName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.email = email;
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

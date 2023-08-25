package com.platelet;

public class DonorModuleCLass {

    private String userName, phoneNumber, numberofDonation, status;

    public DonorModuleCLass() {

    }

    public DonorModuleCLass(String userName, String phoneNumber, String numberofDonation, String location, String status) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.numberofDonation = numberofDonation;
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNumberofDonation() {
        return numberofDonation;
    }

    public void setNumberofDonation(String numberofDonation) {
        this.numberofDonation = numberofDonation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.platelet;

public class DoctorListModule {

    private String userName, information, payment;

    public DoctorListModule() {
    }

    public DoctorListModule(String userName, String information, String payment) {
        this.userName = userName;
        this.information = information;
        this.payment = payment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}

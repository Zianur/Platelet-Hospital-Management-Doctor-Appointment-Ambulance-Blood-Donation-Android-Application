package com.platelet;

import com.google.firebase.database.Exclude;

public class HospitalAppointmentModule {

    private String patientName, patientPhoneNumber, doctorName, deptName, payment, appointmentDate, userId, email;

    private String nodeKey;

    public HospitalAppointmentModule() {


    }

    public HospitalAppointmentModule(String patientName, String patientPhoneNumber, String doctorName, String deptName, String payment, String appointmentDate, String userId, String email, String nodeKey) {
        this.patientName = patientName;
        this.patientPhoneNumber = patientPhoneNumber;
        this.doctorName = doctorName;
        this.deptName = deptName;
        this.payment = payment;
        this.appointmentDate = appointmentDate;
        this.userId = userId;
        this.email = email;
        this.nodeKey = nodeKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
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

package com.platelet;

import com.google.firebase.database.Exclude;

public class UserAppointmentModule {

    private String hospitalName, doctorName, deptName, payment, appointmentDate, hospitalId, uniqueKey;

    private String nodeKey;

    public UserAppointmentModule() {


    }

    public UserAppointmentModule(String hospitalName, String doctorName, String deptName, String payment, String appointmentDate, String hospitalId, String uniqueKey, String nodeKey) {
        this.hospitalName = hospitalName;
        this.doctorName = doctorName;
        this.deptName = deptName;
        this.payment = payment;
        this.appointmentDate = appointmentDate;
        this.hospitalId = hospitalId;
        this.uniqueKey = uniqueKey;
        this.nodeKey = nodeKey;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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

package com.example.myfitnessyard.Models;

public class Users {
    String imageUrl, uName, uNo,uPhno, feeStatus, fee, plan, date;

    public Users() {
    }

    public Users(String imageUrl, String uName, String uNo, String uPhno, String feeStatus, String fee, String plan, String date) {
        this.imageUrl = imageUrl;
        this.uName = uName;
        this.uNo = uNo;
        this.uPhno = uPhno;
        this.feeStatus = feeStatus;
        this.fee = fee;
        this.plan = plan;
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuNo() {
        return uNo;
    }

    public void setuNo(String uNo) {
        this.uNo = uNo;
    }

    public String getuPhno() {
        return uPhno;
    }

    public void setuPhno(String uPhno) {
        this.uPhno = uPhno;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

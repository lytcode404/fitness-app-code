package com.example.myfitnessyard.Models;

public class EditModel {
    String imageUrl, uName, uNo, uPhno, fee, date;

    public EditModel() {
    }

    public EditModel(String imageUrl, String uName, String uNo, String uPhno, String fee, String date) {
        this.imageUrl = imageUrl;
        this.uName = uName;
        this.uNo = uNo;
        this.uPhno = uPhno;
        this.fee = fee;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

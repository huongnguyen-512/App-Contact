package com.example.appcontact.models;

import android.graphics.Bitmap;

public class Directory {
    public int maDonVi;
    public String tenDonVi;
    public String email;
    public String website;
    public String sdt;
    public String diaChi;
    public String maDonViCha;
    public Bitmap logo;

    public Directory(int maDonVi, String tenDonVi, String email, String website, String diaChi,String sdt,  Bitmap logo) {
        this.maDonVi = maDonVi;
        this.tenDonVi = tenDonVi;
        this.email = email;
        this.website = website;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.logo = logo;
    }

    public int getMaDonVi() {
        return maDonVi;
    }

    public void setMaDonVi(int maDonVi) {
        this.maDonVi = maDonVi;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
        this.tenDonVi = tenDonVi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }
}

package com.example.appcontact.models;

import android.graphics.Bitmap;

public class Directory {
    public int maDonVi;
    public String tenDonVi;
    public String email;
    public String website;
    public String sdt;
    public String diaChi;
    public String idCha;
    public Bitmap logo;
    private boolean isHeader;

    private String firstLetter;
    public Directory(int maDonVi, String tenDonVi, String email, String website, String diaChi,String sdt,  Bitmap logo, String idCha,boolean isHeader) {
        this.maDonVi = maDonVi;
        this.tenDonVi = tenDonVi;
        this.email = email;
        this.website = website;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.logo = logo;
        this.idCha = idCha;
        this.isHeader = isHeader;
        if (!isHeader) {
            this.firstLetter = tenDonVi.substring(0, 1).toUpperCase();
        }
    }
    public Directory(String tenDonVi) {
        this.tenDonVi = tenDonVi;
        this.isHeader = true;
        this.firstLetter = tenDonVi.substring(0, 1).toUpperCase();
    }

    public boolean isHeader() {
        return isHeader;
    }

    public int getMaDonVi() {
        return maDonVi;
    }


    public void setMaDonViCha(String maDonViCha) {
        this.idCha = maDonViCha;
    }
    public String getMaDonViCha() {
        return idCha;
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

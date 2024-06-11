package com.example.appcontact.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Employee implements Serializable {
    private int maNV;
    private String hoTen;
    private String chucVu;
    private String email;
    private String sdt;
    private Bitmap anhDaiDien;
    private boolean isHeader;
    private int maDonVi; // Mã đơn vị
    private String tenDonVi; // Tên đơn vị
    private String firstLetter;

    // Constructor thông thường
    public Employee(int maNV, String hoTen, String chucVu, String email, String sdt, Bitmap anhDaiDien, boolean isHeader, String tenDonVi) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.email = email;
        this.sdt = sdt;
        this.anhDaiDien = anhDaiDien;
        this.isHeader = isHeader;
        if (!isHeader) {
            this.firstLetter = hoTen.substring(0, 1).toUpperCase();
        }
        this.maDonVi = maDonVi;
        this.tenDonVi = tenDonVi;
    }

    // Constructor cho header
    public Employee(String hoTen) {
        this.hoTen = hoTen;
        this.isHeader = true;
        this.firstLetter = hoTen.substring(0, 1).toUpperCase();
    }

    public boolean isHeader() {
        return isHeader;
    }

    public Bitmap getAvatar() {
        return anhDaiDien;
    }

    public void setAvatar(Bitmap avatar) {
        this.anhDaiDien = avatar;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
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
}

package com.example.appcontact.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Employee implements Serializable {
    public int maNV;
    public String hoTen;
    public String chucVu;
    public String email;
    public String sdt;
    public Bitmap anhDaiDien;

    public Employee(int maNV, String hoTen, String chucVu, String email, String sdt, Bitmap anhDaiDien) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.email = email;
        this.sdt = sdt;
        this.anhDaiDien = anhDaiDien;
    }


//    public Employee(Bitmap image, String name, String phone) {
//        this.anhDaiDien = image;
//        this.hoTen = name;
//        this.sdt = phone;
//    }

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


}

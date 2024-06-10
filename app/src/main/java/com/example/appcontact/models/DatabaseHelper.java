package com.example.appcontact.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.appcontact.fragment.Employee_show;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "company.db";
    public static final int DATABASE_VERSION = 2;

    // Table DonVi
    public static final String TABLE_DONVI = "DonVi";
    public static final String COLUMN_MA_DONVI = "maDonVi";
    public static final String COLUMN_TEN_DONVI = "tenDonVi";
    public static final String COLUMN_EMAIL_DONVI = "email";
    public static final String COLUMN_WEBSITE_DONVI = "website";
    public static final String COLUMN_LOGO_DONVI = "logo";
    public static final String COLUMN_DIACHI_DONVI = "diaChi";
    public static final String COLUMN_SDT_DONVI = "sdt";

    // Table NhanVien
    public static final String TABLE_NHANVIEN = "NhanVien";
    public static final String COLUMN_MA_NV = "maNV";
    public static final String COLUMN_HOTEN_NV = "hoTen";
    public static final String COLUMN_CHUCVU_NV = "chucVu";
    public static final String COLUMN_EMAIL_NV = "email";
    public static final String COLUMN_SDT_NV = "sdt";
    public static final String COLUMN_ANHDAIDIEN_NV = "anhDaiDien";
    public static final String COLUMN_MA_DONVI_NV = "maDonVi";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableDonVi = "CREATE TABLE " + TABLE_DONVI + " (" +
                COLUMN_MA_DONVI + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEN_DONVI + " TEXT, " +
                COLUMN_EMAIL_DONVI + " TEXT, " +
                COLUMN_WEBSITE_DONVI + " TEXT, " +
                COLUMN_LOGO_DONVI + " BLOB, " +
                COLUMN_DIACHI_DONVI + " TEXT, " +
                COLUMN_SDT_DONVI + " TEXT)";
        db.execSQL(createTableDonVi);

        String createTableNhanVien = "CREATE TABLE " + TABLE_NHANVIEN + " (" +
                COLUMN_MA_NV + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HOTEN_NV + " TEXT, " +
                COLUMN_CHUCVU_NV + " TEXT, " +
                COLUMN_EMAIL_NV + " TEXT, " +
                COLUMN_SDT_NV + " TEXT, " +
                COLUMN_ANHDAIDIEN_NV + " BLOB) " ;
//                +
//                COLUMN_MA_DONVI_NV + " INTEGER)";
//                +
//                "FOREIGN KEY(" + COLUMN_MA_DONVI_NV + ") REFERENCES " + TABLE_DONVI + "(" + COLUMN_MA_DONVI + "))";
        db.execSQL(createTableNhanVien);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHANVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONVI);
        onCreate(db);
    }

}


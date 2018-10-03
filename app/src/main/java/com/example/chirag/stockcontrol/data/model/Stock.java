package com.example.chirag.stockcontrol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "tasks")
public class Stock {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_ID", typeAffinity = ColumnInfo.TEXT)
    private final String mId;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] mImage;

    @NonNull
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    private final String mName;

    @NonNull
    @ColumnInfo(name = "price", typeAffinity = ColumnInfo.INTEGER)
    private final int mPrice;

    @ColumnInfo(name = "quality", typeAffinity = ColumnInfo.INTEGER)
    private final int mQuality;

    @ColumnInfo(name = "date", typeAffinity = ColumnInfo.TEXT)
    private final String mDate;

    @NonNull
    @ColumnInfo(name = "category", typeAffinity = ColumnInfo.INTEGER)
    private final int mCategory;

    @ColumnInfo(name = "location", typeAffinity = ColumnInfo.TEXT)
    private final String mLocation;

    @ColumnInfo(name = "supplier_name", typeAffinity = ColumnInfo.TEXT)
    private final String mSupplierName;

    @ColumnInfo(name = "supplier_contact_number", typeAffinity = ColumnInfo.TEXT)
    private final String mSupplierContactNumber;

    @ColumnInfo(name = "suppleir_email_id", typeAffinity = ColumnInfo.TEXT)
    private final String mSupplierEmailId;

    public Stock(byte[] mImage, @NonNull String mName, @NonNull int mPrice, int mQuality, String mDate, @NonNull int mCategory, String mLocation, String mSupplierName, String mSupplierContactNumber, String mSupplierEmailId) {
        this(UUID.randomUUID().toString(), mImage, mName, mPrice, mQuality, mDate, mCategory, mLocation, mSupplierName, mSupplierContactNumber, mSupplierEmailId);
    }

//    public Task(@NonNull String id, byte[] mImage, @NonNull String mName, @NonNull int mPrice, int mQuality, String mDate, @NonNull int mCategory, String mLocation, String mSupplierName, String mSupplierContactNumber, String mSupplierEmailId) {
//        this(id, mImage, mName, mPrice, mQuality, mDate, mCategory, mLocation, mSupplierName, mSupplierContactNumber, mSupplierEmailId);
//    }

    public Stock(@NonNull String mId, byte[] mImage, @NonNull String mName, @NonNull int mPrice, int mQuality, String mDate, @NonNull int mCategory, String mLocation, String mSupplierName, String mSupplierContactNumber, String mSupplierEmailId) {
        this.mId = mId;
        this.mImage = mImage;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mQuality = mQuality;
        this.mDate = mDate;
        this.mCategory = mCategory;
        this.mLocation = mLocation;
        this.mSupplierName = mSupplierName;
        this.mSupplierContactNumber = mSupplierContactNumber;
        this.mSupplierEmailId = mSupplierEmailId;
    }

    @NonNull
    public String getmId() {
        return mId;
    }

    public byte[] getmImage() {
        return mImage;
    }

    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }

    @NonNull
    public String getmName() {
        return mName;
    }

    @NonNull
    public int getmPrice() {
        return mPrice;
    }

    public int getmQuality() {
        return mQuality;
    }

    public String getmDate() {
        return mDate;
    }

    @NonNull
    public int getmCategory() {
        return mCategory;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmSupplierName() {
        return mSupplierName;
    }

    public String getmSupplierContactNumber() {
        return mSupplierContactNumber;
    }

    public String getmSupplierEmailId() {
        return mSupplierEmailId;
    }
}

package com.example.chirag.stockcontrol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

@Entity(tableName = "tasks")
public class Stock {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_ID")
    private Integer id;

    @Nullable
    @ColumnInfo(name = "image")
    private byte[] image;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "price")
    private Double price;

    @Nullable
    @ColumnInfo(name = "quantity")
    private Integer quantity;

    @Nullable
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "category")
    private Integer category;

    @Nullable
    @ColumnInfo(name = "location")
    private String location;

    @Nullable
    @ColumnInfo(name = "supplier_name")
    private String supplierName;

    @Nullable
    @ColumnInfo(name = "supplier_contact_number")
    private String supplierContactNumber;

    @Nullable
    @ColumnInfo(name = "suppleir_email_id")
    private  String supplierEmailId;

    public Stock(byte[] image, @NonNull String name, @NonNull Double price, Integer quantity, String date, @NonNull Integer category, String location, String supplierName, String supplierContactNumber, String supplierEmailId) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
        this.category = category;
        this.location = location;
        this.supplierName = supplierName;
        this.supplierContactNumber = supplierContactNumber;
        this.supplierEmailId = supplierEmailId;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @Nullable
    public byte[] getImage() {
        return image;
    }

    public void setImage(@Nullable byte[] image) {
        this.image = image;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Double getPrice() {
        return price;
    }

    public void setPrice(@NonNull Double price) {
        this.price = price;
    }

    @Nullable
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@Nullable Integer quantity) {
        this.quantity = quantity;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
    }

    @NonNull
    public Integer getCategory() {
        return category;
    }

    public void setCategory(@NonNull Integer category) {
        this.category = category;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    public void setLocation(@Nullable String location) {
        this.location = location;
    }

    @Nullable
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(@Nullable String supplierName) {
        this.supplierName = supplierName;
    }

    @Nullable
    public String getSupplierContactNumber() {
        return supplierContactNumber;
    }

    public void setSupplierContactNumber(@Nullable String supplierContactNumber) {
        this.supplierContactNumber = supplierContactNumber;
    }

    @Nullable
    public String getSupplierEmailId() {
        return supplierEmailId;
    }

    public void setSupplierEmailId(@Nullable String supplierEmailId) {
        this.supplierEmailId = supplierEmailId;
    }
}

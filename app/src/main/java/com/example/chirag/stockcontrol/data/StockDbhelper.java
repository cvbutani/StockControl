package com.example.chirag.stockcontrol.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import  com.example.chirag.stockcontrol.data.StockContract.StockEntry;

public class StockDbhelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "starlab_data.db";
    public static final int DATABASE_VERSION = 1;

    public StockDbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + StockEntry.TABLE_NAME + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StockEntry.COLUMN_ITEM_IMAGE + " BLOB, " +
                StockEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                StockEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, " +
                StockEntry.COLUMN_ITEM_QUANTITY + " INTEGER DEFAULT 0, " +
                StockEntry.COLUMN_ITEM_DATE + " TEXT, " +
                StockEntry.COLUMN_ITEM_CATEGORY + " INTEGER NOT NULL, " +
                StockEntry.COLUMN_ITEM_LOCATION + " TEXT, " +
                StockEntry.COLUMN_ITEM_SUPPLIER + " TEXT, " +
                StockEntry.COLUMN_ITEM_SUPPLIER_NUMBER + " INTEGER, "+
                StockEntry.COLUMN_ITEM_SUPPLIER_EMAIL + " TEXT);";
        Log.i("STOCK DATABASE HELPER: ", SQL_CREATE_INVENTORY_TABLE);
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

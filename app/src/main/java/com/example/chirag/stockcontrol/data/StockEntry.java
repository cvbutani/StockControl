package com.example.chirag.stockcontrol.data;

import android.provider.BaseColumns;

public final class StockEntry implements BaseColumns {

    public static final String TABLE_NAME = "inventory";
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_ITEM_DATE = "date";
    public static final String COLUMN_ITEM_CATEGORY = "category";
    public static final String COLUMN_ITEM_LOCATION = "location";
    public static final String COLUMN_ITEM_SUPPLIER = "supplier_name";
    public static final String COLUMN_ITEM_SUPPLIER_NUMBER = "supplier_contact_number";
    public static final String COLUMN_ITEM_SUPPLIER_EMAIL = "supplier_Email_Id";
    public static final String COLUMN_ITEM_IMAGE = "image";

    public static final int CATEGORY_UNKNOWN = 0;           //  UNKNOWN
    public static final int CATEGORY_ELECTRONICS = 5;       //  ELECTRONICS
    public static final int CATEGORY_BABY_CLOTHING = 2;     //  BABY'S CLOTHING
    public static final int CATEGORY_BEAUTY_COSMETICS = 3;  //  BEAUTY & COSMETICS
    public static final int CATEGORY_BOOKS = 4;             //  BOOKS
    public static final int CATEGORY_FOOD = 6;              //  FOOD & BEVERAGES
    public static final int CATEGORY_HOUSEWARE = 8;         //  HOUSEWARES / KITCHWARES
    public static final int CATEGORY_HEALTH = 7;            //  HEALTH
    public static final int CATEGORY_ADULT_FASHION = 1;     //  ADULT CLOTHING
    public static final int CATEGORY_GAMES = 9;             //  TOYS & GAMES

    public static boolean isValidCategory(int category) {

        if (category == CATEGORY_UNKNOWN || category == CATEGORY_ELECTRONICS ||
                category == CATEGORY_BABY_CLOTHING || category == CATEGORY_BEAUTY_COSMETICS ||
                category == CATEGORY_BOOKS || category == CATEGORY_FOOD ||
                category == CATEGORY_HOUSEWARE || category == CATEGORY_HEALTH ||
                category == CATEGORY_ADULT_FASHION || category == CATEGORY_GAMES) {
            return true;
        }
        return false;
    }

    public StockEntry() {
    }
}
package com.example.chirag.stockcontrol.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.chirag.stockcontrol.data.model.Stock;

@Database(entities = {Stock.class}, version = 1, exportSchema = false)
public abstract class StockDatabase extends RoomDatabase {

    private static StockDatabase INSTANCE;

    public abstract StockDao stockDao();

    private static final Object sLock = new Object();

    public static StockDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        StockDatabase.class, "Movie.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}

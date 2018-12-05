package com.example.chirag.stockcontrol.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.chirag.stockcontrol.data.entities.StockEntity;

/**
 * Create table using room database persistence and returns single instance
 */
@Database(entities = {StockEntity.class}, version = 1, exportSchema = false)
public abstract class StockDatabase extends RoomDatabase {

    private static StockDatabase INSTANCE;

    // one instance of stockDao
    public abstract StockDao stockDao();

    // Reference object
    private static final Object sLock = new Object();

    /**
     * This method will run when user requests one instance of database
     *
     * @param context activity where instance will be created
     * @return instance of the database
     */
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

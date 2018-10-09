package com.example.chirag.stockcontrol.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

@Dao
public interface StockDao {

    @Query("SELECT * FROM tasks")
    List<Stock> getStocks();

    @Query("SELECT * FROM Tasks WHERE _ID = :taskId")
    Stock getStockById(int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserStock(Stock stock);

    @Query("DELETE FROM tasks WHERE _ID = :taskId")
    int deleteTaskById(int taskId);

    @Query("UPDATE tasks SET quantity= :updatedQuantity WHERE _ID= :taskId")
    void updateStock(int updatedQuantity, int taskId);
}



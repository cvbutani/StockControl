package com.example.chirag.stockcontrol.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StockDao {

    /**
     * Select all stocks from task table.
     *
     * @return all stocks
     */
    @Query("SELECT * FROM tasks")
    List<Stock> getStocks();

    /**
     * Selects only one stock from table.
     *
     * @param taskId stock id to be selected
     * @return selected stock item using @param taskId
     */
    @Query("SELECT * FROM Tasks WHERE _ID = :taskId")
    Stock getStockById(int taskId);

    /**
     * Insert stock in database
     *
     * @param stock to be inserted in database
     */
    @Insert(onConflict = REPLACE)
    void inserStock(Stock stock);

    /**
     * Delete particular stock from table
     *
     * @param taskId stock item to be deleted
     * @return integer if task was deleted
     */
    @Query("DELETE FROM tasks WHERE _ID = :taskId")
    int deleteTaskById(int taskId);

    /**
     * Updates quantity of particular stock
     *
     * @param updatedQuantity new quantity to be updated
     * @param taskId stock item id to update quantity
     */
    @Query("UPDATE tasks SET quantity= :updatedQuantity WHERE _ID= :taskId")
    void updateStock(int updatedQuantity, int taskId);

    /**
     * Update all parameters of stock item
     *
     * @param stock to be replaced with old stock item
     */
    @Update(onConflict = REPLACE)
    void updateStockItem(Stock stock);
}



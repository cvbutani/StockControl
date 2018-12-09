package com.example.chirag.stockcontrol.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.List;
import java.util.Observable;

import io.reactivex.Completable;
import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StockDao {

    /**
     * Select all stocks from task table.
     *
     * @return all stocks
     */
    @Query("SELECT * FROM tasks")
    Maybe<List<StockEntity>> getStocks();

    /**
     * Selects only one stock from table.
     *
     * @param taskId stock id to be selected
     * @return selected stock item using @param taskId
     */
    @Query("SELECT * FROM tasks WHERE _ID = :taskId")
    Maybe<StockEntity> getStockById(int taskId);

    /**
     * Insert stock in database
     *
     * @param stock to be inserted in database
     */
    @Insert(onConflict = REPLACE)
    void inserStock(final StockEntity stock);

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
    void updateStockItem(StockEntity stock);
}



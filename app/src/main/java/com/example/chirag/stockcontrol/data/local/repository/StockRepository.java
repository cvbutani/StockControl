package com.example.chirag.stockcontrol.data.local.repository;

import android.support.annotation.NonNull;

import com.example.chirag.stockcontrol.data.entities.StockEntity;
import com.example.chirag.stockcontrol.data.local.StockDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class StockRepository implements StockRepoCallback {
    private static StockRepository INSTANCE;

    private StockDatabase mStockDatabase;

    private StockRepository (@NonNull StockDatabase database) {
        mStockDatabase = database;
    }

    /**
     * Method will be called to create one single instance of stockService class
     *
     * @param database     database interface
     * @return instance of StockRepository class
     */
    public static StockRepository getInstance(@NonNull StockDatabase database) {
        if (INSTANCE == null) {
            synchronized (StockRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StockRepository(database);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Method will get all stock items from database
     */
    @Override
    public Maybe<List<StockEntity>> getAllStockItems() {
        return mStockDatabase.stockDao().getStocks();
    }

    /**
     * Method will be executed when user requests for particular stock item from database
     *
     * @param stockId item id to pull data from database
     */
    @Override
    public Maybe<StockEntity> getStockItem(final int stockId) {
        return mStockDatabase.stockDao().getStockById(stockId);
    }

    /**
     * Method will be executed when user wants to add data in database
     *
     * @param item stock item detail to be entered in database
     */
    @Override
    public Completable insertStockItem(final StockEntity item) {
        return Completable.fromRunnable(() -> mStockDatabase.stockDao().inserStock(item));
    }

    /**
     * Method will be executed when user wants to delete any stock items
     *
     * @param stockId stock id that user wants to delete
     */
    @Override
    public Completable deleteStockItemData(final int stockId) {
        return Completable.fromRunnable(() -> mStockDatabase.stockDao().deleteTaskById(stockId));

    }

    /**
     * Method will be executed when user wants to update quantity of particular stock item
     *
     * @param updatedQuantity updated/changed quantity
     * @param stockId         stock id that user wants to update quantity
     */
    @Override
    public Completable updateStockItem(final int updatedQuantity, final int stockId) {
        return Completable.fromRunnable(() -> mStockDatabase.stockDao().updateStock(updatedQuantity, stockId));
    }

    /**
     * Update whole stock item that user wants to enter in database
     *
     * @param stock updated stock details
     */
    @Override
    public Completable updateStockItems(final StockEntity stock) {
        return Completable.fromRunnable(() -> mStockDatabase.stockDao().updateStockItem(stock));
    }
}

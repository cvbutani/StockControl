package com.example.chirag.stockcontrol.data.local.repository;

import android.support.annotation.NonNull;

import com.example.chirag.stockcontrol.data.callback.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.entities.StockEntity;
import com.example.chirag.stockcontrol.data.local.StockDao;
import com.example.chirag.stockcontrol.data.local.StockDatabase;
import com.example.chirag.stockcontrol.util.AppExecutors;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableFromCallable;

public class StockRepository implements StockRepoCallback {
    private static StockRepository INSTANCE;

    private StockDatabase mStockDatabase;

    private AppExecutors mAppExecutors;

    private StockRepository(@NonNull AppExecutors appExecutors, @NonNull StockDatabase database) {
        mAppExecutors = appExecutors;
        mStockDatabase = database;
    }

    /**
     * Method will be called to create one single instance of stockService class
     *
     * @param appExecutors to run tasks in background or main thread
     * @param database     database interface
     * @return instance of StockRepository class
     */
    public static StockRepository getInstance(@NonNull AppExecutors appExecutors, @NonNull StockDatabase database) {
        if (INSTANCE == null) {
            synchronized (StockRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StockRepository(appExecutors, database);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Method will get all stock items from database
     *
     */
    @Override
    public Maybe<List<StockEntity>> getAllStockItems() {
        return mStockDatabase.stockDao().getStocks();
    }

    /**
     * Method will be executed when user requests for particular stock item from database
     *
     * @param stockId  item id to pull data from database
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
    public Observable<Boolean> insertStockItem(final StockEntity item) {
        return Observable.fromCallable(new ObservableFromCallable<>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mStockDatabase.stockDao().inserStock(item);
                return true;
            }
        }));
    }

    /**
     * Method will be executed when user wants to delete any stock items
     *
     * @param stockId  stock id that user wants to delete
     * @param callback it will pass integer if successfully deleted
     */
    @Override
    public void deleteStockItemData(final int stockId, final OnTaskCompletion.OnDeleteStockItem callback) {
        Runnable deleteStockRunnable = new Runnable() {
            @Override
            public void run() {
                final int deleted = mStockDatabase.stockDao().deleteTaskById(stockId);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDeleteStockSuccess(deleted);
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(deleteStockRunnable);
    }

    /**
     * Method will be executed when user wants to update quantity of particular stock item
     *
     * @param updatedQuantity updated/changed quantity
     * @param stockId         stock id that user wants to update quantity
     */
    @Override
    public void updateStockItem(final int updatedQuantity, final int stockId) {
        Runnable updateStockRunnable = new Runnable() {
            @Override
            public void run() {
                mStockDatabase.stockDao().updateStock(updatedQuantity, stockId);
            }
        };
        mAppExecutors.getDiskIO().execute(updateStockRunnable);
    }

    /**
     * Update whole stock item that user wants to enter in database
     *
     * @param stock updated stock details
     */
    @Override
    public void updateStockItems(final StockEntity stock) {
        Runnable updatedStockRunnable = new Runnable() {
            @Override
            public void run() {
                mStockDatabase.stockDao().updateStockItem(stock);
            }
        };
        mAppExecutors.getDiskIO().execute(updatedStockRunnable);
    }
}

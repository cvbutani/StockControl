package com.example.chirag.stockcontrol.data.local;

import android.support.annotation.NonNull;

import com.example.chirag.stockcontrol.data.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.StockDataSource;
import com.example.chirag.stockcontrol.data.model.Stock;
import com.example.chirag.stockcontrol.util.AppExecutors;

import java.util.List;

public class  StockService implements StockDataSource {
    private static StockService INSTANCE;

    private StockDao mStockDao;

    private AppExecutors mAppExecutors;

    private StockService(@NonNull AppExecutors appExecutors, @NonNull StockDao stockDao) {
        mAppExecutors = appExecutors;
        mStockDao = stockDao;
    }

    /**
     * Method will be called to create one single instance of stockService class
     *
     * @param appExecutors to run tasks in background or main thread
     * @param stockDao database interface
     * @return instance of StockService class
     */
    public static StockService getInstance(@NonNull AppExecutors appExecutors, @NonNull StockDao stockDao) {
        if (INSTANCE == null) {
            synchronized (StockService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StockService(appExecutors, stockDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Method will get all stock items from database
     *
     * @param callback will get all stock items either success or failure from stockDao
     */
    @Override
    public void getAllStockItems(final OnTaskCompletion.OnGetStockItems callback) {
        Runnable getStockItemsRunnable = new Runnable() {
            @Override
            public void run() {
                final List<Stock> stocks = mStockDao.getStocks();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (stocks.isEmpty()) {
                            callback.stockItemsFailure("NOTHING TO SHOW IN DATABASE");
                        } else {
                            callback.stockItemsSuccess(stocks);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(getStockItemsRunnable);
    }

    /**
     * Method will be executed when user requests for particular stock item from database
     *
     * @param stockId item id to pull data from database
     * @param callback stock item details will be passed on to callback
     */
    @Override
    public void getStockItem(final int stockId, final OnTaskCompletion.OnGetStock callback) {
        Runnable getStockRunnable = new Runnable() {
            @Override
            public void run() {
                final Stock stock = mStockDao.getStockById(stockId);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (stock == null) {
                            callback.getStockFailure("Something went wrong");
                        } else {
                            callback.getStockSuccess(stock);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(getStockRunnable);
    }

    /**
     * Method will be executed when user wants to add data in database
     *
     * @param item stock item detail to be entered in database
     */
    @Override
    public void insertStockItem(final Stock item) {
        Runnable insertStockRunnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.inserStock(item);
            }
        };
        mAppExecutors.getDiskIO().execute(insertStockRunnable);
    }

    /**
     * Method will be executed when user wants to delete any stock items
     *
     * @param stockId stock id that user wants to delete
     * @param callback it will pass integer if successfully deleted
     */
    @Override
    public void deleteStockItemData(final int stockId, final OnTaskCompletion.OnDeleteStockItem callback) {
        Runnable deleteStockRunnable = new Runnable() {
            @Override
            public void run() {
                final int deleted =  mStockDao.deleteTaskById(stockId);
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
     * @param stockId stock id that user wants to update quantity
     */
    @Override
    public void updateStockItem(final int updatedQuantity, final int stockId) {
        Runnable updateStockRunnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.updateStock(updatedQuantity, stockId);
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
    public void updateStockItems(final Stock stock) {
        Runnable updatedStockRunnable = new Runnable() {
            @Override
            public void run() {
                mStockDao.updateStockItem(stock);
            }
        };
        mAppExecutors.getDiskIO().execute(updatedStockRunnable);
    }
}

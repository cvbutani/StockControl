package com.example.chirag.stockcontrol.data.local;

import android.support.annotation.NonNull;
import android.util.Log;

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
}

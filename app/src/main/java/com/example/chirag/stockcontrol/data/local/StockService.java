package com.example.chirag.stockcontrol.data.local;

import android.support.annotation.NonNull;

import com.example.chirag.stockcontrol.data.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.StockDataSource;
import com.example.chirag.stockcontrol.data.model.Stock;
import com.example.chirag.stockcontrol.util.AppExecutors;

import java.util.List;

public class StockService implements StockDataSource {
    private static StockService INSTANCE;

    private StockDao mStockDao;

    private AppExecutors mAppExecutors;

    private StockService(@NonNull AppExecutors appExecutors, @NonNull StockDao stockDao) {
        mAppExecutors = appExecutors;
        mStockDao = stockDao;
    }

    private static StockService getInstance(@NonNull AppExecutors appExecutors, @NonNull StockDao stockDao) {
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
}

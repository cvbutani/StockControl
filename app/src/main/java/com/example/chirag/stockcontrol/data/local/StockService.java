package com.example.chirag.stockcontrol.data.local;

import android.support.annotation.NonNull;

import com.example.chirag.stockcontrol.util.AppExecutors;

public class StockService {
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


}

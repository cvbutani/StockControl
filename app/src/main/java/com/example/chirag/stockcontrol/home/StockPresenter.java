package com.example.chirag.stockcontrol.home;

import android.content.Context;

import com.example.chirag.stockcontrol.data.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.local.StockDatabase;
import com.example.chirag.stockcontrol.data.local.StockService;
import com.example.chirag.stockcontrol.data.model.Stock;
import com.example.chirag.stockcontrol.util.AppExecutors;

import java.util.List;

public class StockPresenter implements StockContract.Presenter {

    private StockContract.View mCallback;

    private StockService mStockService;

    public StockPresenter(Context context) {
        mStockService = StockService.getInstance(new AppExecutors(), StockDatabase.getInstance(context).stockDao());
    }

    @Override
    public void getAllStockitems() {
        mStockService.getAllStockItems(new OnTaskCompletion.OnGetStockItems() {
            @Override
            public void stockItemsSuccess(List<Stock> stock) {
                mCallback.getAllStockItems(stock);
            }

            @Override
            public void stockItemsFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void updateStock(int updateQuantity, int stockId) {
        mStockService.updateStockItem(updateQuantity, stockId);
    }

    @Override
    public void attachView(StockContract.View callback) {
        mCallback = callback;
        getAllStockitems();
    }
}

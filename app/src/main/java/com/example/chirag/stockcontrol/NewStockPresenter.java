package com.example.chirag.stockcontrol;

import android.content.Context;

import com.example.chirag.stockcontrol.data.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.local.StockDatabase;
import com.example.chirag.stockcontrol.data.local.StockService;
import com.example.chirag.stockcontrol.data.model.Stock;
import com.example.chirag.stockcontrol.util.AppExecutors;

import java.util.List;

public class NewStockPresenter implements NewStockContract.Presenter {

    private NewStockContract.View mCallback;

    private StockService mStockService;

    NewStockPresenter(Context context) {
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
    public void getStockData(final int stockId) {
        mStockService.getStockItem(stockId, new OnTaskCompletion.OnGetStock() {
            @Override
            public void getStockSuccess(Stock stock) {
                mCallback.getStock(stockId, stock);
            }

            @Override
            public void getStockFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void insertStock(Stock item) {
        mStockService.insertStockItem(item);
    }

    @Override
    public void attachView(NewStockContract.View view) {
        mCallback = view;
        getAllStockitems();
    }
}
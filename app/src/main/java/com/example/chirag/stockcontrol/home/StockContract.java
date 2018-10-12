package com.example.chirag.stockcontrol.home;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public interface StockContract {

    interface View {

        void getAllStockItems(List<Stock> stockItem);

    }

    interface Presenter {

        void getAllStockitems();

        void updateStock(int updateQuantity, int stockId);

        void attachView(StockContract.View callback);

    }
}

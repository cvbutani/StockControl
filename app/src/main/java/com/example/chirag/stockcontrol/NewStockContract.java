package com.example.chirag.stockcontrol;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public interface NewStockContract {

    interface View {
        void getAllStockItems(List<Stock> stockItem);

        void getStock(int stockId, Stock stock);

        void insertStocks();

    }

    interface Presenter {

        void getAllStockitems();

        void getStockData(int stockId);

        void insertStock(Stock item);

        void attachView(NewStockContract.View view);
    }
}
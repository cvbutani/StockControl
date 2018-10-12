package com.example.chirag.stockcontrol.newstock;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public interface NewStockContract {

    interface View {

        void getStock(Stock stock);

        void insertStocks();

    }

    interface Presenter {

        void getStockData(int stockId);

        void insertStock(Stock item);

        int deleteStockData(int stockId);

        void updateStock(Stock stock);

        void attachView(NewStockContract.View view);
    }
}

package com.example.chirag.stockcontrol.newstock;

import com.example.chirag.stockcontrol.base.MvpView;
import com.example.chirag.stockcontrol.data.entities.StockEntity;

import io.reactivex.disposables.Disposable;

public interface NewStockContract {

    interface View extends MvpView {

        void getStock(StockEntity stock);

        void insertStocks();

        void onDisposable(Disposable disposable);

    }

    interface Presenter {

        void getStockData(int stockId);

        void insertStock(StockEntity item);

        int deleteStockData(int stockId);

        void updateStock(StockEntity stock);

        void attachView(NewStockContract.View view);
    }
}

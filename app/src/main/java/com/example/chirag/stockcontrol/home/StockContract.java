package com.example.chirag.stockcontrol.home;

import com.example.chirag.stockcontrol.base.MvpView;
import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface StockContract {

    interface View extends MvpView {

        void onDisposable(Disposable disposable);

        void getAllStockItems(List<StockEntity> stockItem);
    }

    interface Presenter {

        void getAllStockitems();

        void updateStock(int updateQuantity, int stockId);

        void attachView(StockContract.View callback);

    }
}

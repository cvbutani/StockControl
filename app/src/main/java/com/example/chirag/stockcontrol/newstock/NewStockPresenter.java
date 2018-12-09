package com.example.chirag.stockcontrol.newstock;

import android.content.Context;

import com.example.chirag.stockcontrol.base.BasePresenter;
import com.example.chirag.stockcontrol.data.callback.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.entities.StockEntity;
import com.example.chirag.stockcontrol.data.manager.DataManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewStockPresenter
        extends BasePresenter<NewStockContract.View>
        implements NewStockContract.Presenter {

    private DataManager mDataManager;

    NewStockPresenter(DataManager manager) {
        mDataManager = manager;
    }

    @Override
    public void getStockData(final int stockId) {
        Disposable disposable =
                mDataManager.getStockItem(stockId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stockEntity -> getView().getStock(stockEntity));
        getView().onDisposable(disposable);
    }

    @Override
    public void insertStock(StockEntity item) {
        Disposable disposable =
                mDataManager.insertStockItem(item).toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> getView().insertStocks());
        getView().onDisposable(disposable);
    }

    @Override
    public int deleteStockData(int stockId) {
        Disposable disposable =
                mDataManager.deleteStockItemData(stockId).toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
        getView().onDisposable(disposable);
        int value = 0;
        return value;
    }

    @Override
    public void updateStock(StockEntity stock) {
        Disposable disposable =
                mDataManager.updateStockItems(stock).toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
        getView().onDisposable(disposable);
    }

    @Override
    public NewStockContract.View getView() {
        return super.getView();
    }

    @Override
    public void attachView(NewStockContract.View view) {
        super.attachView(view);
    }
}

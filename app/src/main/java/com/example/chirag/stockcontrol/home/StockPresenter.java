package com.example.chirag.stockcontrol.home;

import com.example.chirag.stockcontrol.base.BasePresenter;
import com.example.chirag.stockcontrol.data.entities.StockEntity;
import com.example.chirag.stockcontrol.data.manager.DataManager;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StockPresenter
        extends BasePresenter<StockContract.View>
        implements StockContract.Presenter {

    private DataManager mdatamanager;

    StockPresenter(DataManager manager) {
        mdatamanager = manager;
    }

    @Override
    public StockContract.View getView() {
        return super.getView();
    }

    @Override
    public void getAllStockitems() {
        mdatamanager.getAllStockItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<StockEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getView().onDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<StockEntity> stockEntities) {
                        getView().getAllStockItems(stockEntities);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void updateStock(int updateQuantity, int stockId) {
        Disposable disposable =
                mdatamanager.updateStockItem(updateQuantity, stockId)
                        .toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
        getView().onDisposable(disposable);
    }

    @Override
    public void attachView(StockContract.View view) {
        super.attachView(view);
        getAllStockitems();
    }
}

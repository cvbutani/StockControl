package com.example.chirag.stockcontrol.data.manager;

import com.example.chirag.stockcontrol.data.callback.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.entities.StockEntity;
import com.example.chirag.stockcontrol.data.local.StockDatabase;
import com.example.chirag.stockcontrol.data.local.repository.StockRepository;
import com.example.chirag.stockcontrol.util.AppExecutors;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class DataManager implements DataContract {

    private static StockDatabase mStockDatabase;
    private StockRepository mStockRepository;

    private DataManager() {
        mStockRepository = StockRepository.getInstance(new AppExecutors(), mStockDatabase);
    }

    public static DataManager getInstance(StockDatabase database) {
        mStockDatabase =database;
        return SingletonHelper.INSTANCE;
    }

    @Override
    public Maybe<List<StockEntity>> getAllStockItems() {
        return mStockRepository.getAllStockItems();
    }

    @Override
    public Maybe<StockEntity> getStockItem(int stockId) {
        return mStockRepository.getStockItem(stockId);
    }

    @Override
    public Completable insertStockItem(StockEntity stock) {
        return mStockRepository.insertStockItem(stock);
    }

    @Override
    public void deleteStockItemData(int stockId, OnTaskCompletion.OnDeleteStockItem callback) {
        mStockRepository.deleteStockItemData(stockId, callback);
    }

    @Override
    public void updateStockItem(int updatedQuantity, int stockId) {
        mStockRepository.updateStockItem(updatedQuantity, stockId);
    }

    @Override
    public void updateStockItems(StockEntity stock) {
        mStockRepository.updateStockItems(stock);
    }

    private static class SingletonHelper {
        private static final DataManager INSTANCE = new DataManager();
    }
}

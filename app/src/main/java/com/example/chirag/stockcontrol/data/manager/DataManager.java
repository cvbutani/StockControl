package com.example.chirag.stockcontrol.data.manager;

import com.example.chirag.stockcontrol.data.entities.StockEntity;
import com.example.chirag.stockcontrol.data.local.StockDatabase;
import com.example.chirag.stockcontrol.data.local.repository.StockRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class DataManager implements DataContract {

    private static StockDatabase mStockDatabase;
    private StockRepository mStockRepository;

    private DataManager() {
        mStockRepository = StockRepository.getInstance(mStockDatabase);
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
    public Completable deleteStockItemData(int stockId) {
        return mStockRepository.deleteStockItemData(stockId);
    }

    @Override
    public Completable updateStockItem(int updatedQuantity, int stockId) {
        return mStockRepository.updateStockItem(updatedQuantity, stockId);
    }

    @Override
    public Completable updateStockItems(StockEntity stock) {
        return mStockRepository.updateStockItems(stock);
    }

    private static class SingletonHelper {
        private static final DataManager INSTANCE = new DataManager();
    }
}

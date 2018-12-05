package com.example.chirag.stockcontrol.data.local.repository;

import com.example.chirag.stockcontrol.data.callback.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface StockRepoCallback {

    Maybe<List<StockEntity>> getAllStockItems();

    Maybe<StockEntity> getStockItem(int stockId);

    Observable<Boolean> insertStockItem(StockEntity stock);

    void deleteStockItemData(int stockId, OnTaskCompletion.OnDeleteStockItem callback);

    void updateStockItem(int updatedQuantity, int stockId);

    void updateStockItems(StockEntity stock);

}

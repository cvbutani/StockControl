package com.example.chirag.stockcontrol.data.manager;

import com.example.chirag.stockcontrol.data.callback.OnTaskCompletion;
import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface DataContract {
    Maybe<List<StockEntity>> getAllStockItems();

    Maybe<StockEntity> getStockItem(int stockId);

    Completable insertStockItem(StockEntity stock);

    void deleteStockItemData(int stockId, OnTaskCompletion.OnDeleteStockItem callback);

    void updateStockItem(int updatedQuantity, int stockId);

    void updateStockItems(StockEntity stock);
}

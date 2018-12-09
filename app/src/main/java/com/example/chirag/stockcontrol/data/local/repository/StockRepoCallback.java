package com.example.chirag.stockcontrol.data.local.repository;

import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface StockRepoCallback {

    Maybe<List<StockEntity>> getAllStockItems();

    Maybe<StockEntity> getStockItem(int stockId);

    Completable insertStockItem(StockEntity stock);

    Completable deleteStockItemData(int stockId);

    Completable updateStockItem(int updatedQuantity, int stockId);

    Completable updateStockItems(StockEntity stock);

}

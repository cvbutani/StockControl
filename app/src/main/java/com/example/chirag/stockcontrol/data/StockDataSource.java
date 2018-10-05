package com.example.chirag.stockcontrol.data;

import com.example.chirag.stockcontrol.data.model.Stock;

public interface StockDataSource {

    void getAllStockItems(OnTaskCompletion.OnGetStockItems callback);

    void getStockItem(int stockId, OnTaskCompletion.OnGetStock callback);

    void insertStockItem(Stock stock);

    void deleteStockItemData(int stockId, OnTaskCompletion.OnDeleteStockItem callback);

}

package com.example.chirag.stockcontrol.data;

import com.example.chirag.stockcontrol.data.model.Stock;

public interface StockDataSource {

    void getAllStockItems(OnTaskCompletion.OnGetStockItems callback);

    void insertStockItem(Stock stock, OnTaskCompletion.OnInsertStockItem callback);

}

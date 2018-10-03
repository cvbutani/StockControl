package com.example.chirag.stockcontrol.data;

public interface StockDataSource {

    void getAllStockItems(OnTaskCompletion.OnGetStockItems callback);

}

package com.example.chirag.stockcontrol.data;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public interface OnTaskCompletion {
    interface OnGetStockItems {
        void stockItemsSuccess (List<Stock> stock);
        void stockItemsFailure (String errorMessage);
    }
}

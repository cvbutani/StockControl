package com.example.chirag.stockcontrol.data;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public interface OnTaskCompletion {

    interface OnGetStockItems {
        void stockItemsSuccess (List<Stock> stock);
        void stockItemsFailure (String errorMessage);
    }

    interface OnGetStock {
        void getStockSuccess (Stock stock);
        void getStockFailure (String errorMessage);
    }

    interface OnDeleteStockItem {
        void onDeleteStockSuccess(int response);
    }

    interface OnUpdateStockItem {
        void onUpdateStockSuccess(Stock stock);
    }
}

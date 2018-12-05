package com.example.chirag.stockcontrol.data.callback;

import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.List;

public interface OnTaskCompletion {

    interface OnGetStockItems {
        void stockItemsSuccess (List<StockEntity> stock);
        void stockItemsFailure (String errorMessage);
    }

    interface OnGetStock {
        void getStockSuccess (StockEntity stock);
        void getStockFailure (String errorMessage);
    }

    interface OnDeleteStockItem {
        void onDeleteStockSuccess(int response);
    }

    interface OnUpdateStockItem {
        void onUpdateStockSuccess(StockEntity stock);
    }
}

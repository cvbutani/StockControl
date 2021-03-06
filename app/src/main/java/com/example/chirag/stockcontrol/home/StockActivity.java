package com.example.chirag.stockcontrol.home;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;

import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.chirag.stockcontrol.R;
import com.example.chirag.stockcontrol.StockAdapter;
import com.example.chirag.stockcontrol.data.model.Stock;
import com.example.chirag.stockcontrol.newstock.NewStockActivity;

public class StockActivity extends AppCompatActivity implements StockContract.View, StockAdapter.OnItemClickListener {

    StockPresenter mStockPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockActivity.this, NewStockActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStockPresenter = new StockPresenter(this);
        mStockPresenter.attachView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
//                insertData();
                return true;
            case R.id.action_delete_all_entries:
                showDeleteAllStockItemsDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllStockItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  User clicked the "Delete" button, so delete the pet.
//                deleteAllStockItems();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  User clicked the "Cancel" button, so dismiss the dialog
                //  and continue editing the stock item.
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        //  Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    private void deleteAllStockItems() {
//
//        int rowsDeleted = getContentResolver().delete(StockEntry.CONTENT_URI, null, null);
//
//        //  Show a toast message depending on whether or not the delete was successful.
//        if (rowsDeleted == 0) {
//            //  If no rows were deleted, then there was an error with the delete.
//            Toast.makeText(this, getString(R.string.delete_stock_failure),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            //  The delete was successful and we can display a toast.
//            Toast.makeText(this, getString(R.string.delete_stock_success),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void getAllStockItems(List<Stock> stockItem) {
        RecyclerView rvStocks = findViewById(R.id.my_recycler_view);
        StockAdapter adapter = new StockAdapter(stockItem, this, this);
        rvStocks.setLayoutManager(new LinearLayoutManager(this));
        rvStocks.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(StockActivity.this, NewStockActivity.class);
        intent.putExtra("POSITION", position + 1);
        Log.i("STOCK ACTIVITY ", " Position - - - " + position);
        startActivity(intent);
    }

    @Override
    public void onQuantityUpdate(int quantity, int stockId) {
        mStockPresenter.updateStock(quantity, stockId);
    }
}

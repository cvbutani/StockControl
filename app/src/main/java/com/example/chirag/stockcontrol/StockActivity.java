package com.example.chirag.stockcontrol;

import android.app.LoaderManager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;

import android.database.Cursor;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chirag.stockcontrol.data.StockContract.StockEntry;
import com.example.chirag.stockcontrol.data.StockDbhelper;

public class StockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private StockCursorAdapter mStockCursorAdapter;
    private StockDbhelper mStockDbHelper;
    public static final int STOCK_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockActivity.this, NewStockActivity.class);
                startActivity(intent);
            }
        });

        final ListView stockListView = (ListView) findViewById(R.id.dialog_listview);

//        View emptyView = findViewById(R.id.empty_view);


        mStockCursorAdapter = new StockCursorAdapter(this, null);
        stockListView.setAdapter(mStockCursorAdapter);

        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StockActivity.this, NewStockActivity.class);
                Uri currentItemUri = ContentUris.withAppendedId(StockEntry.CONTENT_URI, id);
                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(STOCK_LOADER, null, this);
    }

    private void insertData() {
        mStockDbHelper = new StockDbhelper(this);
        ContentValues values = new ContentValues();

        values.put(StockEntry.COLUMN_ITEM_NAME, "iPad");
        values.put(StockEntry.COLUMN_ITEM_PRICE, 299);
        values.put(StockEntry.COLUMN_ITEM_QUANTITY, 20);
        values.put(StockEntry.COLUMN_ITEM_DATE, "20/05/2018");
        values.put(StockEntry.COLUMN_ITEM_CATEGORY, StockEntry.CATEGORY_ELECTRONICS);
        values.put(StockEntry.COLUMN_ITEM_LOCATION, "L05A1");
        values.put(StockEntry.COLUMN_ITEM_SUPPLIER, "Apple inc.");

        getContentResolver().insert(StockEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            case R.id.action_delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                StockEntry._ID,
                StockEntry.COLUMN_ITEM_IMAGE,
                StockEntry.COLUMN_ITEM_NAME,
                StockEntry.COLUMN_ITEM_PRICE,
                StockEntry.COLUMN_ITEM_QUANTITY,
                StockEntry.COLUMN_ITEM_DATE,
                StockEntry.COLUMN_ITEM_CATEGORY,
                StockEntry.COLUMN_ITEM_LOCATION,
                StockEntry.COLUMN_ITEM_SUPPLIER,
                StockEntry.COLUMN_ITEM_SUPPLIER_NUMBER,
                StockEntry.COLUMN_ITEM_SUPPLIER_EMAIL
        };
        return new CursorLoader(this, StockEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mStockCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mStockCursorAdapter.swapCursor(null);
    }
}

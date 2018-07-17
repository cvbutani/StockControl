package com.example.chirag.stockcontrol;

import android.app.AlertDialog;
import android.app.LoaderManager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chirag.stockcontrol.data.StockContract.StockEntry;
import com.example.chirag.stockcontrol.data.StockDbhelper;

public class StockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private StockCursorAdapter mStockCursorAdapter;
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

    @Override
    protected void onResume() {
        getLoaderManager().restartLoader(STOCK_LOADER,null,this);
        super.onResume();
    }

    private void insertData() {
        StockDbhelper mStockDbHelper = new StockDbhelper(this);
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
                showDeleteAllStockItemsDialog();
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

    private void showDeleteAllStockItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  User clicked the "Delete" button, so delete the pet.
                deleteAllStockItems();
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

    private void deleteAllStockItems() {

        int rowsDeleted = getContentResolver().delete(StockEntry.CONTENT_URI, null, null);

        //  Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            //  If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.delete_stock_failure),
                    Toast.LENGTH_SHORT).show();
        } else {
            //  The delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.delete_stock_success),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

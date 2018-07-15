package com.example.chirag.stockcontrol;

import android.app.DatePickerDialog;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;

import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

import android.net.Uri;

import android.os.Bundle;
import android.os.StrictMode;

import android.provider.MediaStore;

import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chirag.stockcontrol.data.ImageCapture;
import com.example.chirag.stockcontrol.data.StockContract.StockEntry;

import java.util.Calendar;

/**
 * StockControl
 * Created by Chirag on 06/07/18.
 */

public class NewStockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private RelativeLayout rlCamera;
    private TextView tvDatePicker;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private Spinner mCategorySpinner;
    private EditText mLocationEditText;
    private EditText mSupplierEditText;
    private ImageView mImageView;

    public static final int STOCK_LOADER = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    int mCategory = 0;
    Uri currentSelectedItemUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Intent intent = getIntent();
        currentSelectedItemUri = intent.getData();

        if (currentSelectedItemUri != null) {
            setTitle("Edit Stock");
            getLoaderManager().initLoader(STOCK_LOADER, null, this);
        } else {
            setTitle("Add New Stock Item");
        }

        findAllViews();

        rlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewStockActivity.this,
                        android.R.style.Theme_Holo_Dialog_NoActionBar,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                tvDatePicker.setText(date);
            }
        };

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setupSpinner();
    }

    private void findAllViews() {
        tvDatePicker = findViewById(R.id.edit_item_date);
        rlCamera = findViewById(R.id.camera);
        mNameEditText = findViewById(R.id.edit_item_name);
        mPriceEditText = findViewById(R.id.edit_item_price);
        mQuantityEditText = findViewById(R.id.edit_item_quantity);
        mLocationEditText = findViewById(R.id.edit_item_location);
        mSupplierEditText = findViewById(R.id.edit_item_supplier);
        mCategorySpinner = findViewById(R.id.spinner_category);
        mImageView = findViewById(R.id.inventory_image);
    }

    private void setupSpinner() {
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_adult_clothing))) {
                        mCategory = StockEntry.CATEGORY_ADULT_FASHION;
                    } else if (selection.equals(getString(R.string.category_baby_clothing))) {
                        mCategory = StockEntry.CATEGORY_BABY_CLOTHING;
                    } else if (selection.equals(getString(R.string.category_beauty))) {
                        mCategory = StockEntry.CATEGORY_BEAUTY_COSMETICS;
                    } else if (selection.equals(getString(R.string.category_books))) {
                        mCategory = StockEntry.CATEGORY_BOOKS;
                    } else if (selection.equals(getString(R.string.category_electronics))) {
                        mCategory = StockEntry.CATEGORY_ELECTRONICS;
                    } else if (selection.equals(getString(R.string.category_food))) {
                        mCategory = StockEntry.CATEGORY_FOOD;
                    } else if (selection.equals(getString(R.string.category_health))) {
                        mCategory = StockEntry.CATEGORY_HEALTH;
                    } else if (selection.equals(getString(R.string.category_housewares))) {
                        mCategory = StockEntry.CATEGORY_HOUSEWARE;
                    } else if (selection.equals(getString(R.string.category_toys_game))) {
                        mCategory = StockEntry.CATEGORY_GAMES;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveStockItem() {
        double price = 0;
        int quantity = 0;
        String name = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String date = tvDatePicker.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();
        String supplier = mSupplierEditText.getText().toString().trim();

        Bitmap bitmapImage = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }

        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }

        byte[] mByteImage = ImageCapture.getBytes(bitmapImage);
        ContentValues values = new ContentValues();

        values.put(StockEntry.COLUMN_ITEM_IMAGE, mByteImage);
        values.put(StockEntry.COLUMN_ITEM_NAME, name);
        values.put(StockEntry.COLUMN_ITEM_PRICE, price);
        values.put(StockEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(StockEntry.COLUMN_ITEM_CATEGORY, mCategory);
        values.put(StockEntry.COLUMN_ITEM_DATE, date);
        values.put(StockEntry.COLUMN_ITEM_LOCATION, location);
        values.put(StockEntry.COLUMN_ITEM_SUPPLIER, supplier);


        Uri newUri = getContentResolver().insert(StockEntry.CONTENT_URI, values);

        if (newUri != null) {
            Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Can't add this Item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveStockItem();
                finish();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(NewStockActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
        }
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
                StockEntry.COLUMN_ITEM_SUPPLIER
        };
        return new CursorLoader(this, currentSelectedItemUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            int imageColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_IMAGE);
            int nameColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_QUANTITY);
            int dateColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_DATE);
            int categoryColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_CATEGORY);
            int locationColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_LOCATION);
            int supplierColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_SUPPLIER);

            byte[] bitmap = data.getBlob(imageColumnIndex);

            Bitmap img = ImageCapture.getImage(bitmap);
            String name = data.getString(nameColumnIndex);
            int price = data.getInt(priceColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            String location = data.getString(locationColumnIndex);
            String supplier = data.getString(supplierColumnIndex);
            String date = data.getString(dateColumnIndex);
            int category = data.getInt(categoryColumnIndex);

            mNameEditText.setText(name);
            mImageView.setImageBitmap(img);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            tvDatePicker.setText(date);
            mLocationEditText.setText(location);
            mSupplierEditText.setText(supplier);
            switch (category) {
                case StockEntry.CATEGORY_ADULT_FASHION:
                    mCategorySpinner.setSelection(1);
                    break;
                case StockEntry.CATEGORY_BABY_CLOTHING:
                    mCategorySpinner.setSelection(2);
                    break;
                case StockEntry.CATEGORY_BEAUTY_COSMETICS:
                    mCategorySpinner.setSelection(3);
                    break;
                case StockEntry.CATEGORY_BOOKS:
                    mCategorySpinner.setSelection(4);
                    break;
                case StockEntry.CATEGORY_ELECTRONICS:
                    mCategorySpinner.setSelection(5);
                    break;
                case StockEntry.CATEGORY_FOOD:
                    mCategorySpinner.setSelection(6);
                    break;
                case StockEntry.CATEGORY_HEALTH:
                    mCategorySpinner.setSelection(7);
                    break;
                case StockEntry.CATEGORY_HOUSEWARE:
                    mCategorySpinner.setSelection(8);
                    break;
                case StockEntry.CATEGORY_GAMES:
                    mCategorySpinner.setSelection(9);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

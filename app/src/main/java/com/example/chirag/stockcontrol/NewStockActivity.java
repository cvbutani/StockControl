package com.example.chirag.stockcontrol;

import android.app.DatePickerDialog;

import android.content.ContentValues;
import android.content.Intent;

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

public class NewStockActivity extends AppCompatActivity {

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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri fileUri;
    static final int MEDIA_TYPE_IMAGE = 1;
    private int mCategory = 0;
    private String imageFilePath;
    Bitmap image = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

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
        String date = tvDatePicker.toString();
        String location = mLocationEditText.toString().trim();
        String supplier = mSupplierEditText.toString().trim();

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
}

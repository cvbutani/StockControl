package com.example.chirag.stockcontrol;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chirag.stockcontrol.data.StockContract.StockEntry;

import java.util.Calendar;

public class NewStockActivity extends AppCompatActivity {

    private RelativeLayout rlCamera;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView tvDatePicker;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private Spinner mCategorySpinner;
    private EditText mLocationEditText;
    private EditText mSupplierEditText;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private int mCategory = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        findAllViews();

        rlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
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
                String date = dayOfMonth + "/" + month + "/" + year ;
                tvDatePicker.setText(date);
            }
        };

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
    }

    private void setupSpinner(){
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals(getString(R.string.category_adult_clothing))) {
                        mCategory = StockEntry.CATEGORY_ADULT_FASHION;
                    } else if (selection.equals(getString(R.string.category_baby_clothing))) {
                        mCategory = StockEntry.CATEGORY_BABY_CLOTHING;
                    }else if (selection.equals(getString(R.string.category_beauty))) {
                        mCategory = StockEntry.CATEGORY_BEAUTY_COSMETICS;
                    }else if (selection.equals(getString(R.string.category_books))) {
                        mCategory = StockEntry.CATEGORY_BOOKS;
                    }else if (selection.equals(getString(R.string.category_electronics))) {
                        mCategory = StockEntry.CATEGORY_ELECTRONICS;
                    }else if (selection.equals(getString(R.string.category_food))) {
                        mCategory = StockEntry.CATEGORY_FOOD;
                    }else if (selection.equals(getString(R.string.category_health))) {
                        mCategory = StockEntry.CATEGORY_HEALTH;
                    }else if (selection.equals(getString(R.string.category_housewares))) {
                        mCategory = StockEntry.CATEGORY_HOUSEWARE;
                    }else if (selection.equals(getString(R.string.category_toys_game))) {
                        mCategory = StockEntry.CATEGORY_GAMES   ;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(NewStockActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

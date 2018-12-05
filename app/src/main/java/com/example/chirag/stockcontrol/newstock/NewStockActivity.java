package com.example.chirag.stockcontrol.newstock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;

import android.os.Bundle;

import android.provider.ContactsContract;
import android.provider.MediaStore;

import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chirag.stockcontrol.R;
import com.example.chirag.stockcontrol.base.BaseActivity;
import com.example.chirag.stockcontrol.data.constant.AppConfig;
import com.example.chirag.stockcontrol.data.manager.DataManager;
import com.example.chirag.stockcontrol.util.ImageCapture;
import com.example.chirag.stockcontrol.data.entities.StockEntity;

import java.util.Calendar;

import io.reactivex.disposables.Disposable;

/**
 * StockControl
 * Created by Chirag on 06/07/18.
 */

public class NewStockActivity extends BaseActivity implements NewStockContract.View {

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String TAG = "NEW STOCK ACTIVITY --- ";
    private TextView cameraTextView;
    private LinearLayout mPlaceOrderLayout;

    private TextView tvDatePicker;

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mLocationEditText;
    private EditText mSupplierEditText;
    private EditText mSupplierContactNumberEditText;
    private EditText mSupplerEmailId;
    private EditText mOrderQuantity;

    private Spinner mCategorySpinner;

    private ImageView mImageView;
    private Button mDeleteButton;
    private Button mPlaceOrder;
    private Button mSaveItem;
    private StockEntity stock;
    private int position;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean mStockHasChanged = false;
    private int mCategory = 0;

    private NewStockPresenter mStockPresenter;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mStockHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        findAllViewsAndAttachListener();

        Intent intent = getIntent();
        if (intent.hasExtra("POSITION")) {
            position = intent.getExtras().getInt("POSITION");
        }

        // Attach presenter with Activity
        mStockPresenter = new NewStockPresenter(DataManager.getInstance());
        mStockPresenter.attachView(this);
        // if position clicked was 0 then it will set title as Edit StockEntity otherwise
        if (position != 0) {
            mDeleteButton.setVisibility(View.VISIBLE);
            mPlaceOrderLayout.setVisibility(View.VISIBLE);
            setTitle("Edit StockEntity");
            mStockPresenter.getStockData(position);
            // It will set title as Add New StockEntity Item
        } else {
            mPlaceOrderLayout.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.GONE);
            setTitle("Add New StockEntity Item");
        }

        // camera to take picture of stock item
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        cameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
        // date picker dialog
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewStockActivity.this,
                        android.R.style.Theme_DeviceDefault_Light,
                        mDateSetListener,
                        year, month, day);
                dialog.show();
            }
        });
        // date display as String
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                tvDatePicker.setText(date);
            }
        };
        setupSpinner();
        // delete stock
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
        // place order if stock is low
        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString().trim();
                String quantity = mQuantityEditText.getText().toString().trim();
                String supplierEmail = mSupplerEmailId.getText().toString().trim();
                String quantityOrder = mOrderQuantity.getText().toString().trim();

                String messageText = "Hello,\n" + "We Would like to order " + name + ". \n" +
                        "Currently, we have " + quantity + " quantity in stock.\n" +
                        "Can you please send us " + quantityOrder + " ?\n" +
                        "Please provide us tracking number asap.\n" +
                        "Thank you";
                if (!TextUtils.isEmpty(supplierEmail) && !TextUtils.isEmpty(quantityOrder)) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + supplierEmail));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Place Order for " + name);
                    intent.putExtra(Intent.EXTRA_TEXT, messageText);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(NewStockActivity.this, "Supplier not added for this item", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // save stock in database
        mSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Save stock item to database
                insertStocks();
                //  Exit activity
                finish();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void findAllViewsAndAttachListener() {
        tvDatePicker = findViewById(R.id.edit_item_date);
        cameraTextView = findViewById(R.id.edit_button);
        mNameEditText = findViewById(R.id.edit_item_name);
        mPriceEditText = findViewById(R.id.edit_item_price);
        mQuantityEditText = findViewById(R.id.edit_item_quantity);
        mLocationEditText = findViewById(R.id.edit_item_location);
        mSupplierEditText = findViewById(R.id.edit_item_supplier);
        mCategorySpinner = findViewById(R.id.spinner_category);
        mImageView = findViewById(R.id.inventory_image);
        mSupplierContactNumberEditText = findViewById(R.id.edit_item_supplier_contact_number);
        mSupplerEmailId = findViewById(R.id.edit_item_supplier_email_id);
        mDeleteButton = findViewById(R.id.delete_item);
        mPlaceOrder = findViewById(R.id.place_order);
        mSaveItem = findViewById(R.id.save_item);
        mPlaceOrderLayout = findViewById(R.id.place_order_layout);
        mOrderQuantity = findViewById(R.id.place_order_quantity);

        tvDatePicker.setOnTouchListener(mTouchListener);
        cameraTextView.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mLocationEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mSupplierContactNumberEditText.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);
        mImageView.setOnTouchListener(mTouchListener);
        mSupplerEmailId.setOnTouchListener(mTouchListener);
        mOrderQuantity.setOnTouchListener(mTouchListener);
    }

    /**
     * Setup Spinner
     */
    private void setupSpinner() {
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_adult_clothing))) {
                        mCategory = AppConfig.CATEGORY_ADULT_FASHION;
                    } else if (selection.equals(getString(R.string.category_baby_clothing))) {
                        mCategory = AppConfig.CATEGORY_BABY_CLOTHING;
                    } else if (selection.equals(getString(R.string.category_beauty))) {
                        mCategory = AppConfig.CATEGORY_BEAUTY_COSMETICS;
                    } else if (selection.equals(getString(R.string.category_books))) {
                        mCategory = AppConfig.CATEGORY_BOOKS;
                    } else if (selection.equals(getString(R.string.category_electronics))) {
                        mCategory = AppConfig.CATEGORY_ELECTRONICS;
                    } else if (selection.equals(getString(R.string.category_food))) {
                        mCategory = AppConfig.CATEGORY_FOOD;
                    } else if (selection.equals(getString(R.string.category_health))) {
                        mCategory = AppConfig.CATEGORY_HEALTH;
                    } else if (selection.equals(getString(R.string.category_housewares))) {
                        mCategory = AppConfig.CATEGORY_HOUSEWARE;
                    } else if (selection.equals(getString(R.string.category_toys_game))) {
                        mCategory = AppConfig.CATEGORY_GAMES;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = AppConfig.CATEGORY_UNKNOWN;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                //  If stock hasn't changed, continue with navigation up to parent activity
                //  which is th {@link StockActivity}
                if (!mStockHasChanged) {
                    NavUtils.navigateUpFromSameTask(NewStockActivity.this);
                    return true;
                }

                //  Otherwise if there are unsaved changes, setup a dialog to warn the user
                //  Create a click listener to handle the user confirming that
                //  changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //  User clicked Discard button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(NewStockActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
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

    /**
     * This method is called when back button is pressed.
     */
    @Override
    public void onBackPressed() {
        //  If the stock item hasn't changed, continue with handling back button press
        if (!mStockHasChanged) {
            super.onBackPressed();
            return;
        }

        //  Otherwise if there are unsaved changes, setup a dialog to warn the user.
        //  Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        //  Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when the user
     *                                   confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        //  Create an AlertDialog.Builder and set the message and click listeners
        //  for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  User clicked the "Keep editing" button, so dismiss the dialog
                //  and continue editing the stock item
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        //  Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        //  Create an AlertDialog.Builder and set the message and click listeners
        //  fir the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  User clicked the "Delete" button, so delete the pet.
                int response = mStockPresenter.deleteStockData(position);
                //  Show a toast message depending on whether or not the delete was successful.
                deleteStock(response);
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

    /**
     * Get stock item from database and display on UI.
     *
     * @param stock item to be displayed on UI.
     */
    @Override
    public void getStock(StockEntity stock) {
        int quantity = stock.getQuantity();

        mNameEditText.setText(stock.getName());
        mImageView.setImageBitmap(ImageCapture.getImage(stock.getImage()));
        mPriceEditText.setText(Double.toString(stock.getPrice()));
        mQuantityEditText.setText(Integer.toString(quantity));
        tvDatePicker.setText(stock.getDate());
        mLocationEditText.setText(stock.getLocation());
        mSupplierEditText.setText(stock.getSupplierName());
        mSupplierContactNumberEditText.setText(stock.getSupplierContactNumber());
        mSupplerEmailId.setText(stock.getSupplierEmailId());

        if (quantity == 0) {
            mPlaceOrderLayout.setVisibility(View.VISIBLE);
        } else if (quantity <= 15 && quantity > 0) {
            mPlaceOrderLayout.setVisibility(View.VISIBLE);
        } else {
            mPlaceOrderLayout.setVisibility(View.GONE);
        }

        int category = stock.getCategory();
        switch (category) {
            case AppConfig.CATEGORY_ADULT_FASHION:
                mCategorySpinner.setSelection(1);
                break;
            case AppConfig.CATEGORY_BABY_CLOTHING:
                mCategorySpinner.setSelection(2);
                break;
            case AppConfig.CATEGORY_BEAUTY_COSMETICS:
                mCategorySpinner.setSelection(3);
                break;
            case AppConfig.CATEGORY_BOOKS:
                mCategorySpinner.setSelection(4);
                break;
            case AppConfig.CATEGORY_ELECTRONICS:
                mCategorySpinner.setSelection(5);
                break;
            case AppConfig.CATEGORY_FOOD:
                mCategorySpinner.setSelection(6);
                break;
            case AppConfig.CATEGORY_HEALTH:
                mCategorySpinner.setSelection(7);
                break;
            case AppConfig.CATEGORY_HOUSEWARE:
                mCategorySpinner.setSelection(8);
                break;
            case AppConfig.CATEGORY_GAMES:
                mCategorySpinner.setSelection(9);
                break;
        }
    }

    /**
     * Get User input from editor and save stock item in database.
     */
    @Override
    public void insertStocks() {
        //  Read from input fields
        //  Use trim to eliminate leading or trailing white space
        double price = 0;
        int quantity = 0;
        String name = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String date = tvDatePicker.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();
        String supplier = mSupplierEditText.getText().toString().trim();
        String supplierContactNumber = mSupplierContactNumberEditText.getText().toString().trim();
        String supplierEmailId = mSupplerEmailId.getText().toString().trim();

        Bitmap bitmapImage = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        byte[] mByteImage = ImageCapture.getBytes(bitmapImage);

        //  Check if this is supposed to be a new stock item
        //  and check if all the fields in the editor are blank.
        if (position == 0 &&
                TextUtils.isEmpty(name) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(date) &&
                TextUtils.isEmpty(location) && TextUtils.isEmpty(supplier) &&
                TextUtils.isEmpty(supplierContactNumber) && TextUtils.isEmpty(supplierEmailId)) {
            //  Since no fields were modified, we can return early without creating a new StockEntity.
            //  No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        //  If price is not provided by the user, don't try to parse the string into an
        //  integer value. Use 0 by default.
        if (!TextUtils.isEmpty(priceString)) {
            price = Double.parseDouble(priceString);
        }

        //  If quantity is not provided by the user, don't try to parse the string into an
        //  integer value. Use 0 by default.
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        stock = new StockEntity(mByteImage, name, price, quantity, date, mCategory, location, supplier, supplierContactNumber, supplierEmailId);

        if (position == 0) {
            mStockPresenter.insertStock(stock);
            //  Show a toast message depending on whether or not the insertion was successful.
            Toast.makeText(getApplicationContext(), getString(R.string.new_stock_added_success),
                    Toast.LENGTH_SHORT).show();
        } else {
            stock.setId(position);
            mStockPresenter.updateStock(stock);
            // Update was successful and we can display a toast.
            Toast.makeText(getApplicationContext(), getString(R.string.update_stock_success),
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDisposable(Disposable disposable) {
        baseCompositeDisposable.add(disposable);
    }

    /**
     * Delete StockEntity item Toast depending on response
     *
     * @param response is 0 error if 1 success
     */
    public void deleteStock(int response) {
        //  Show a toast message depending on whether or not the delete was successful.
        if (response == 0) {
            //  If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.delete_stock_failure),
                    Toast.LENGTH_SHORT).show();
        } else {
            //  The delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.delete_stock_success),
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}

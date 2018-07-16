package com.example.chirag.stockcontrol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

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
import android.view.MotionEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chirag.stockcontrol.data.ImageCapture;
import com.example.chirag.stockcontrol.data.StockContract.StockEntry;

import org.w3c.dom.Text;

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
    private EditText mSupplierContactNumberEditText;
    private EditText mSupplerEmailId;
    private Button mDeleteButton;
    private Button mPlaceOrder;

    private boolean mStockHasChanged = false;

    public static final int STOCK_LOADER = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    int mCategory = 0;
    Uri mCurrentSelectedStockItem;

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

        Intent intent = getIntent();
        mCurrentSelectedStockItem = intent.getData();

        if (mCurrentSelectedStockItem != null) {
            setTitle("Edit Stock");
            getLoaderManager().initLoader(STOCK_LOADER, null, this);
        } else {
            invalidateOptionsMenu();
            setTitle("Add New Stock Item");
        }

        findAllViewsAndAttachListener();

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
                        android.R.style.Theme_DeviceDefault_Light,
                        mDateSetListener,
                        year, month, day);
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
        setupSpinner();

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString().trim();
                String quantity = mQuantityEditText.getText().toString().trim();
                String supplierEmail = mSupplerEmailId.getText().toString().trim();
                String messageText = "Hello,\n" + "We Would like to order " + name + ". \n" +
                        "Currently, we have " + quantity + " quantity in stock.\n" +
                        "Can you please send us 100 ?\n" +
                        "Please provide us tracking number asap.\n" +
                        "Thank you";

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + supplierEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Place Order for " + name);
                intent.putExtra(Intent.EXTRA_TEXT, messageText);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void findAllViewsAndAttachListener() {
        tvDatePicker = findViewById(R.id.edit_item_date);
        rlCamera = findViewById(R.id.camera);
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

        tvDatePicker.setOnTouchListener(mTouchListener);
        rlCamera.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mLocationEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mSupplierContactNumberEditText.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);
        mImageView.setOnTouchListener(mTouchListener);
        mSupplerEmailId.setOnTouchListener(mTouchListener);
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
                mCategory = StockEntry.CATEGORY_UNKNOWN;
            }
        });
    }

    /**
     * Get User input from editor and save stock item in database.
     */
    private void saveStockItem() {
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
        if (mCurrentSelectedStockItem == null &&
                TextUtils.isEmpty(name) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(date) &&
                TextUtils.isEmpty(location) && TextUtils.isEmpty(supplier) &&
                TextUtils.isEmpty(supplierContactNumber) && TextUtils.isEmpty(supplierEmailId)) {
            //  Since no fields were modified, we can return early without creating a new Stock.
            //  No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        //  If price is not provided by the user, don't try to parse the string into an
        //  integer value. Use 0 by default.
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }

        //  If quantity is not provided by the user, don't try to parse the string into an
        //  integer value. Use 0 by default.
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }

        //  Create a ContentValues object where column names are the keys,
        //  and stock attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(StockEntry.COLUMN_ITEM_IMAGE, mByteImage);
        values.put(StockEntry.COLUMN_ITEM_NAME, name);
        values.put(StockEntry.COLUMN_ITEM_PRICE, price);
        values.put(StockEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(StockEntry.COLUMN_ITEM_CATEGORY, mCategory);
        values.put(StockEntry.COLUMN_ITEM_DATE, date);
        values.put(StockEntry.COLUMN_ITEM_LOCATION, location);
        values.put(StockEntry.COLUMN_ITEM_SUPPLIER, supplier);
        values.put(StockEntry.COLUMN_ITEM_SUPPLIER_NUMBER, supplierContactNumber);
        values.put(StockEntry.COLUMN_ITEM_SUPPLIER_EMAIL, supplierEmailId);

        //  Determine if this is a new or existing stock item by checking if
        //  mCurrentSelectedStockItem is null or not
        if (mCurrentSelectedStockItem == null) {
            //  This is a NEW stock item, so insert a new stock item into the provider,
            //  returning the content URI for the new stock item.
            Uri newUri = getContentResolver().insert(StockEntry.CONTENT_URI, values);
            //  Show a toast message depending on whether or not the insertion was successful.
            if (newUri != null) {
                //  If the new content Uri is not null, then we can show successful toast.
                Toast.makeText(getApplicationContext(), getString(R.string.new_stock_added_success),
                        Toast.LENGTH_SHORT).show();
            } else {
                //  otherwise, we can display an error with the insertion.
                Toast.makeText(getApplicationContext(), getString(R.string.new_stock_added_failure),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            //  This is an EXISTING stock items. Update the pet with content URI: mCurrentSelectedStockItems
            //  and pass in the new ContentValues. Pass in null for the selection and selection args
            //  because mCurrentSelectedStockItem will already identify the correct row in the database
            //  that we want to modify.
            int rowsChanged = getContentResolver().update(mCurrentSelectedStockItem, values, null, null);
            //  Shows a toast message depending on whether or not update was successful.
            if (rowsChanged == 0) {
                //  If no rows were affected, then there was an error with the update.
                Toast.makeText(getApplicationContext(), getString(R.string.update_stock_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                //  Otherwise, the update was successful and we can display a toast.
                Toast.makeText(getApplicationContext(), getString(R.string.update_stock_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //  If this is a new stock item, hide the "Delete" menu item.
        if (mCurrentSelectedStockItem == null) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            //  Respond to a click on the "Save" menu option.
            case R.id.action_save:
                //  Save stock item to database
                saveStockItem();
                //  Exit activity
                finish();
                return true;
            //  Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                //Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
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
        return new CursorLoader(this, mCurrentSelectedStockItem, projection, null, null, null);
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
            int supplierNumberColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_SUPPLIER_NUMBER);
            int supplierEmailIdColumnIndex = data.getColumnIndex(StockEntry.COLUMN_ITEM_SUPPLIER_EMAIL);

            byte[] bitmap = data.getBlob(imageColumnIndex);

            Bitmap img = ImageCapture.getImage(bitmap);
            String name = data.getString(nameColumnIndex);
            int price = data.getInt(priceColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            String location = data.getString(locationColumnIndex);
            String supplier = data.getString(supplierColumnIndex);
            String date = data.getString(dateColumnIndex);
            int category = data.getInt(categoryColumnIndex);
            String supplierNumber = data.getString(supplierNumberColumnIndex);
            String supplierEmail = data.getString(supplierEmailIdColumnIndex);

            mNameEditText.setText(name);
            mImageView.setImageBitmap(img);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            tvDatePicker.setText(date);
            mLocationEditText.setText(location);
            mSupplierEditText.setText(supplier);
            mSupplierContactNumberEditText.setText(supplierNumber);
            mSupplerEmailId.setText(supplierEmail);

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
                deleteStockItem();
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

    private void deleteStockItem() {
        //  Only perform the delete tif this is an existing stock item.
        if (mCurrentSelectedStockItem != null) {
            //  Call the ContentResolver to delete the pet at the given content URI.
            //  Pass in null for the selection and selection args because the mCurrentSelectedStockItem
            //  content URI already identifies the stock item that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentSelectedStockItem, null, null);

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
        finish();
    }
}

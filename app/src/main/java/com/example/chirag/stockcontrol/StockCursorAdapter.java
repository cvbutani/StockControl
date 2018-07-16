package com.example.chirag.stockcontrol;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chirag.stockcontrol.data.ImageCapture;
import com.example.chirag.stockcontrol.data.StockContract;
import com.example.chirag.stockcontrol.data.StockContract.StockEntry;

public class StockCursorAdapter extends CursorAdapter {

    StockCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvQuantity = (TextView) view.findViewById(R.id.quantity);
        TextView tvCategory = view.findViewById(R.id.category);
        ImageView ivProductImage = view.findViewById(R.id.image);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_NAME));
        String quantity = cursor.getString(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_PRICE));
        int category = cursor.getInt(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_CATEGORY));

        if (cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_IMAGE) == 1) {
            byte[] bitmap = cursor.getBlob(cursor.getColumnIndex(StockEntry.COLUMN_ITEM_IMAGE));
            Bitmap imageBitmap = ImageCapture.getImage(bitmap);
            ivProductImage.setImageBitmap(imageBitmap);
        } else {
            ivProductImage.setImageResource(R.drawable.inventory);
        }

        switch (category) {
            case StockEntry.CATEGORY_ADULT_FASHION:
                tvCategory.setText(R.string.category_adult_clothing);
                break;
            case StockEntry.CATEGORY_BABY_CLOTHING:
                tvCategory.setText(R.string.category_baby_clothing);
                break;
            case StockEntry.CATEGORY_BEAUTY_COSMETICS:
                tvCategory.setText(R.string.category_beauty);
                break;
            case StockEntry.CATEGORY_BOOKS:
                tvCategory.setText(R.string.category_books);
                break;
            case StockEntry.CATEGORY_ELECTRONICS:
                tvCategory.setText(R.string.category_electronics);
                break;
            case StockEntry.CATEGORY_FOOD:
                tvCategory.setText(R.string.category_food);
                break;
            case StockEntry.CATEGORY_HEALTH:
                tvCategory.setText(R.string.category_health);
                break;
            case StockEntry.CATEGORY_HOUSEWARE:
                tvCategory.setText(R.string.category_housewares);
                break;
            case StockEntry.CATEGORY_GAMES:
                tvCategory.setText(R.string.category_toys_game);
                break;
            default:
                tvCategory.setText(R.string.category_unknown);
                break;
        }

        tvName.setText(name);
        tvQuantity.setText(quantity);

    }
}

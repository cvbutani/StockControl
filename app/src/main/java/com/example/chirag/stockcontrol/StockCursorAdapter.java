package com.example.chirag.stockcontrol;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.chirag.stockcontrol.data.StockContract.StockEntry;

public class StockCursorAdapter extends CursorAdapter {

    public StockCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvBreed = (TextView) view.findViewById(R.id.summary);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_NAME));
        String breed = cursor.getString(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_PRICE));

        tvName.setText(name);
        tvBreed.setText(breed);
    }
}

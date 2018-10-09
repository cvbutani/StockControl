package com.example.chirag.stockcontrol;

import android.content.Context;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private List<Stock> mStock;

    public StockAdapter(List<Stock> stocks) {
        mStock = stocks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View stockView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(stockView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Stock stock = mStock.get(i);

        TextView textView = viewHolder.tvName;
        textView.setText(stock.getName());
    }

    @Override
    public int getItemCount() {
        return mStock.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvPrice;
        TextView tvCategory;
        TextView tvQuantity;
        ImageView ivButton;

        ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvPrice = (TextView) itemView.findViewById(R.id.price);
            tvCategory = (TextView) itemView.findViewById(R.id.category);
            tvQuantity = (TextView) itemView.findViewById(R.id.quantity);
            ivButton = (ImageView) itemView.findViewById(R.id.image);
        }


        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
//
//    private int updatedQuantity = 0;
//
//    StockCursorAdapter(Context context, Cursor cursor) {
//        super(context, cursor, 0);
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
//    }
//
//    @Override
//    public void bindView(View newView, final Context context, Cursor cursor) {
//        TextView tvName = (TextView) newView.findViewById(R.id.name);
//        TextView tvPrice = (TextView) newView.findViewById(R.id.price);
//        TextView tvCategory = (TextView) newView.findViewById(R.id.category);
//        final TextView tvQuantity = (TextView) newView.findViewById(R.id.quantity);
//        ImageView ivButton = (ImageView) newView.findViewById(R.id.image);
//
//        String name = cursor.getString(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_NAME));
//        String price = cursor.getString(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_PRICE));
//        int category = cursor.getInt(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_CATEGORY));
//        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(StockEntry.COLUMN_ITEM_QUANTITY));
//        long stockId = cursor.getLong(cursor.getColumnIndexOrThrow(StockEntry._ID));
//
//        updatedQuantity = quantity;
//        final Uri selectedStockItem = ContentUris.withAppendedId(StockEntry.CONTENT_URI, stockId);
//
//        ivButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (quantity > 0) {
//                    updatedQuantity = quantity - 1;
//                    Toast.makeText(context, "Quantity Updated", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Insufficiant Quantity", Toast.LENGTH_SHORT).show();
//                }
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put(StockEntry.COLUMN_ITEM_QUANTITY, updatedQuantity);
//                        context.getContentResolver().update(selectedStockItem, contentValues, null, null);
//                    }
//                });
//            }
//        });
//
//        if (updatedQuantity == 0) {
//            tvQuantity.setTextColor(Color.RED);
//        } else if (updatedQuantity <= 15 && updatedQuantity >=1) {
//            tvQuantity.setTextColor(Color.parseColor("#FFBF00"));
//        } else {
//            tvQuantity.setTextColor(Color.BLACK);
//        }
//
//        tvName.setText(name);
//        tvPrice.setText("CAD $ " + price);
//        tvQuantity.setText("IN STOCK: " + String.valueOf(updatedQuantity));
//
//        switch (category) {
//            case StockEntry.CATEGORY_ADULT_FASHION:
//                tvCategory.setText(R.string.category_adult_clothing);
//                break;
//            case StockEntry.CATEGORY_BABY_CLOTHING:
//                tvCategory.setText(R.string.category_baby_clothing);
//                break;
//            case StockEntry.CATEGORY_BEAUTY_COSMETICS:
//                tvCategory.setText(R.string.category_beauty);
//                break;
//            case StockEntry.CATEGORY_BOOKS:
//                tvCategory.setText(R.string.category_books);
//                break;
//            case StockEntry.CATEGORY_ELECTRONICS:
//                tvCategory.setText(R.string.category_electronics);
//                break;
//            case StockEntry.CATEGORY_FOOD:
//                tvCategory.setText(R.string.category_food);
//                break;
//            case StockEntry.CATEGORY_HEALTH:
//                tvCategory.setText(R.string.category_health);
//                break;
//            case StockEntry.CATEGORY_HOUSEWARE:
//                tvCategory.setText(R.string.category_housewares);
//                break;
//            case StockEntry.CATEGORY_GAMES:
//                tvCategory.setText(R.string.category_toys_game);
//                break;
//            default:
//                tvCategory.setText(R.string.category_unknown);
//                break;
//        }
//    }
}

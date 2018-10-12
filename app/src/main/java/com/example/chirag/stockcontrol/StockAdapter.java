package com.example.chirag.stockcontrol;

import android.content.Context;

import android.graphics.Color;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chirag.stockcontrol.data.StockEntry;
import com.example.chirag.stockcontrol.data.model.Stock;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private List<Stock> mStock;

    private OnItemClickListener mOnClickListener;

    private int updatedQuantity = 0;

    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onQuantityUpdate(int quantity, int stockId);
    }

    public StockAdapter(List<Stock> stocks, OnItemClickListener listener, Context context) {
        mStock = stocks;
        mOnClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View stockView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(stockView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Stock stock = mStock.get(i);

        String name = stock.getName();
        String price = String.valueOf(stock.getPrice());
        int category = stock.getCategory();
        final int quantity = stock.getQuantity();
        final int stockId = stock.getId();

        updatedQuantity = quantity;

        viewHolder.ivButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    updatedQuantity = quantity - 1;
                    Toast.makeText(mContext, "Quantity Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Insufficiant Quantity", Toast.LENGTH_SHORT).show();
                }
                mOnClickListener.onQuantityUpdate(updatedQuantity, stockId);
            }
        });

        if (updatedQuantity == 0) {
            viewHolder.tvQuantity.setTextColor(Color.RED);
        } else if (updatedQuantity <= 15 && updatedQuantity >= 1) {
            viewHolder.tvQuantity.setTextColor(Color.parseColor("#FFBF00"));
        } else {
            viewHolder.tvQuantity.setTextColor(Color.BLACK);
        }

        viewHolder.tvName.setText(name);
        viewHolder.tvPrice.setText("CAD $ " + price);
        viewHolder.tvQuantity.setText("IN STOCK: " + String.valueOf(updatedQuantity));

        switch (category) {
            case StockEntry.CATEGORY_ADULT_FASHION:
                viewHolder.tvCategory.setText(R.string.category_adult_clothing);
                break;
            case StockEntry.CATEGORY_BABY_CLOTHING:
                viewHolder.tvCategory.setText(R.string.category_baby_clothing);
                break;
            case StockEntry.CATEGORY_BEAUTY_COSMETICS:
                viewHolder.tvCategory.setText(R.string.category_beauty);
                break;
            case StockEntry.CATEGORY_BOOKS:
                viewHolder.tvCategory.setText(R.string.category_books);
                break;
            case StockEntry.CATEGORY_ELECTRONICS:
                viewHolder.tvCategory.setText(R.string.category_electronics);
                break;
            case StockEntry.CATEGORY_FOOD:
                viewHolder.tvCategory.setText(R.string.category_food);
                break;
            case StockEntry.CATEGORY_HEALTH:
                viewHolder.tvCategory.setText(R.string.category_health);
                break;
            case StockEntry.CATEGORY_HOUSEWARE:
                viewHolder.tvCategory.setText(R.string.category_housewares);
                break;
            case StockEntry.CATEGORY_GAMES:
                viewHolder.tvCategory.setText(R.string.category_toys_game);
                break;
            default:
                viewHolder.tvCategory.setText(R.string.category_unknown);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mStock.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
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

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClick(view, clickedPosition);
        }
    }
}

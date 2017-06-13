package com.haitr.doge.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitr.doge.Object.Dish;
import com.haitr.doge.Object.Food;
import com.haitr.doge.Object.Vendor;
import com.haitr.doge.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by haitr on 6/5/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int CART = 0;
    private final int HISTORY = 1;
    private final Context context;
    private final ArrayList<Dish> list;
    private BtnClickListener mClickListener = null;

    public interface BtnClickListener {
        public abstract void onBtnClick(int id);
    }

    public CartAdapter(Context context, ArrayList<Dish> list, BtnClickListener mClickListener) {
        this.context = context;
        this.list = list;
        this.mClickListener = mClickListener;
    }

    private class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price;
        IconTextView delete;

        CartViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            price = (TextView) view.findViewById(R.id.price);
            delete = (IconTextView) view.findViewById(R.id.delete);
        }
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price;
        IconTextView delete;

        HistoryViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            price = (TextView) view.findViewById(R.id.price);
            delete = (IconTextView) view.findViewById(R.id.delete);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(list.get(position).getDate(), "")) {
            return CART;
        } else if (!Objects.equals(list.get(position).getDate(), "")) {
            return HISTORY;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case CART:
                View v1 = inflater.inflate(R.layout.order, parent, false);
                viewHolder = new CartAdapter.CartViewHolder(v1);
                break;
            case HISTORY:
                View v2 = inflater.inflate(R.layout.order, parent, false);
                viewHolder = new CartAdapter.HistoryViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("onBind" ,holder.getItemViewType()+"" );
        switch (holder.getItemViewType()) {
            case CART:
                CartAdapter.CartViewHolder vh1 = (CartAdapter.CartViewHolder) holder;
                configureCartViewHolder(vh1, position);
                break;
            case HISTORY:
                CartAdapter.HistoryViewHolder vh2 = (CartAdapter.HistoryViewHolder) holder;
                configureHistoryViewHolder(vh2, position);
                break;
        }
    }

    private void configureCartViewHolder(final CartAdapter.CartViewHolder holder, final int position) {
        Iconify.with(new FontAwesomeModule());
        final Dish dish = list.get(position);
//        if(getItemCount() == 0){
//            noContent.setVisibility(View.VISIBLE);
//        }else {
        holder.delete.setText("{fa-trash-o}");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null){
                    mClickListener.onBtnClick(dish.getDishId());
                }
            }
        });

        holder.name.setText(dish.getDishName());
        holder.quantity.setText(dish.getQuantity() + "");
        holder.price.setText(dish.getPrice() + "đ");
//        }
    }


    private void configureHistoryViewHolder(final CartAdapter.HistoryViewHolder holder, final int position) {
        final Dish dish = list.get(position);
//        if(getItemCount() == 0){
//            noContent.setVisibility(View.VISIBLE);
//        }else {
        holder.delete.setText(dish.getDate());
        holder.delete.setTextColor(Color.BLACK);
        holder.delete.setPadding(0,0,0,0);

        holder.name.setText(dish.getDishName());
        holder.quantity.setText(dish.getQuantity() + "");
        holder.price.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Iconify.with(new FontAwesomeModule());
//        final Dish dish = list.get(position);
//        if(convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order, parent, false);
//        }
//        TextView name = (TextView) convertView.findViewById(R.id.name);
//        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
//        TextView price = (TextView) convertView.findViewById(R.id.price);
//        IconTextView delete = (IconTextView) convertView.findViewById(R.id.delete);
//
//        delete.setText("{fa-trash-o}");
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mClickListener != null){
//                    mClickListener.onBtnClick(dish.getDishId());
//                }
//            }
//        });
//
//        name.setText(dish.getDishName());
//        quantity.setText(dish.getQuantity() + "");
//        price.setText(dish.getPrice() + "đ");
//
//        return convertView;
//    }
//
//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
}
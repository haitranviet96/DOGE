package com.haitr.doge.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitr.doge.Object.Food;
import com.haitr.doge.Fragment.HomeFragment;
import com.haitr.doge.R;
import com.haitr.doge.Object.Vendor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haitr on 5/19/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int FOOD = 0;
    private final int VENDOR = 1;
    private List<Object> list;
    private Context context;
    public ListViewAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
    }

    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<Object> getList() {
        return list;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description,price;
        public ImageView thumbnail;
        public EditText quantity;
        public Button order_button, increase_button, decrease_button;

        public FoodViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.imageView);
            quantity = (EditText) view.findViewById(R.id.quantity);
            order_button = (Button) view.findViewById(R.id.order_button);
            increase_button = (Button) view.findViewById(R.id.increase_button);
            decrease_button = (Button) view.findViewById(R.id.decrease_button);

            price.setVisibility(View.GONE);
            quantity.setVisibility(View.GONE);
            order_button.setVisibility(View.GONE);
            increase_button.setVisibility(View.GONE);
            decrease_button.setVisibility(View.GONE);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    private class VendorViewHolder extends RecyclerView.ViewHolder {
        TextView title, description,quality,price;
        ImageView thumbnail;
        EditText quantity;
        Button order_button, increase_button, decrease_button;

        VendorViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.imageView);
            quality = (TextView) view.findViewById(R.id.quality);
            quantity = (EditText) view.findViewById(R.id.quantity);
            order_button = (Button) view.findViewById(R.id.order_button);
            increase_button = (Button) view.findViewById(R.id.increase_button);
            decrease_button = (Button) view.findViewById(R.id.decrease_button);

            price.setVisibility(View.GONE);
            quantity.setVisibility(View.GONE);
            order_button.setVisibility(View.GONE);
            increase_button.setVisibility(View.GONE);
            decrease_button.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Food) {
            return FOOD;
        } else if (list.get(position) instanceof Vendor) {
            return VENDOR;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        //View itemView = LayoutInflater.from(parent.getContext())
                //.inflate(R.layout.food_item, parent, false);
        //return new FoodViewHolder(itemView);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case FOOD:
                View v1 = inflater.inflate(R.layout.food_item, parent, false);
                viewHolder = new FoodViewHolder(v1);
                break;
            case VENDOR:
                View v2 = inflater.inflate(R.layout.vendor_item, parent, false);
                viewHolder = new VendorViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FOOD:
                FoodViewHolder vh1 = (FoodViewHolder) holder;
                configureFoodViewHolder(vh1, position);
                break;
            case VENDOR:
                VendorViewHolder vh2 = (VendorViewHolder) holder;
                configureVendorViewHolder(vh2, position);
                break;
        }
    }

    private void configureFoodViewHolder(final FoodViewHolder holder, int position) {
        Food food = (Food) list.get(position);
        if(getItemCount() == 0){
            HomeFragment.noContent.setVisibility(View.VISIBLE);
        }else {
            holder.title.setText(food.getName());
            holder.description.setText(food.getDescription());
            //holder.quantity.setText(food.g() + " ");

            // loading album cover using Glide library
            // Glide.with(context).load(food.getImage()).into(holder.thumbnail);
        }
    }

    private void configureVendorViewHolder(final VendorViewHolder holder, int position) {
        Vendor vendor = (Vendor) list.get(position);
        if(getItemCount() == 0){
            HomeFragment.noContent1.setVisibility(View.VISIBLE);
        }else {
            holder.title.setText(vendor.getName());
            holder.description.setText(vendor.getAddress());
            holder.quality.setText(vendor.getQuality() + "");

            // loading album cover using Glide library
            // Glide.with(context).load(food.getImage()).into(holder.thumbnail);

            Picasso.with(context)
                    .load(vendor.getImage())
                    .error(R.drawable.shop_placeholder)
                    .placeholder(R.drawable.shop_placeholder)
                    .resize(200,200)
                    .centerCrop()
                    .into(holder.thumbnail);
        }
    }

//    /**
//     * Showing popup menu when tapping on 3 dots
//     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
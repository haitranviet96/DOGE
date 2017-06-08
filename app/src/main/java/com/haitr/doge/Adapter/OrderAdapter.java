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

import com.haitr.doge.Object.Dish;
import com.haitr.doge.Object.Food;
import com.haitr.doge.R;
import com.haitr.doge.Object.Vendor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.haitr.doge.R.id.quantity;

/**
 * Created by haitr on 5/26/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int FOOD = 0;
    private final int VENDOR = 1;
    private List<Object> list;
    private Context context;
    private TextView noContent;
    private BtnClickListener mClickListener = null;

    public OrderAdapter(List<Object> list, Context context, TextView noContent, BtnClickListener mClickListener) {
        this.noContent = noContent;
        this.list = list;
        this.context = context;
        this.mClickListener = mClickListener;
    }

    public interface BtnClickListener {
        public abstract void onBtnClick(int id, Dish dish);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
    }

    public List<Object> getList() {
        return list;
    }

    private class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, price;
        ImageView thumbnail;
        EditText quantity;
        Button order_button, increase_button, decrease_button;

        FoodViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.imageView);
            quantity = (EditText) view.findViewById(R.id.quantity);
            order_button = (Button) view.findViewById(R.id.order_button);
            increase_button = (Button) view.findViewById(R.id.increase_button);
            decrease_button = (Button) view.findViewById(R.id.decrease_button);
        }
    }

    private class VendorViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, quality, price;
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
                viewHolder = new OrderAdapter.FoodViewHolder(v1);
                break;
            case VENDOR:
                View v2 = inflater.inflate(R.layout.vendor_item, parent, false);
                viewHolder = new OrderAdapter.VendorViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FOOD:
                OrderAdapter.FoodViewHolder vh1 = (OrderAdapter.FoodViewHolder) holder;
                configureFoodViewHolder(vh1, position);
                break;
            case VENDOR:
                OrderAdapter.VendorViewHolder vh2 = (OrderAdapter.VendorViewHolder) holder;
                configureVendorViewHolder(vh2, position);
                break;
        }
    }

    private void configureFoodViewHolder(final OrderAdapter.FoodViewHolder holder, final int position) {
        final Food food = (Food) list.get(position);
        if(getItemCount() == 0){
            noContent.setVisibility(View.VISIBLE);
        }else {
            holder.title.setText(food.getName());
            if(Objects.equals(food.getDescription(), ""))
                holder.description.setVisibility(View.GONE);
            else
                holder.description.setText(food.getDescription());
            holder.price.setText(food.getPrice() + " đ");
            //holder.quantity.setText(food.g() + " ");

            // loading album cover using Glide library
            // Glide.with(context).load(food.getImage()).into(holder.thumbnail);

            holder.order_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showPopupMenu(holder.order_button);
                    if(mClickListener != null && Integer.parseInt(holder.quantity.getText().toString()) > 0){
                        food.setQuantity(Integer.parseInt(holder.quantity.getText().toString()));
                        mClickListener.onBtnClick(food.getDishId(), food); //Integer.parseInt(holder.quantity.getText().toString()), food.getPrice()
                    }
                }
            });

            holder.increase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = Integer.parseInt(holder.quantity.getText().toString());
                    temp += 1;
                    holder.quantity.setText(temp + "");
                }
            });

            holder.decrease_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = Integer.parseInt(holder.quantity.getText().toString());
                    temp -= 1;
                    if (temp < 1) {
                        return;
                    }
                    holder.quantity.setText(temp + "");
                }
            });
        }
    }

    private void configureVendorViewHolder(final OrderAdapter.VendorViewHolder holder, int position) {
        final Vendor vendor = (Vendor) list.get(position);
        if(getItemCount() == 0){
            noContent.setVisibility(View.VISIBLE);
        }else {
            holder.title.setText(vendor.getName());
            if(Objects.equals(vendor.getAddress(), ""))
                holder.description.setVisibility(View.GONE);
            else
                holder.description.setText(vendor.getAddress());
            holder.price.setText(vendor.getPrice() + " đ");
            holder.quality.setText(String.format("%s", vendor.getQuality()));

            // loading album cover using Glide library
            // Glide.with(context).load(food.getImage()).into(holder.thumbnail);

            Picasso.with(context)
                    .load(vendor.getImage())
                    .error(R.drawable.shop_placeholder)
                    .placeholder(R.drawable.shop_placeholder)
                    .resize(200,200)
                    .centerCrop()
                    .into(holder.thumbnail);

            holder.order_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showPopupMenu(holder.order_button);
                    if(mClickListener != null && Integer.parseInt(holder.quantity.getText().toString()) > 0){
                        vendor.setQuantity(Integer.parseInt(holder.quantity.getText().toString()));
                        mClickListener.onBtnClick(vendor.getDishId(), vendor); //Integer.parseInt(holder.quantity.getText().toString()), vendor.getPrice()
                    }
                }
            });

            holder.increase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = Integer.parseInt(holder.quantity.getText().toString());
                    temp += 1;
                    holder.quantity.setText(temp + "");
                }
            });

            holder.decrease_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = Integer.parseInt(holder.quantity.getText().toString());
                    temp -= 1;
                    if (temp < 1) {
                        return;
                    }
                    holder.quantity.setText(temp + "");
                }
            });
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

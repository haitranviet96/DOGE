package com.haitr.doge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haitr on 5/19/2017.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView thumbnail;
        public EditText quantity;
        public Button order_button, increase_button, decrease_button;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            thumbnail = (ImageView) view.findViewById(R.id.imageView);
            quantity = (EditText) view.findViewById(R.id.quantity);
            order_button = (Button) view.findViewById(R.id.order_button);
            increase_button = (Button) view.findViewById(R.id.increase_button);
            decrease_button = (Button) view.findViewById(R.id.decrease_button);
        }
    }

    private List<Food> foods;

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public FoodAdapter(List<Food> foods) {
        this.foods = foods;
    }

    public List<Food> getFoods() {
        return foods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(getItemCount() == 0){
            MainActivity.noContent.setVisibility(View.VISIBLE);
        }else {
            MainActivity.noContent.setVisibility(View.GONE);
            Food food = foods.get(position);
            holder.title.setText(food.getName());
            holder.description.setText(food.getDescription());
            //holder.quantity.setText(food.g() + " ");

            // loading album cover using Glide library
            // Glide.with(context).load(food.getImage()).into(holder.thumbnail);

            holder.order_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showPopupMenu(holder.order_button);
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
        return foods.size();
    }
}
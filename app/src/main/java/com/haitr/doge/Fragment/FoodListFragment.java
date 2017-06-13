package com.haitr.doge.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.haitr.doge.Activity.BaseActivity;
import com.haitr.doge.Activity.MainActivity;
import com.haitr.doge.Adapter.OrderAdapter;
import com.haitr.doge.Constants;
import com.haitr.doge.Object.Dish;
import com.haitr.doge.Object.Food;
import com.haitr.doge.JSON;
import com.haitr.doge.R;
import com.haitr.doge.Object.Vendor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by haitr on 5/26/2017.
 */

public class FoodListFragment extends RecyclerViewFragment {

    Vendor vendor = new Vendor();
    PullRefreshLayout swipeLayout;
    ArrayList<Object> foodDishList = new ArrayList<>();
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    ImageView imageView;
    TextView name,open_time,close_time,address,noContent,quality,service,pricing,space;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vendor.setId(getArguments().getInt("VendorID"));
            vendor.setName(getArguments().getString("Vendor_Name"));
            vendor.setOpen_time(getArguments().getString("Open_Time"));
            vendor.setClose_time(getArguments().getString("Close_Time"));
            vendor.setAddress(getArguments().getString("Address"));
            vendor.setImage(getArguments().getString("Image"));
            vendor.setQuality(getArguments().getDouble("Quality"));
            vendor.setService(getArguments().getDouble("Service"));
            vendor.setPricing(getArguments().getDouble("Pricing"));
            vendor.setSpace(getArguments().getDouble("Space"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        // set Swipe layout
        swipeLayout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        // listen refresh event
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                prepareDish(Constants.BASE_URL + Constants.SEARCH_DISH_VIA_STORE_NAME + vendor.getName());
            }
        });
        swipeLayout.setRefreshStyle(randInt(0, 4));
        // refresh complete
        swipeLayout.setRefreshing(false);

        // get View
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.vendor_name);
        open_time = (TextView) rootView.findViewById(R.id.open_time);
        close_time = (TextView) rootView.findViewById(R.id.close_time);
        address = (TextView) rootView.findViewById(R.id.address);
        noContent = (TextView) rootView.findViewById(R.id.noContent);
        quality = (TextView) rootView.findViewById(R.id.quality);
        service = (TextView) rootView.findViewById(R.id.service);
        pricing = (TextView) rootView.findViewById(R.id.pricing);
        space = (TextView) rootView.findViewById(R.id.space);

        name.setText(vendor.getName());
        open_time.setText(vendor.getOpen_time());
        close_time.setText(vendor.getClose_time());
        address.setText(vendor.getAddress());
        Picasso.with(getContext())
                .load(Constants.BASE_PICTURE_URL + vendor.getImage())
                .error(R.drawable.shop_placeholder)
                .placeholder(R.drawable.shop_placeholder)
                .fit()
                .into(imageView);
        quality.setText(String.format("%s", vendor.getQuality()));
        service.setText(String.format("%s", vendor.getService()));
        pricing.setText(String.format("%s", vendor.getPricing()));
        space.setText(String.format("%s", vendor.getSpace()));

        orderAdapter = new OrderAdapter(foodDishList, getActivity(), noContent, new OrderAdapter.BtnClickListener() {
            @Override
            public void onBtnClick(int id, Dish dish) {
                ((MainActivity)getActivity()).changeCart(id,dish);
                Log.d("ckeck cart", dish.getQuantity()+"");
            }
        });

        // set up vendor list RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);

        prepareDish(Constants.BASE_URL + Constants.SEARCH_DISH_VIA_STORE_NAME + vendor.getName());

        return rootView;
    }

    public void prepareDish(String link) {

        if (isNetworkConnected()) {
            Log.d("foodDishList size : ", foodDishList.size() + "");
            foodDishList.clear();
            orderAdapter.notifyDataSetChanged();

            final JSON json = new JSON() {
                public void processFinish(String output) {
                    try {
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                        JSONArray jsonArray = new JSONArray(output);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject foodDish = jsonArray.getJSONObject(i);
                            Food temp = new Food(foodDish.getString("Food_Name"), foodDish.getString("Description"), foodDish.getString("Image"));
                            temp.setDishId(foodDish.getInt("DishID"));
                            temp.setPrice(foodDish.getInt("Price"));
                            temp.setDishName(foodDish.getString("Food_Name"));
                            foodDishList.add(temp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        swipeLayout.setRefreshing(false);
                        noContent.setVisibility(View.INVISIBLE);
                        orderAdapter.notifyDataSetChanged();
                    }
                }
            };
            json.execute(link);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (json.getStatus() == AsyncTask.Status.RUNNING) {
                        json.cancel(true);
                        Toast.makeText(getContext(), "Failed refreshing vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
                        noContent.setVisibility(View.VISIBLE);
                        // refresh complete
                        swipeLayout.setRefreshing(false);
                    }
                }
            }, 5000);
        } else {
            Toast.makeText(getContext(), "Failed refreshing vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
            noContent.setVisibility(View.VISIBLE);
            swipeLayout.setRefreshing(false);
        }
    }
}

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
import com.haitr.doge.Activity.MainActivity;
import com.haitr.doge.Adapter.OrderAdapter;
import com.haitr.doge.Constants;
import com.haitr.doge.Object.Dish;
import com.haitr.doge.Object.Food;
import com.haitr.doge.JSON;
import com.haitr.doge.R;
import com.haitr.doge.Object.Vendor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haitr on 5/26/2017.
 */

public class StoreListFragment extends RecyclerViewFragment {

    Food food = new Food();
    PullRefreshLayout swipeLayout;
    ArrayList<Object> storeDishList = new ArrayList<>();
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    ImageView imageView;
    TextView name,type,course,description,noContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            food.setId(getArguments().getInt("FoodID"));
            food.setName(getArguments().getString("FoodName"));
            food.setType(getArguments().getString("Type"));
            food.setCourse(getArguments().getString("Course"));
            food.setDescription(getArguments().getString("Description"));
            food.setImage(getArguments().getString("Image"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_list, container, false);

        // set Swipe layout
        swipeLayout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        // listen refresh event
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                prepareDish(Constants.BASE_URL + Constants.SEARCH_DISH_VIA_FOOD_NAME + food.getName());
            }
        });
        swipeLayout.setRefreshStyle(randInt(0, 4));
        // refresh complete
        swipeLayout.setRefreshing(false);

        // get View
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.vendor_name);
        type = (TextView) rootView.findViewById(R.id.food_type);
        course = (TextView) rootView.findViewById(R.id.food_course);
        description = (TextView) rootView.findViewById(R.id.address);
        noContent = (TextView) rootView.findViewById(R.id.noContent);

        name.setText(food.getName());
        type.setText(food.getType());
        course.setText(food.getCourse());
        description.setText(food.getDescription());

        orderAdapter = new OrderAdapter(storeDishList, getActivity(), noContent, new OrderAdapter.BtnClickListener() {
            @Override
            public void onBtnClick(int id, Dish dish) {
                ((MainActivity)getActivity()).changeCart(id, dish);
            }
        });

        // set up vendor list RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);

        prepareDish(Constants.BASE_URL + Constants.SEARCH_DISH_VIA_FOOD_NAME + food.getName());

        return rootView;
    }

    public void prepareDish(String link) {

        if (isNetworkConnected()) {
            Log.d("foodDishList size : ", storeDishList.size() + "");
            storeDishList.clear();
            orderAdapter.notifyDataSetChanged();

            final JSON json = new JSON() {
                public void processFinish(String output) {
                    try {
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                        JSONArray jsonArray = new JSONArray(output);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject storeDish = jsonArray.getJSONObject(i);
                            Vendor temp = new Vendor(storeDish.getString("Vendor_Name"), storeDish.getString("Address"), storeDish.getDouble("Quality"), storeDish.getString("Image"));
                            temp.setPrice(storeDish.getInt("Price"));
                            temp.setDishId(storeDish.getInt("DishID"));
                            temp.setDishName(food.getName());
                            storeDishList.add(temp);
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

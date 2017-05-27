package com.haitr.doge.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.haitr.doge.Constants;
import com.haitr.doge.Object.Food;
import com.haitr.doge.JSON;
import com.haitr.doge.Adapter.ListViewAdapter;
import com.haitr.doge.R;
import com.haitr.doge.Object.Vendor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends RecyclerViewFragment {

    public static TextView noContent, noContent1;
    ListViewAdapter foodAdapter, storeAdapter;
    PullRefreshLayout swipeLayout;
    RecyclerView foodListView,storeListView;

    ArrayList<Object> foodList = new ArrayList<>();
    ArrayList<Object> storeList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        swipeLayout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        // listen refresh event
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                //swipeLayout.setRefreshStyle(randInt(0,4));
                prepareFoods(Constants.BASE_URL + Constants.SEARCH_FOOD_1 + Constants.SEARCH_FOOD_2);
                prepareStore(Constants.BASE_URL + Constants.ALL_STORE_URL);
                //noContent.setVisibility(View.GONE);
//                foodDishList.clear();
//                foodDishList.add(new Food());
//                foodAdapter.notifyDataSetChanged();
//                swipeLayout.setRefreshing(false);
            }
        });

        swipeLayout.setRefreshStyle(randInt(0, 4));

        // refresh complete
        swipeLayout.setRefreshing(false);

        foodAdapter = new ListViewAdapter(foodList,getActivity());
        foodAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Food food = (Food) foodList.get(position);
                String name = food.getName();
                Toast.makeText(getActivity(), name + " was clicked!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("FoodID", food.getId());
                bundle.putString("Image", food.getImage());
                bundle.putString("FoodName", food.getName());
                bundle.putString("Type", food.getType());
                bundle.putString("Course", food.getCourse());
                bundle.putString("Description", food.getDescription());

                StoreListFragment storeListFragment = new StoreListFragment();
                storeListFragment.setArguments(bundle);

                ft.replace(R.id.content_main, storeListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        storeAdapter = new ListViewAdapter(storeList,getActivity());
        storeAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Vendor store = (Vendor) storeList.get(position);
                String name = store.getName();
                Toast.makeText(getActivity(), name + " was clicked!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("VendorID", store.getId());
                bundle.putString("Image", store.getImage());
                bundle.putString("Vendor_Name", store.getName());
                bundle.putString("Open_Time", store.getOpen_time());
                bundle.putString("Close_Time", store.getClose_time());
                bundle.putString("Address", store.getAddress());
                bundle.putDouble("Quality", store.getQuality());
                bundle.putDouble("Service", store.getService());
                bundle.putDouble("Pricing", store.getPricing());
                bundle.putDouble("Space", store.getSpace());

                FoodListFragment foodListFragment = new FoodListFragment();
                foodListFragment.setArguments(bundle);

                ft.replace(R.id.content_main, foodListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        foodListView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        storeListView = (RecyclerView) rootView.findViewById(R.id.recycler_view_store);
        noContent = (TextView) rootView.findViewById(R.id.noContent);
        noContent1 = (TextView) rootView.findViewById(R.id.noContent1);

        prepareFoods(Constants.BASE_URL + Constants.SEARCH_FOOD_1 + Constants.SEARCH_FOOD_2);
        prepareStore(Constants.BASE_URL + Constants.ALL_STORE_URL);

        // set up vendor list RecyclerView
        foodListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        foodListView.setNestedScrollingEnabled(false);
        foodListView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        foodListView.setItemAnimator(new DefaultItemAnimator());
        foodListView.setAdapter(foodAdapter);

        storeListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //storeListView.setItemAnimator(new DefaultItemAnimator());
        storeListView.setAdapter(storeAdapter);

        return rootView;
    }

    public void prepareFoods(String link) {

        //ArrayList<Food> foodDishList = new ArrayList<>();

        if (isNetworkConnected()) {
            //prepareFoods(getString(R.string.my_computer_link) + getString(R.string.all_food_link));
            foodList.clear();
            foodAdapter.notifyDataSetChanged();

            final JSON json = new JSON() {
                public void processFinish(String output) {
                    try {
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                        JSONArray jsonArray = new JSONArray(output);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject food = jsonArray.getJSONObject(i);
                            foodList.add(new Food(food.getInt("FoodID"), food.getString("Food_Name"), food.getString("Type"),food.getString("Course"),food.getString("Description"), food.getString("Image")));
                        }
                        swipeLayout.setRefreshing(false);
                        noContent.setVisibility(View.INVISIBLE);
                        foodAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                        Toast.makeText(getActivity().getApplicationContext(), "Failed refreshing vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
                        noContent.setVisibility(View.VISIBLE);
                        // refresh complete
                        swipeLayout.setRefreshing(false);
                    }
                }
            }, 5000);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Failed refreshing vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
            noContent.setVisibility(View.VISIBLE);
            swipeLayout.setRefreshing(false);
        }
//        try {
//            json.get(30000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            e.printStackTrace();
//        }
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (json.getStatus() == AsyncTask.Status.RUNNING) {
//                    json.cancel(true);
//                    Toast.makeText(getApplicationContext(), "Failed refreshing vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
//                    noContent.setVisibility(View.VISIBLE);
//                    // refresh complete
//                    swipeLayout.setRefreshing(false);
//                }
//            }
//        }, 5000);
//        checkAsyncTask chk = new checkAsyncTask(json);
//        // Thread keeping 1 minute time watch
//        (new Thread(chk)).start();
//        try {
//            json.get(3000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException | ExecutionException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            // TODO Auto-generated catch block
//            Toast.makeText(getApplicationContext(), "Failed getting vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
//            noContent.setVisibility(View.VISIBLE);
//            // refresh complete
//            swipeLayout.setRefreshing(false);
//        }

        //foodAdapter.setList(foodDishList);
        //foodAdapter = new ListViewAdapter(this, foodDishList);
        //foodListView.setAdapter(foodAdapter);
        //foodDishList.remove(1);
        //foodAdapter.notifyDataSetChanged();
    }

    public void prepareStore(String link) {

        if (isNetworkConnected()) {
            storeList.clear();
            storeAdapter.notifyDataSetChanged();

            final JSON json = new JSON() {
                public void processFinish(String output) {
                    try {
                        //Here you will receive the result fired from async class
                        //of onPostExecute(result) method.
                        JSONArray jsonArray = new JSONArray(output);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject store = jsonArray.getJSONObject(i);
                            storeList.add(new Vendor(store.getInt("VendorID"), store.getString("Vendor_Name"), store.getString("Address"), store.getString("Open_Time"), store.getString("Close_Time"), store.getDouble("Quality"), store.getDouble("Service"), store.getDouble("Pricing"), store.getDouble("Space"), store.getString("Image")));
                        }
                        swipeLayout.setRefreshing(false);
                        noContent1.setVisibility(View.INVISIBLE);
                        storeAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                        noContent1.setVisibility(View.VISIBLE);
                        // refresh complete
                        swipeLayout.setRefreshing(false);
                    }
                }
            }, 5000);
        } else {
            Toast.makeText(getContext(), "Failed refreshing vendor list. Please check your internet connection !", Toast.LENGTH_LONG).show();
            noContent1.setVisibility(View.VISIBLE);
            swipeLayout.setRefreshing(false);
        }
    }
}

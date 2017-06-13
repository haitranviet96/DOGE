package com.haitr.doge.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitr.doge.Adapter.CartAdapter;
import com.haitr.doge.Constants;
import com.haitr.doge.Object.Dish;
import com.haitr.doge.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static junit.runner.BaseTestRunner.savePreferences;

/**
 * Created by haitr on 6/10/2017.
 */

public class TransactionHistoryFragment extends RecyclerViewFragment {
    ArrayList<Dish> list = new ArrayList<>();
    RecyclerView listView;
    TextView orderTime, title, price;
    LinearLayout totalTable;
    Button order_button;
    CartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_your_cart, container, false);

        title = (TextView) rootView.findViewById(R.id.title);
        price = (TextView) rootView.findViewById(R.id.price);
        listView = (RecyclerView) rootView.findViewById(R.id.list_view);
        order_button = (Button) rootView.findViewById(R.id.btn_order);
        orderTime = (TextView) rootView.findViewById(R.id.delete_all);
        totalTable = (LinearLayout) rootView.findViewById(R.id.total_table);

        title.setText("Your purchase History :");
        price.setVisibility(View.GONE);
        order_button.setVisibility(View.GONE);
        totalTable.setVisibility(View.GONE);
        orderTime.setText("Order time");
        orderTime.setTextColor(Color.BLACK);

        adapter = new CartAdapter(getActivity(), list, new CartAdapter.BtnClickListener() {
            @Override
            public void onBtnClick(int id) {
            }
        });

        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listView.setNestedScrollingEnabled(false);
        listView.setAdapter(adapter);
        listView.setPadding(0,0,0,100);

        Ion.with(getContext())
                .load("GET", Constants.BASE_URL + Constants.USER + Constants.USER_HISTORY)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("result ",result);
                        if(Objects.equals(result, Constants.ERROR)){
                            return;
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new Dish(jsonObject.getInt("Quantity"), jsonObject.getString("Food_Name"),jsonObject.getString("Date")));
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        return rootView;
    }
}

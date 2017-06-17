package com.haitr.doge.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haitr.doge.Adapter.CartAdapter;
import com.haitr.doge.Constants;
import com.haitr.doge.Object.Dish;
import com.haitr.doge.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class YourCartActivity extends BaseActivity {

    ArrayList<Dish> list = new ArrayList<>();
    RecyclerView listView;
    TextView total_price,deleteAll;
    Button order_button;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

        Bundle b = this.getIntent().getExtras();
        if (b != null)
            list = (ArrayList<Dish>) b.getSerializable("transaction");

        //Log.d("check cart", list.get(0).getDishName());

        listView = (RecyclerView) findViewById(R.id.list_view);
        total_price = (TextView) findViewById(R.id.total_price);
        order_button = (Button) findViewById(R.id.btn_order);
        deleteAll = (TextView) findViewById(R.id.delete_all);

        adapter = new CartAdapter(this, list, new CartAdapter.BtnClickListener() {
            @Override
            public void onBtnClick(int id) {
                deleteOne(id);
            }
        });

        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IS_LOGIN){
                    Toast.makeText(YourCartActivity.this,"You haven't login yet. Please login to order !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(YourCartActivity.this, LoginActivity.class));
                }else {
                    if (list.size() > 0) {
                        new SweetAlertDialog(YourCartActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Won't be able to cancel this order!")
                                .setCancelText("No,cancel !")
                                .setConfirmText("Yes,order !")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        for (int i = 0; i < list.size(); i++) {
                                            Dish temp = list.get(i);
                                            Ion.with(getApplicationContext())
                                                    .load(Constants.BASE_URL + Constants.ADD_CART)
                                                    .setBodyParameter("submit", "[{\"" + temp.getDishId() + "\":\"" + temp.getQuantity() + "\"}]")
                                                    .asString()
                                                    .setCallback(new FutureCallback<String>() {
                                                        @Override
                                                        public void onCompleted(Exception e, String result) {
                                                            Log.d("check link", result);
                                                        }
                                                    });
                                        }
                                        sDialog
                                                .setTitleText("Ordered!")
                                                .setContentText("Your your order has been successful!")
                                                .setConfirmText("OK")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        onBackPressed();
                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        list.clear();
                                        changeCart();
                                    }
                                })
                                .show();
                    }
                }
            }
        });

        changeCart();

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                changeCart();
            }
        });
    }

    @Override
    protected void onResume(){
        getSharedPreferences();
        super.onResume();
    }


    void deleteOne(int id){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getDishId() == id){
                list.remove(i);
                changeCart();
                return;
            }
        }
    }

    void changeCart(){
        //set total
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            Dish temp = list.get(i);
            total += temp.getQuantity() * temp.getPrice();
        }
        total_price.setText(total + "đ");
        adapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.
        Bundle bundle = new Bundle();
        bundle.putSerializable("transaction_return", list);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}

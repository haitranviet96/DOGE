package com.haitr.doge.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.haitr.doge.Constants;
import com.haitr.doge.Object.Dish;

import java.util.ArrayList;

/**
 * Created by haitr on 5/27/2017.
 */

public class BaseActivity extends AppCompatActivity {

    boolean IS_LOGIN = false;
    String EMAIL, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, BIRTHDAY,PASSWORD;
    ArrayList<Dish> TRANSACTION = new ArrayList<>();

//    protected Toolbar mToolbar;

    void getSharedPreferences() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);

        IS_LOGIN = sharedPreferences.getBoolean(Constants.ISLOGIN, false);
        EMAIL = sharedPreferences.getString(Constants.EMAIL, "");
        FIRST_NAME = sharedPreferences.getString(Constants.FIRST_NAME, "");
        LAST_NAME = sharedPreferences.getString(Constants.LAST_NAME, "");
        PHONE = sharedPreferences.getString(Constants.PHONE, "");
        ADDRESS = sharedPreferences.getString(Constants.ADDRESS, "");
        BIRTHDAY = sharedPreferences.getString(Constants.BIRTHDAY, "");
        PASSWORD = sharedPreferences.getString(Constants.PASSWORD, "");
    }

    void savePreferences() {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(IS_LOGIN) {
            editor.putBoolean(Constants.ISLOGIN, true);
            editor.putString(Constants.EMAIL, EMAIL);
            editor.putString(Constants.FIRST_NAME, FIRST_NAME);
            editor.putString(Constants.LAST_NAME, LAST_NAME);
            editor.putString(Constants.PHONE, PHONE);
            editor.putString(Constants.ADDRESS, ADDRESS);
            editor.putString(Constants.BIRTHDAY, BIRTHDAY);
            editor.putString(Constants.PASSWORD, PASSWORD);
        }else{
            editor.clear();
        }

        editor.apply();
    }

    void getCart() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        int length = sharedPreferences.getInt(Constants.CART_LENGTH, 0);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                    if(i >= TRANSACTION.size()) {
                        TRANSACTION.add(i,
                                new Dish(sharedPreferences.getInt(Constants.CART_ID + i, 0),
                                        sharedPreferences.getInt(Constants.CART_QUANTITY + i, 0),
                                        sharedPreferences.getInt(Constants.CART_PRICE + i, 0),
                                        sharedPreferences.getString(Constants.CART_DISH_NAME + i, "")
                                ));
                    }else{
                        TRANSACTION.set(i,
                                new Dish(sharedPreferences.getInt(Constants.CART_ID + i, 0),
                                        sharedPreferences.getInt(Constants.CART_QUANTITY + i, 0),
                                        sharedPreferences.getInt(Constants.CART_PRICE + i, 0),
                                        sharedPreferences.getString(Constants.CART_DISH_NAME + i, "")
                                ));
                    }
            }
        }
    }

    void saveCart() {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (TRANSACTION.size() > 0) {
            editor.putInt(Constants.CART_LENGTH, TRANSACTION.size());
            for (int i = 0; i < TRANSACTION.size(); i++) {
                editor.putInt(Constants.CART_ID + i, TRANSACTION.get(i).getDishId());
                editor.putInt(Constants.CART_QUANTITY + i, TRANSACTION.get(i).getQuantity());
                editor.putInt(Constants.CART_PRICE + i, TRANSACTION.get(i).getPrice());
                editor.putString(Constants.CART_DISH_NAME + i, TRANSACTION.get(i).getDishName());
            }
        } else {
            editor.clear();
        }

        editor.apply();
    }

    int cartContains(int id) {
        for (int i = 0; i < TRANSACTION.size(); i++) {
            if (TRANSACTION.get(i).getDishId() == id) {
                return i;
            }
        }
        return -1;
    }

    void clearPreferences() {
        // clear user information
        IS_LOGIN = false;
        FIRST_NAME = "";
        LAST_NAME = "";
        EMAIL = "";
        PHONE = "";
        ADDRESS = "";
        BIRTHDAY = "";

        TRANSACTION.clear();

        savePreferences();
        saveCart();
    }
//
//    protected Toolbar activateToolbar(){
//        if (mToolbar == null){
//            mToolbar = (Toolbar) findViewById(R.id.toolbar);
//            if (mToolbar != null){
//                setSupportActionBar(mToolbar);
//            }
//        }
//        return mToolbar;
//    }
//
//    protected Toolbar activateToolbarWithHomeEnable(){
//        activateToolbar();
//        if (mToolbar != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        return mToolbar;
//    }
}

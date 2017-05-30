package com.haitr.doge.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.haitr.doge.Constants;

/**
 * Created by haitr on 5/27/2017.
 */

public class BaseActivity extends AppCompatActivity {

    boolean IS_LOGIN = false;
    String EMAIL, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, BIRTHDAY;
    int CART_LENGTH = 0;
    int CART_ID[];
    int CART_QUANTITY[];

//    protected Toolbar mToolbar;

    void getSharedPreferences(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);

        IS_LOGIN = sharedPreferences.getBoolean(Constants.ISLOGIN,false);
        EMAIL = sharedPreferences.getString(Constants.EMAIL,"");
        FIRST_NAME = sharedPreferences.getString(Constants.FIRST_NAME,"");
        LAST_NAME = sharedPreferences.getString(Constants.LAST_NAME,"");
        PHONE = sharedPreferences.getString(Constants.PHONE,"");
        ADDRESS = sharedPreferences.getString(Constants.ADDRESS,"");
        BIRTHDAY = sharedPreferences.getString(Constants.BIRTHDAY,"");
    }

    void savePreferences(){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putBoolean(Constants.ISLOGIN, IS_LOGIN);
        editor.putString(Constants.EMAIL, EMAIL);
        editor.putString(Constants.FIRST_NAME, FIRST_NAME);
        editor.putString(Constants.LAST_NAME, LAST_NAME);
        editor.putString(Constants.PHONE, PHONE);
        editor.putString(Constants.ADDRESS, ADDRESS);
        editor.putString(Constants.BIRTHDAY, BIRTHDAY);

        editor.apply();
    }

    void getCart(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        if(CART_LENGTH > 0){
            for(int i = 0 ; i < CART_LENGTH ; i++){
                CART_ID[i] = sharedPreferences.getInt(Constants.CART_ID + i, 0);
                CART_QUANTITY[i] = sharedPreferences.getInt(Constants.CART_QUANTITY, 0);
            }
        }
    }

    void saveCart(){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(CART_LENGTH > 0 ){
            for (int i = 0 ; i < CART_LENGTH; i++ ){
                editor.putInt(Constants.CART_ID + i , CART_ID[i]);
                editor.putInt(Constants.CART_QUANTITY + i , CART_QUANTITY[i]);
            }
        }

        editor.apply();
    }

    void clearPreferences(){
        // clear user information
        IS_LOGIN = false;
        FIRST_NAME = "";
        LAST_NAME = "";
        EMAIL = "";
        PHONE = "";
        ADDRESS = "";
        BIRTHDAY = "";

        savePreferences();
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

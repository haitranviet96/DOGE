package com.haitr.doge.Activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.haitr.doge.Constants;
import com.haitr.doge.Fragment.HomeFragment;
import com.haitr.doge.Fragment.StoreListFragment;
import com.haitr.doge.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconButton;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.String.format;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    HomeFragment homeFragment;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    FloatingActionButton fab;
    TextView itemCartBadgeTextView;
    IconButton iconButtonCart;

    MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSharedPreferences();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCartFragment();
            }
        });
        fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_shopping_cart)
                .colorRes(R.color.white));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //first fragment shown
        homeFragment = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_main, homeFragment).commit();

        // search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Toast.makeText(getApplicationContext(), "Search " + query, Toast.LENGTH_SHORT).show();
                homeFragment.prepareFoods(Constants.BASE_URL + Constants.SEARCH_FOOD_1 + query + Constants.SEARCH_FOOD_2);
                homeFragment.prepareStore(Constants.BASE_URL + Constants.SEARCH_STORE_1 + query + Constants.SEARCH_STORE_2);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                    toolbar.getMenu().findItem(R.id.action_search).setVisible(false);
                } else {
                    //show hamburger
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                    toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        View headerView = navigationView.getHeaderView(0);
        TextView profileName = (TextView) headerView.findViewById(R.id.your_name);
        TextView email = (TextView) headerView.findViewById(R.id.email);
        getSharedPreferences();
        showLogin(IS_LOGIN);
        if (IS_LOGIN){
            profileName.setText(LAST_NAME + " " + FIRST_NAME);
            email.setText(EMAIL);
        }else{
            profileName.setText("Your Name");
            email.setText("DOGE@doge.com");
        }
        super.onResume();
    }

    private void showLogin(boolean isLogIn){
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.nav_login).setVisible(!isLogIn);
        nav_menu.findItem(R.id.nav_logout).setVisible(isLogIn);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        MenuItem item1 = menu.findItem(R.id.action_cart);
        RelativeLayout badgeLayout = (RelativeLayout) item1.getActionView();
        itemCartBadgeTextView = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        iconButtonCart = (IconButton) badgeLayout.findViewById(R.id.badge_icon_button);
        iconButtonCart.setTextColor(Color.WHITE);
        showCart();

        iconButtonCart.setText("{fa-shopping-cart}");

        iconButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCartFragment();
            }
        });
        return true;
    }

    void showCartFragment(){
        if (IS_LOGIN) {
            //Intent intent = new Intent(getThis(), HJActivityMessagesContexts.class);
            //show fragment
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    void addCart(int id, int quantity){

    }

    void showCart(){
        if(CART_LENGTH > 0){
            itemCartBadgeTextView.setText(""+CART_LENGTH);
            itemCartBadgeTextView.setVisibility(View.VISIBLE);
        } else {
            itemCartBadgeTextView.setVisibility(View.GONE); // initially hidden
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().replace(R.id.content_main, new HomeFragment()).commit();
        } else if (id == R.id.nav_transaction) {
//            if (!IS_LOGIN){
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            }else{
//                navigationView.setCheckedItem(R.id.nav_transaction);
//                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                fm.beginTransaction().replace(R.id.content_main, new WatchlistFragment()).commit();
//            }
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_logout) {
            IS_LOGIN = false;
            savePreferences();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

            // send server logout notification
            Ion.with(getApplicationContext()).load("GET", Constants.BASE_URL + Constants.LOG_OUT);
            clearPreferences();

            //go home after log out
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().replace(R.id.content_main, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}



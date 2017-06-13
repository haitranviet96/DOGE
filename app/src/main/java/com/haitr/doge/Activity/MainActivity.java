package com.haitr.doge.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haitr.doge.Constants;
import com.haitr.doge.Fragment.HomeFragment;
import com.haitr.doge.Fragment.TransactionHistoryFragment;
import com.haitr.doge.Object.Dish;
import com.haitr.doge.Object.Food;
import com.haitr.doge.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HomeFragment homeFragment;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    FloatingActionButton fab;
    TextView itemCartBadgeTextView;
    IconTextView iconButtonCart;
    MaterialSearchView searchView;
    private Toolbar toolbar;
    static final int CHANGE_CART_REQUEST = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSharedPreferences();
        try {
            Ion.with(getApplicationContext())
                    .load("GET",Constants.BASE_URL + Constants.CHECK_LOGIN)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                JSONObject login = jsonArray.getJSONObject(0);
                                String email = login.getString("id");
                                Log.d("email",email);
                                if(!EMAIL.equalsIgnoreCase(email)) {
                                    IS_LOGIN = false;
                                    savePreferences();
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }})
                    .get(4000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

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

        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.nav_profile).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_user).colorRes(R.color.text_color));
    }

    public void changeCart(int id, Dish dish) {
        int check = cartContains(id);
        if(check == -1){
            TRANSACTION.add(dish);
        }else{
            TRANSACTION.get(check).setQuantity(TRANSACTION.get(check).getQuantity() + dish.getQuantity());
        }
        saveCart();
        itemCartBadgeTextView.setText("" + TRANSACTION.size());
        itemCartBadgeTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        View headerView = navigationView.getHeaderView(0);
        TextView profileName = (TextView) headerView.findViewById(R.id.your_name);
        TextView email = (TextView) headerView.findViewById(R.id.email);
        getSharedPreferences();
        showLogin(IS_LOGIN);
        if (IS_LOGIN) {
            profileName.setText(LAST_NAME + " " + FIRST_NAME);
            email.setText(EMAIL);
        } else {
            profileName.setText("Your Name");
            email.setText("DOGE@doge.com");
        }
        super.onResume();
    }

    private void showLogin(boolean isLogIn) {
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
        iconButtonCart = (IconTextView) badgeLayout.findViewById(R.id.badge_icon_button);
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

    void showCartFragment() {
        Intent intent;
        if (IS_LOGIN) {
            //show fragment
            intent = new Intent(MainActivity.this, YourCartActivity.class);
            if(TRANSACTION.size() != 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("transaction", TRANSACTION);
                intent.putExtras(bundle);
                startActivityForResult(intent,CHANGE_CART_REQUEST);
            }else{
                Toast.makeText(this,"You have nothing in your cart. Please choose something to order !", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"You haven't login yet. Please login to order !", Toast.LENGTH_SHORT).show();
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CHANGE_CART_REQUEST:
                if (resultCode == RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    Bundle b = data.getExtras();
                    if (b != null) {
                        ArrayList<Dish> temp;
                        temp = (ArrayList<Dish>) b.getSerializable("transaction_return");
                        TRANSACTION.clear();
                        Log.d("transaction", TRANSACTION.size() + "");
                        Log.d("temp", temp.size() + "");
                        TRANSACTION = temp;
                        Log.d("transaction", TRANSACTION.size() + "");
                        saveCart();
                        showCart();
                    }
                }
                break;
        }
    }

    void showCart() {
        getCart();
        if (TRANSACTION.size() > 0) {
            itemCartBadgeTextView.setText("" + TRANSACTION.size() );
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
            if (!IS_LOGIN){
                Toast.makeText(this,"You haven't login yet. Please login to order !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }else{
                navigationView.setCheckedItem(R.id.nav_transaction);
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().replace(R.id.content_main, new TransactionHistoryFragment()).commit();
            }
        }  else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, EditActivity.class));
        }else if (id == R.id.nav_login) {
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



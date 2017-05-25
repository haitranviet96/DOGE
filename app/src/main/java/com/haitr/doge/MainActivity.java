package com.haitr.doge;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView noContent, noContent1;
    public static ListViewAdapter foodAdapter, storeAdapter;
    public static PullRefreshLayout swipeLayout;
    RecyclerView foodListView,storeListView;
    ArrayList<Object> foodList = new ArrayList<>();
    ArrayList<Object> storeList = new ArrayList<>();

    MaterialSearchView searchView;

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }

    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return (mNetworkInfo != null);

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Toast.makeText(getApplicationContext(), "Search " + query, Toast.LENGTH_SHORT).show();
                prepareFoods(getString(R.string.my_computer_link) + getString(R.string.search_food_1) + query + getString(R.string.search_food_2));
                prepareStore(getString(R.string.my_computer_link) + getString(R.string.search_store_1) + query + getString(R.string.search_store_2));
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

        swipeLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // listen refresh event
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                //swipeLayout.setRefreshStyle(randInt(0,4));
                prepareFoods(getString(R.string.my_computer_link) + getString(R.string.all_food_link));
                prepareStore(getString(R.string.my_computer_link) + getString(R.string.all_store_link));
                //noContent.setVisibility(View.GONE);
//                foodList.clear();
//                foodList.add(new Food());
//                foodAdapter.notifyDataSetChanged();
//                swipeLayout.setRefreshing(false);
            }
        });

        swipeLayout.setRefreshStyle(randInt(0, 4));

        // refresh complete
        swipeLayout.setRefreshing(false);

        foodAdapter = new ListViewAdapter(foodList,this);
        storeAdapter = new ListViewAdapter(storeList,this);
        foodListView = (RecyclerView) findViewById(R.id.recycler_view);
        storeListView = (RecyclerView) findViewById(R.id.recycler_view_store);
        noContent = (TextView) findViewById(R.id.noContent);
        noContent1 = (TextView) findViewById(R.id.noContent1);

        prepareFoods(getString(R.string.my_computer_link) + getString(R.string.all_food_link));
        prepareStore(getString(R.string.my_computer_link) + getString(R.string.all_store_link));

        // set up food list RecyclerView
        foodListView.setLayoutManager(new GridLayoutManager(this, 2));
        foodListView.setNestedScrollingEnabled(false);
        foodListView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        foodListView.setItemAnimator(new DefaultItemAnimator());
        foodListView.setAdapter(foodAdapter);

        storeListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //storeListView.setItemAnimator(new DefaultItemAnimator());
        storeListView.setAdapter(storeAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    class checkAsyncTask implements Runnable {
//        JSON mAT;
//        Context context;
//        Handler mHandler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (mAT.getStatus() == AsyncTask.Status.RUNNING || mAT.getStatus() == AsyncTask.Status.PENDING) {
//                    mAT.cancel(true); //Cancel Async task or do the operation you want after 1 minute
//                    Toast.makeText(getApplicationContext(), "Failed refreshing food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
//                    noContent.setVisibility(View.VISIBLE);
//                    // refresh complete
//                    swipeLayout.setRefreshing(false);
//                }
//            }
//        };
//
//        public checkAsyncTask(JSON at) {
//            mAT = at;
//        }
//
//        @Override
//        public void run() {
//            mHandler.postDelayed(runnable, 5000);
//            // After 60sec the task in run() of runnable will be done
//        }
//    }

    private void prepareFoods(String link) {

        //ArrayList<Food> foodList = new ArrayList<>();

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
                            foodList.add(new Food(food.getInt("FoodID"), food.getString("Food_Name"), food.getString("Description"), 1));
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
                        Toast.makeText(getApplicationContext(), "Failed refreshing food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
                        noContent.setVisibility(View.VISIBLE);
                        // refresh complete
                        swipeLayout.setRefreshing(false);
                    }
                }
            }, 5000);
        } else {
            Toast.makeText(getApplicationContext(), "Failed refreshing food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
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
//                    Toast.makeText(getApplicationContext(), "Failed refreshing food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
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
//            Toast.makeText(getApplicationContext(), "Failed getting food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
//            noContent.setVisibility(View.VISIBLE);
//            // refresh complete
//            swipeLayout.setRefreshing(false);
//        }

        //foodAdapter.setList(foodList);
        //foodAdapter = new ListViewAdapter(this, foodList);
        //foodListView.setAdapter(foodAdapter);
        //foodList.remove(1);
        //foodAdapter.notifyDataSetChanged();
    }

    private void prepareStore(String link) {

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
                            storeList.add(new Vendor(store.getString("Vendor_Name"), store.getString("Address"), store.getDouble("Quality"), store.getString("Image")));
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
                        Toast.makeText(getApplicationContext(), "Failed refreshing food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
                        noContent1.setVisibility(View.VISIBLE);
                        // refresh complete
                        swipeLayout.setRefreshing(false);
                    }
                }
            }, 5000);
        } else {
            Toast.makeText(getApplicationContext(), "Failed refreshing food list. Please check your internet connection !", Toast.LENGTH_LONG).show();
            noContent1.setVisibility(View.VISIBLE);
            swipeLayout.setRefreshing(false);
        }
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}



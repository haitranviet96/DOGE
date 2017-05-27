package com.haitr.doge;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

import static android.R.attr.tag;

/**
 * Created by haitr on 5/20/2017.
 */

public abstract class JSON extends AsyncTask<String, Integer, String>{

//    public AsyncResponse delegate = null;
//
//    public interface AsyncResponse {
        protected abstract void processFinish(String output);

//    }
//
//    public JSON(AsyncResponse delegate){
//        this.delegate = delegate;
//}

    @Override
    protected String doInBackground(String... params) {
        return readURL(params[0]);
    }

    //public JSON(ArrayList<Food> foodList) {
    //    this.foodList = foodList;
    //}

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
            processFinish(s);
//        try {
//            JSONArray jsonArray = new JSONArray(s);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject food = jsonArray.getJSONObject(i);
//                MainActivity.foodList.add(new Food(food.getInt("FoodID"), food.getString("Food_Name"), food.getString("Description"), 1));
//                super.onPostExecute(s);
//            }
//            MainActivity.foodAdapter.notifyDataSetChanged();
//            // refresh complete
//            MainActivity.swipeLayout.setRefreshing(false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl.replace(" ", "%20"));
            Log.d("URL haitr 1: ",url.toString());
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("URL haitr 2: ",content.toString());

        return content.toString();
    }
}

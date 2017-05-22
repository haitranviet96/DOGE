package com.haitr.doge;

import android.os.AsyncTask;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by haitr on 5/20/2017.
 */

public class JSON extends AsyncTask<String, Integer, String>{

    private ArrayList<Food> foodList;

    @Override
    protected String doInBackground(String... params) {
        return readURL(params[0]);
    }

    public JSON(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    protected void onPostExecute(final String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject food = jsonArray.getJSONObject(i);
                foodList.add(new Food(food.getInt("FoodID"), food.getString("Food_Name"), food.getString("Description"), 1));
                super.onPostExecute(s);
            }
            MainActivity.adapter.notifyDataSetChanged();
            // refresh complete
            MainActivity.layout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
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
        return content.toString();
    }
}

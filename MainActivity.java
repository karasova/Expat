package com.example.expat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    EditText etCity;
    ArrayAdapter<String> adapter;
    private ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.cities_list);
        etCity = findViewById(R.id.city);
        int[] base_cities = getResources().getIntArray(R.array.base_cities);
        APITask task = new APITask();
        Integer[] i_cities = new Integer[base_cities.length];
        for (int i = 0; i < base_cities.length; i++){
            i_cities[i] = Integer.valueOf(base_cities[i]);
        }
        task.execute(i_cities);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        lv.setAdapter(adapter);

    }

    class APITask extends AsyncTask<Integer[], Integer, Void> {
        public Weather getTempByCity(int cityID) {
            String API_KEY = "50e59ad56c37dd66e36a46f5c0f1e6d8";
            String API_URL = "https://api.openweathermap.org/data/2.5/weather?id=" + String.valueOf(cityID) + "&apikey=" + API_KEY;
            try {
                URL url = new URL(API_URL);
                InputStream stream = (InputStream) url.getContent();
                Gson gson = new Gson();
                Weather weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                return weather;
            } catch (IOException e) {
                Log.d("mytag", e.getLocalizedMessage());
                return new Weather();
            }

        }
        @Override
        protected Void doInBackground(Integer[]... int_cities) {
            for (Integer cityID: int_cities[0]){
                Weather weather = getTempByCity(cityID);
                Log.d("mytag", "temperature for " + cityID + " is "+ weather.main.temp);
                int i = 0;
                if (weather != null) {
                    String str = String.valueOf(cityID) + ": " + String.valueOf((int)weather.main.temp - 273);
                    cities.add(str);
                }

            }
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            APITask task = new APITask();
            task.execute(new Integer[] {Integer.parseInt(etCity.getText().toString())});
        }
        if (v.getId() == R.id.clear) {
            adapter.clear();
        }



    }
}

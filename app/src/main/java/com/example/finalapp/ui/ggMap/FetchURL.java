package com.example.finalapp.ui.ggMap;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchURL extends AsyncTask<String, Void, String> {

    private final MapsActivity3 context;
    private String directionMode = "driving";

    public FetchURL(MapsActivity3 context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        // Lấy URL và mode
        String urlString = strings[0];
        directionMode = strings[1];
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                json.append(line);
            }

            br.close();
            conn.disconnect();

            return json.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            DataParser parser = new DataParser();
            PolylineOptions polylineOptions = parser.parse(jsonObject);
            if (polylineOptions != null) {
                context.drawRoute(polylineOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

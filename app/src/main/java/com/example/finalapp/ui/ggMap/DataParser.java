package com.example.finalapp.ui.ggMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    public PolylineOptions parse(JSONObject jObject) {
        List<LatLng> points = new ArrayList<>();
        PolylineOptions lineOptions = new PolylineOptions();

        try {
            JSONArray routes = jObject.getJSONArray("routes");
            if (routes.length() == 0) return null;

            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

            for (int i = 0; i < steps.length(); i++) {
                String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");
                points.addAll(decodePolyline(polyline));
            }

            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(0xFF2196F3); // Màu xanh dương
            lineOptions.geodesic(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lineOptions;
    }

    // Giải mã polyline
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lng += dlng;

            LatLng p = new LatLng((lat / 1E5), (lng / 1E5));
            poly.add(p);
        }

        return poly;
    }
}

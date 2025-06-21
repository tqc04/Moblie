package com.example.finalapp.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationUtil {
    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = coder.getFromLocationName(strAddress, 1);
            if (addressList == null || addressList.isEmpty()) return null;

            Address location = addressList.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            Log.e("Geocoder", "Lỗi lấy tọa độ: " + e.getMessage());
            return null;
        }
    }
}
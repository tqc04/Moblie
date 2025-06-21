//package com.example.finalapp.ui.ggMap;
//
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.os.Bundle;
//
//import com.example.finalapp.R;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms.location.FusedLocationProviderClient;
//
//
//public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps3);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // ✅ Nhận tọa độ từ intent
//        double lat = Double.parseDouble(getIntent().getStringExtra("lat"));
//        double lng = Double.parseDouble(getIntent().getStringExtra("lng"));
//        LatLng destination = new LatLng(lat, lng);
//
//        // ✅ Lấy vị trí hiện tại
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            return;
//        }
//        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
//            if (location != null) {
//                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
//
//                // Marker vị trí hiện tại
//                mMap.addMarker(new MarkerOptions().position(current).title("Vị trí của bạn"));
//                // Marker vị trí trọ
//                mMap.addMarker(new MarkerOptions().position(destination).title("Phòng trọ"));
//
//                // Vẽ đường
//                mMap.addPolyline(new PolylineOptions().add(current, destination).width(8).color(Color.RED));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
//            }
//        });
//
//        mMap.setMyLocationEnabled(true);
//    }
//
//}
package com.example.finalapp.ui.ggMap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.finalapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double targetLat, targetLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);


        try {
            targetLat = Double.parseDouble(getIntent().getStringExtra("lat").trim());
            targetLng = Double.parseDouble(getIntent().getStringExtra("lng").trim());
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tọa độ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Bật hiển thị vị trí hiện tại
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng roomLatLng = new LatLng(targetLat, targetLng);

                // Thêm marker vị trí của bạn
                mMap.addMarker(new MarkerOptions()
                        .position(userLatLng)
                        .title("Vị trí của bạn"));

                // Thêm marker vị trí phòng trọ
                mMap.addMarker(new MarkerOptions()
                        .position(roomLatLng)
                        .title("Phòng trọ"));

                // Vẽ đường từ bạn → trọ
                mMap.addPolyline(new PolylineOptions()
                        .add(userLatLng, roomLatLng)
                        .width(8)
                        .color(Color.RED));

                // Di chuyển camera đến giữa 2 điểm
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14));
            } else {
                Toast.makeText(this, "Không tìm được vị trí hiện tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude +
                "&destination=" + dest.latitude + "," + dest.longitude +
                "&key=AIzaSyCEzRGJkJ9FTA89aunjrzN4GrAAGUETt5E";
    }
    public void drawRoute(PolylineOptions polylineOptions) {
        mMap.addPolyline(polylineOptions);
    }


}

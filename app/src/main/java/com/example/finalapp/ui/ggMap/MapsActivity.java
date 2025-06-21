package com.example.finalapp.ui.ggMap;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.example.finalapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // khởi tạo Map System
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);//tạo vị trí có kinh độ , vĩ độ
//        LatLng NongLam = new LatLng(10.868137622400885, 106.78787686915302);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Đại học Nông Lâm ")); //add đc maker vào bảng đồ tại vị trí
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
//
    }
}
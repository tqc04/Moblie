package com.example.finalapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapp.R;
import com.example.finalapp.adapter.CustomListAdapter;
import com.example.finalapp.model.BaiDang;
import com.example.finalapp.model.Motel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private RecyclerView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private CustomListAdapter adapter;
    private ArrayList<Motel> listmotels;
    private ExecutorService executorService;
    private Handler mainHandler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        listView = root.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        listmotels = new ArrayList<>();
        adapter = new CustomListAdapter(getActivity(), listmotels);
        listView.setAdapter(adapter);
        GetDataFromFirebase();
        return root;
    }

    private void GetDataFromFirebase() {
        executorService.execute(() -> {
            Query query = databaseReference.child("DangBai");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Motel> tempList = new ArrayList<>();
                    long now = System.currentTimeMillis();
                    Log.d("TIME_CHECK", "Now: " + now);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BaiDang baiDang = dataSnapshot.getValue(BaiDang.class);
                        if (baiDang != null && baiDang.isView()) {
                            Long expirationTimestamp = baiDang.getExpirationTimestamp();
                            Log.d("TIME_CHECK", "Expiration: " + expirationTimestamp);
                            if (expirationTimestamp != null && now <= expirationTimestamp) {
                                Motel motel = dataSnapshot.getValue(Motel.class);
                                if (motel != null) {
                                    tempList.add(motel);
                                }
                            }
                    }}
                    mainHandler.post(() -> {
                        listmotels.clear();
                        listmotels.addAll(tempList);
                        adapter.notifyDataSetChanged();
                        if (tempList.isEmpty()) {
                            Toast.makeText(getContext(), getString(R.string.home_empty), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mainHandler.post(() -> Toast.makeText(getContext(), getString(R.string.error) + ": " + error.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
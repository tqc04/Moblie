package com.example.finalapp.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.finalapp.Detail;
import com.example.finalapp.R;
import com.example.finalapp.custom.SearchAdapter;
import com.example.finalapp.model.BaiDang;
import com.example.finalapp.model.Motel;
import com.example.finalapp.model.QuanHuyen;
import com.example.finalapp.model.TinhTP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {
    ///////////

    ArrayAdapter<TinhTP> adapter_Tinh;
    ArrayAdapter<QuanHuyen> adapter_QH;
    RadioButton timtro, oghep, tinhhuyen;
    Spinner tinh, huyen;
    Button button;
    boolean check;
    List<Motel> motels;
    ListView listView;
    //    CustomListAdapter cus;
    SearchView searchView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SearchAdapter adapter;
    private ArrayList<BaiDang> listmotels;
    private BaiDang baiDang;



    LinearLayout layoutTimKiem;
    Button btnHienForm;
    //////////
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
//    public static com.thanhtai.qlntusers.ui.search.SearchFragment newInstance(String param1, String param2) {
//        com.example.test_search_2.ui.search.SearchFragment fragment = new com.example.test_search_2.ui.search.SearchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        anhxa(root);

        btnHienForm.setOnClickListener(v -> {
            layoutTimKiem.setVisibility(View.VISIBLE);
            btnHienForm.setVisibility(View.GONE);
            tinhhuyen.setChecked(true);
        });

        check = tinhhuyen.isChecked();
        motels = new ArrayList<>();

//        adapter = new SearchAdapter(listmotels, getActivity());
//        listView.setAdapter(adapter);
        tinhhuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    tinhhuyen.setChecked(false);
                    adapter_Tinh.clear();
                    adapter_QH.clear();
                    check = false;
                } else {
                    tinhhuyen.setChecked(true);
                    actionSql();
                    check= true;
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        ////Arraylisst
        listmotels = new ArrayList<>();

        /// Clear ArrayList
        ClearAll();
        GetDataFromFirebase();

        ///
        tinhhuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = tinhhuyen.isChecked();
                if(!check) {
                    adapter_Tinh.clear();
                    adapter_QH.clear();
                    check=false;
                }else {
                    tinhhuyen.setChecked(true);
                    actionSql();
                    check=true;
                }
            }
        });
        actionTK();
        if(searchView!= null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BaiDang baiDang = listmotels.get(position);
                    Intent intent = new Intent(getContext(), Detail.class);
                    intent.putExtra("img",baiDang.getImg());
                    intent.putExtra("tieude", baiDang.getTieude());
                    intent.putExtra("mota",baiDang.getMota());
                    intent.putExtra("tinh", baiDang.getTinh());
                    intent.putExtra("huyen",baiDang.getHuyen());
                    intent.putExtra("address", baiDang.getAddress());
                    intent.putExtra("price", baiDang.getPrice());
                    intent.putExtra("title", baiDang.getTitle());
                    intent.putExtra("phone", baiDang.getPhone());
                    intent.putExtra("dientich",baiDang.getDientich());
                    startActivity(intent);
                }
            });
        }
        return root;
    }
    private void search(String s) {
        listmotels = new ArrayList<>();
        for (BaiDang b : listmotels){
            if(b.getTinh().toLowerCase().contains(s.toLowerCase())){
                listmotels.add(b);
            }
        }
//        GetDataFromFirebase();
        adapter = new SearchAdapter(listmotels, getActivity());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void actionTK() {
        button.setOnClickListener(v -> {
            listmotels.clear();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            String loaiBaiDang = timtro.isChecked() ? "Cho thuê phòng trọ" : "Tìm người ở ghép";

            Query allPost = myRef.child("DangBai");
            allPost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listmotels.clear();
                    for (DataSnapshot item : snapshot.getChildren()) {
                        BaiDang baiDang = new BaiDang();
                        baiDang.setKey(item.getKey());
                        baiDang.setImg(getSafeValue(item, "img"));
                        baiDang.setTieude(getSafeValue(item, "tieude"));
                        baiDang.setMota(getSafeValue(item, "mota"));
                        baiDang.setLatitude(getSafeValue(item, "latitude"));
                        baiDang.setLongitude(getSafeValue(item, "longitude"));
                        baiDang.setAddress(getSafeValue(item, "address")); // nếu chưa có dòng này

                        baiDang.setTitle(getSafeValue(item, "title"));
                        baiDang.setTinh(getSafeValue(item, "tinh"));
                        baiDang.setHuyen(getSafeValue(item, "huyen"));
                        baiDang.setPhone(getSafeValue(item, "phone"));
                        baiDang.setPrice(getSafeValue(item, "price"));
                        baiDang.setDientich(getSafeValue(item, "dientich"));
                        baiDang.setView(Boolean.parseBoolean(getSafeValue(item, "view")));

//                        String fullAddress = baiDang.getAddress() + ", " + baiDang.getHuyen() + ", " + baiDang.getTinh();
//                        LatLng latLng = LocationUtil.getLocationFromAddress(getContext(), fullAddress);
//
//                        if (latLng != null) {
//                            baiDang.setLatitude(String.valueOf(latLng.latitude));
//                            baiDang.setLongitude(String.valueOf(latLng.longitude));
//                        }


                        if (!baiDang.getTitle().equalsIgnoreCase(loaiBaiDang)) continue;
                        Log.d("DEBUG_ALL_BAIDANG", "Bài đăng: " +
                                baiDang.getTieude() + " | " +
                                "Tỉnh: " + baiDang.getTinh() + " | " +
                                "Huyện: " + baiDang.getHuyen());
                        if (baiDang.isView()) {
                            Log.d("DEBUG_RADIO", "Trạng thái nút Tỉnh/Huyện: " + tinhhuyen.isChecked());
                            if (tinhhuyen.isChecked()) {
                                String st_tinh = tinh.getSelectedItem().toString();
                                String st_huyen = huyen.getSelectedItem().toString();

                                Log.d("DEBUG_TINH_HUYEN", "So sánh Spinner: " + st_tinh + " - " + st_huyen);
                                Log.d("DEBUG_TINH_HUYEN", "Bài đăng có: " + baiDang.getTinh() + " - " + baiDang.getHuyen());

                                if (st_tinh.equals(baiDang.getTinh()) && st_huyen.equals(baiDang.getHuyen())) {
                                    Log.d("DEBUG_MATCH", "✔ Khớp");
                                    listmotels.add(baiDang);
                                } else {
                                    Log.d("DEBUG_MATCH", "✘ Không khớp");
                                }
                            } else {
                                listmotels.add(baiDang);
                            }}
                    }

                    Collections.reverse(listmotels);
                    adapter = new SearchAdapter(listmotels, getActivity());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    layoutTimKiem.setVisibility(View.GONE);
                    btnHienForm.setVisibility(View.VISIBLE);
                    Log.d("DEBUG_KETQUA", "Tổng kết quả sau lọc: " + listmotels.size());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        });
    }

    private void GetDataFromFirebase() {

        Query query = databaseReference.child("DangBai");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BaiDang baiDang = new BaiDang();
                    baiDang.setImg(dataSnapshot.child("img").getValue().toString());
                    baiDang.setTieude(dataSnapshot.child("tieude").getValue().toString());
                    baiDang.setMota(dataSnapshot.child("mota").getValue().toString());
                    baiDang.setAddress(dataSnapshot.child("address").getValue().toString());
                    baiDang.setTitle(dataSnapshot.child("title").getValue().toString());
                    baiDang.setTinh(dataSnapshot.child("tinh").getValue().toString());
                    baiDang.setHuyen(dataSnapshot.child("huyen").getValue().toString());
                    baiDang.setPhone(dataSnapshot.child("phone").getValue().toString());
                    baiDang.setPrice(dataSnapshot.child("price").getValue().toString());
                    baiDang.setDientich(dataSnapshot.child("dientich").getValue().toString());

                    listmotels.add(baiDang);
                }
                adapter = new SearchAdapter(listmotels, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ClearAll(){
        if(listmotels!= null){
            listmotels.clear();
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
        listmotels = new ArrayList<>();
    }

    private void anhxa(View root) {
        tinh = (Spinner) root.findViewById(R.id.spiner_tinh_search);
        huyen = (Spinner) root.findViewById(R.id.spiner_huyen_search);
        timtro = (RadioButton) root.findViewById(R.id.rbtn_timtro);
        oghep = (RadioButton) root.findViewById(R.id.rbtn_oghep);
        button = (Button) root.findViewById(R.id.btn_timkiem);
        tinhhuyen = (RadioButton) root.findViewById(R.id.rbtn_tinhhuyen);
        listView =  root.findViewById(R.id.list_item_search);
        searchView = root.findViewById(R.id.search);
        layoutTimKiem = root.findViewById(R.id.layout_timkiem);
        btnHienForm = root.findViewById(R.id.btn_hien_form);
    }

    public void actionSql() {
        DatabaseReference tinhRef = FirebaseDatabase.getInstance().getReference("TinhTP");
        tinhRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TinhTP> tinhTPS = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    TinhTP tp = dataSnapshot.getValue(TinhTP.class);
                    tinhTPS.add(tp);
                }
                adapter_Tinh = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tinhTPS);
                tinh.setAdapter(adapter_Tinh);
                // Set district spinner when province selected
                tinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TinhTP tinhTP1 = (TinhTP) parent.getAdapter().getItem(position);
                        int idtinh = tinhTP1.getId();
                        DatabaseReference huyenRef = FirebaseDatabase.getInstance().getReference("QuanHuyen").child(String.valueOf(idtinh));
                        huyenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<QuanHuyen> quanHuyens = new ArrayList<>();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    QuanHuyen qh = dataSnapshot.getValue(QuanHuyen.class);
                                    quanHuyens.add(qh);
                                }
                                adapter_QH = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, quanHuyens);
                                huyen.setAdapter(adapter_QH);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("TEST_QH", "Lỗi đọc Firebase: " + error.getMessage());
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        });
    }
    private String getSafeValue(DataSnapshot item, String key) {
        Object value = item.child(key).getValue();
        return value != null ? value.toString() : "";
    }
}
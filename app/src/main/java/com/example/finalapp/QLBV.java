package com.example.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapp.custom.NewAdapter;
import com.example.finalapp.model.BaiDang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QLBV extends AppCompatActivity {
    ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private NewAdapter adapter; //để hiển thị mỗi bài đăng trong ListView
    private ArrayList<BaiDang> listnew; // Danh sách tất cả bài đăng
    private ArrayList<BaiDang> filteredList; // danh sách bài đăng sau khi lọc
    Spinner spinnerFilter; // Spinner để chọn bộ lọc (đã duyệt, chưa duyệt,...).

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_l_b_v);
        // Lấy tham chiếu đến ListView và Spinner từ layout.
        listView = findViewById(R.id.list_item_new);
        spinnerFilter = findViewById(R.id.spinner_filter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        listnew = new ArrayList<>();
        filteredList = new ArrayList<>();

        ClearAll(); // làm sạch danh sách.
        GetDataFromFirebase(); //  Gọi phương thức để lấy dữ liệu từ Firebase.

        // Tạo danh sách tùy chọn trực tiếp
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("Tất cả");
        filterOptions.add("Đã duyệt");
        filterOptions.add("Chưa duyệt");
        filterOptions.add("Đã hết hạn");

        // Gán option vào Spinner.
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filterOptions);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterSpinner);
        // khi người dùng chọn một tùy chọn trong Spinner.
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                filterList(selectedOption); // Gọi phương thức lọc danh sách dựa trên tùy chọn đã chọn.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì cả
            }
        });

        // Đăng ký một launcher để xử lý kết quả trả về từ Activity khác.
        // Khi bên kia cập nhật, xóa,... thì quay lại trang này.
        ActivityResultLauncher<Intent> updateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        GetDataFromFirebase(); // Refresh lại dữ liệu
                    }
                });

        // Thiết lập lắng nghe sự kiện click cho từng mục trong ListView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Sử dụng filteredList để lấy bài đăng đã chọn
                BaiDang baiDang = filteredList.get(position);
                Intent intent = new Intent(getApplicationContext(), UpdateDelete.class);
                intent.putExtra("key", baiDang.getKey()); // Truyền khóa
                updateLauncher.launch(intent); // Khởi động Activity UpdateDelete.
            }
        });
    }

public void GetDataFromFirebase() {
    listnew.clear();
    filteredList.clear();
    databaseReference.child("DangBai").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                BaiDang baiDang = dataSnapshot.getValue(BaiDang.class); // Chuyển đổi DataSnapshot thành đối tượng BaiDang.
                if (baiDang != null) {
                    baiDang.setKey(dataSnapshot.getKey()); // Gán khóa của bài đăng.
                    listnew.add(baiDang);
                }
            }
            // Gọi phương thức lọc danh sách sau khi lấy dữ liệu.
            filterList(spinnerFilter.getSelectedItem().toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(QLBV.this, "Lỗi khi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    });
}


    private void filterList(String option) {
        filteredList.clear(); // Làm sạch filteredList trước khi lọc
        long now = System.currentTimeMillis();
        Log.d("THOIGIANHIENTAI","TG: "+now);
        for (BaiDang baiDang : listnew) {
            if (option.equals("Đã duyệt") && baiDang.isView()) {
                filteredList.add(baiDang);
            } else if (option.equals("Chưa duyệt") && !baiDang.isView()) {
                filteredList.add(baiDang);
            } else if (option.equals("Tất cả")) {
                filteredList.add(baiDang);
            } else if (option.equals("Đã hết hạn") && baiDang.getExpirationTimestamp() <= now) {
                filteredList.add(baiDang);
            }
        }
        // Tạo một adapter mới với danh sách đã lọc.
        adapter = new NewAdapter(filteredList, getApplicationContext());
        listView.setAdapter(adapter); // Gán adapter cho ListView.
        adapter.notifyDataSetChanged(); // Cập nhật giao diện của ListView.
    }

    private void ClearAll() {
        if (listnew != null) {
            listnew.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        listnew = new ArrayList<>();
    }
}
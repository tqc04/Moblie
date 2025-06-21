package com.example.finalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapp.model.BaiDang;
import com.example.finalapp.ui.ggMap.MapsActivity2;
import com.example.finalapp.ui.ggMap.MapsActivity3;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.example.finalapp.R;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    TextView tv_tv_ttchutro, tv_diachi, tv_doituong, tv_gia, tv_hinhthuc, tv_ten, tv_sdt, tv_tinh, tv_huyen, tv_mota, tv_dt;
    BaiDang baiDang=new BaiDang();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);
        anhxa();
        Intent intent = getIntent();
//        String img = intent.getStringExtra("img");
//        String diachi = intent.getStringExtra("address");
//        String doituong = intent.getStringExtra("tieude");
//        String gia = intent.getStringExtra("price");
//        String hinhthuc = intent.getStringExtra("title");
//        String mota = intent.getStringExtra("mota");
//        String sdt = intent.getStringExtra("phone");
//        String tinh = intent.getStringExtra("tinh");
//        String huyen = intent.getStringExtra("huyen");
//        String phone = intent.getStringExtra("phone");
//        String dt = intent.getStringExtra("dientich");
// ✅ Lấy key từ Intent để load từ Firebase
        String id = getIntent().getStringExtra("key");
        if (id != null && !id.isEmpty()) {
            loadData(id);
        }
        // Cách lấy ảnh
//        String imgString = "data:image/png;base64," + img;
        Button btnMap = findViewById(R.id.btn_map);
        btnMap.setOnClickListener(view -> {
            if (baiDang != null) {
                String address = baiDang.getAddress() + ", " + baiDang.getHuyen() + ", " + baiDang.getTinh();

                try {
                    String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");

                    String uri = "https://www.google.com/maps/dir/?api=1" +
                            "&destination=" + encodedAddress +
                            "&travelmode=driving";

                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent1.setPackage("com.google.android.apps.maps");

                    if (intent1.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent1);
                    } else {
                        Toast.makeText(Detail.this, "Thiết bị chưa cài Google Maps", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Detail.this, "Lỗi địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        tv_diachi.setText(diachi);
//        tv_doituong.setText(doituong);
//        tv_gia.setText(gia);
//        tv_hinhthuc.setText(hinhthuc);
//        tv_mota.setText(mota);
//        tv_sdt.setText(sdt);
//        tv_tinh.setText(tinh);
//        tv_huyen.setText(huyen);
//        tv_sdt.setText(phone);
//        tv_dt.setText(dt);


        tv_tv_ttchutro = findViewById(R.id.tv_ttchutro);
        tv_tv_ttchutro.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }
    private void loadData(String id) {
        FirebaseDatabase.getInstance().getReference("DangBai").child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        baiDang = snapshot.getValue(BaiDang.class);  // ✅ GÁN vào biến toàn cục

                        if (baiDang != null) {
                            // Set ảnh
                            String imgString = "data:image/png;base64," + baiDang.getImg();
                            Bitmap bitmap = decodeBase64ToBitmap(imgString);
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                imageView.setImageResource(R.drawable.placeholder_image);
                            }

                            // Set text
                            tv_diachi.setText("Địa chỉ: " + baiDang.getAddress());
                            tv_doituong.setText("Tiêu đề: " + baiDang.getTieude());
                            tv_gia.setText("Giá: " + baiDang.getPrice() + " VND");
                            tv_hinhthuc.setText("Hình thức: " + baiDang.getTitle());
                            tv_mota.setText("Mô tả: " + baiDang.getMota());
                            tv_sdt.setText("Số điện thoại liên hệ: " + baiDang.getPhone());
                            tv_tinh.setText("Tỉnh/TP: " + baiDang.getTinh());
                            tv_huyen.setText("Quận/Huyện: " + baiDang.getHuyen());
                            tv_dt.setText("Diện tích: " + baiDang.getDientich() + " m²");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    private Bitmap decodeBase64ToBitmap(String base64Image) {
        if (base64Image.startsWith("data:image/png;base64,")) {
            String base64 = base64Image.split(",")[1]; // Tách phần mã hóa
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            return null; // Không phải chuỗi Base64 hợp lệ
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ttchutro || v.getId() == R.id.image_detail) {
            if (baiDang != null && baiDang.getLatitude() != null && baiDang.getLongitude() != null) {
                try {
                    double lat = Double.parseDouble(baiDang.getLatitude().trim());
                    double lng = Double.parseDouble(baiDang.getLongitude().trim());

                    Intent i2 = new Intent(Detail.this, MapsActivity3.class);
                    i2.putExtra("latitude", lat);
                    i2.putExtra("longitude", lng);
                    startActivity(i2);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Tọa độ không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không có thông tin tọa độ để hiển thị bản đồ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void anhxa() {
        imageView = (ImageView) findViewById(R.id.image_detail);
        tv_diachi = (TextView) findViewById(R.id.diachi_detail);
        tv_doituong = (TextView) findViewById(R.id.doituong_detail);
        tv_gia = (TextView) findViewById(R.id.gia_detail);
        tv_hinhthuc = (TextView) findViewById(R.id.hinhthuc_detail);
        tv_ten = (TextView) findViewById(R.id.ten_detail);
        tv_sdt = (TextView) findViewById(R.id.sdt_detail);
        tv_tinh = (TextView) findViewById(R.id.tinh_detail);
        tv_huyen = (TextView) findViewById(R.id.huyen_detail);
        tv_mota = findViewById(R.id.mota_detail);
        tv_dt = findViewById(R.id.txt_dt);
    }
}

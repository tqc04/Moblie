package com.example.finalapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateDelete extends AppCompatActivity {
//    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> imagePickerLauncher; // Biến dùng để xử lý kết quả khi người dùng chọn ảnh từ thư viện.
    private String imgBase64 = null; // Lưu chuỗi ảnh đã mã hóa dạng base64 (dùng để hiển thị và lưu lên Firebase).
    ImageView imageView;
    TextView tv_diachi,tv_gia,tv_ten,tv_sdt, tv_mota, tv_dt;
    TextView mhinhthuc, mtinh, mhuyen;
    Button update, delete, chontep, duyetbai;
    DatabaseReference databaseReference;

    // Hàm khởi tạo Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete); // Thiết lập layout cho Activity.
        anhxa(); // ánh xạ các biến với View trong layout.
        actionImage(); // Hành động chọn ảnh.
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            Picasso.get().load(imageUri).into(imageView); // hiển thị ảnh bằng Picasso.
                            try {
                                imgBase64 = encodeImageToBase64(imageUri); // Cập nhật imgBase64 (mã hóa).
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(UpdateDelete.this, "Lỗi mã hóa ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DangBai").child(key);

        // Load dữ liệu từ Firebase. addListenerForSingleValueEvent để lắng nghe một lần.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tieude = snapshot.child("tieude").getValue(String.class);
                    String mota = snapshot.child("mota").getValue(String.class);
                    String tinh = snapshot.child("tinh").getValue(String.class);
                    String huyen = snapshot.child("huyen").getValue(String.class);
                    String hinhthuc = snapshot.child("title").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String diachi = snapshot.child("address").getValue(String.class);
                    String img = snapshot.child("img").getValue(String.class);
                    String gia = snapshot.child("price").getValue(String.class);
                    String dt = snapshot.child("dientich").getValue(String.class);

                    tv_ten.setText(tieude);
                    tv_mota.setText(mota);
                    mtinh.setText(tinh);
                    mhuyen.setText(huyen);
                    mhinhthuc.setText(hinhthuc);
                    tv_sdt.setText(phone);
                    tv_diachi.setText(diachi);
                    tv_gia.setText(gia);
                    tv_dt.setText(dt);
                    imgBase64 = img;

                    // Kiểm tra đã duyệt hay chưa để ẩn nút duyệt.
                    Boolean isViewed = snapshot.child("view").getValue(Boolean.class);
                    if (isViewed != null && isViewed) {
                        duyetbai.setVisibility(View.GONE); // Ẩn nút nếu đã duyệt
                    }

                    // Kiểm tra nếu bài đăng hết hạn thì ẩn đã duyệt và cập nhật.
                    Long expirationTimestamp = snapshot.child("expirationTimestamp").getValue(Long.class);
                    if (expirationTimestamp != null) {
                        long now = System.currentTimeMillis();
                        if (expirationTimestamp <= now) {
                            // Bài đã hết hạn, ẩn nút update và duyệt bài
                            if (update != null) {
                                update.setVisibility(View.GONE); // Ẩn nút sửa
                            }
                            if (duyetbai != null) {
                                duyetbai.setVisibility(View.GONE); // Ẩn nút duyệt
                            }
                        }
                    }

                    // Hiển thị ảnh từ base64 nếu có.
                    String imgString = "data:image/png;base64," + img;
                    Bitmap bitmap = decodeBase64ToBitmap(imgString);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.placeholder_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateDelete.this, "Lỗi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void actionImage() {
        chontep.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT); // Yêu cầu hệ thống cho người dùng chọn một nội dung (file) từ thiết bị.
            imagePickerLauncher.launch(intent);// Gửi intent và đợi người dùng chọn ảnh.
        });
    }

    private String encodeImageToBase64(Uri imageUri) throws IOException {
        ContentResolver contentResolver = this.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(imageUri); // Mở InputStream để đọc dữ liệu ảnh từ imageUri.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // lưu trữ tạm dữ liệu ảnh khi đọc.
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        inputStream.close();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); // mã hóa sang chuỗi Base64.
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        if (base64Image.startsWith("data:image/png;base64,")) {
            String base64 = base64Image.split(",")[1]; // Tách phần mã hóa
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT); // Giải mã chuỗi base64 thành mảng byte byte[].
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); // Tạo ảnh Bitmap từ mảng byte đã giải mã.
        } else {
            return null; // Không phải chuỗi Base64 hợp lệ
        }
    }


public void btnUpdate(View view){
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String imgToUpdate;
            if (imgBase64 != null) {
                imgToUpdate = imgBase64;
            } else {
                imgToUpdate = getIntent().getStringExtra("img"); // dùng ảnh cũ nếu chưa chọn mới.
            }
            snapshot.getRef().child("tieude").setValue(tv_ten.getText().toString());
            snapshot.getRef().child("mota").setValue(tv_mota.getText().toString());
            snapshot.getRef().child("tinh").setValue(mtinh.getText().toString());
            snapshot.getRef().child("huyen").setValue(mhuyen.getText().toString());
            snapshot.getRef().child("title").setValue(mhinhthuc.getText().toString());
            snapshot.getRef().child("phone").setValue(tv_sdt.getText().toString());
            snapshot.getRef().child("address").setValue(tv_diachi.getText().toString());
            snapshot.getRef().child("img").setValue(imgToUpdate);
            snapshot.getRef().child("price").setValue(tv_gia.getText().toString());
            snapshot.getRef().child("dientich").setValue(tv_dt.getText().toString());

            Toast.makeText(UpdateDelete.this, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            UpdateDelete.this.finish(); // Đóng UpdateDelete và quay về màn hình trước đó.
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(UpdateDelete.this, "Lỗi cập nhật: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}



//    public void btnDelete(View view){
//
//        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(UpdateDelete.this,"Recored Delete successfully...!", Toast.LENGTH_LONG).show();
//                UpdateDelete.this.finish();
//                }else{
//                    Toast.makeText(UpdateDelete.this,"Recored Delete Error...!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    public void btnDelete(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa bài đăng")
                .setMessage("Bạn có chắc chắn muốn xóa bài đăng này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateDelete.this, "Đã xóa bài đăng thành công!", Toast.LENGTH_LONG).show();
                                setResult(RESULT_OK);
                                UpdateDelete.this.finish(); // Kết thúc Activity

                            } else {
                                Toast.makeText(UpdateDelete.this, "Lỗi khi xóa bài đăng!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                })
                .setNegativeButton("Không", null) // Không làm gì nếu người dùng chọn "Không"
                .show(); // hiển thị hộp thoại (AlertDialog).
    }

    public void btnDuyet(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Duyệt bài đăng")
                .setMessage("Bạn có chắc chắn muốn duyệt bài đăng này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    databaseReference.child("view").setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateDelete.this, "Bài đăng đã được duyệt thành công!", Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            UpdateDelete.this.finish();
                        } else {
                            Toast.makeText(UpdateDelete.this, "Lỗi khi duyệt bài đăng!", Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Không", null)
                .show();
    }

    public void anhxa(){
        tv_ten = findViewById(R.id.tieude);
        tv_mota =findViewById(R.id.mota);
        tv_sdt = findViewById(R.id.phone);
        mtinh = findViewById(R.id.spiner_tinh);
        mhuyen = findViewById(R.id.spiner_huyen);
        tv_diachi= findViewById(R.id.diachi);
        tv_gia = findViewById(R.id.giatien);
        tv_dt = findViewById(R.id.dientich);
        mhinhthuc = findViewById(R.id.spiner_hinhthucdang);
        imageView = findViewById(R.id.img);
        update = findViewById(R.id.btnupdate);
        delete = findViewById(R.id.btnDelete);
        chontep = findViewById(R.id.btchontep);
        duyetbai = findViewById(R.id.btnDuyet);
    }
}
package com.example.finalapp;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteUser extends AppCompatActivity {
    private EditText txtNameUsers, txtEmails, txtLevel;
    private Button btnEdit, btnDelete;
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_user); // Layout để chỉnh sửa thông tin

        // Khởi tạo các view từ layout
        txtNameUsers = findViewById(R.id.txtnameusers);
        txtEmails = findViewById(R.id.txtemails);
        txtLevel = findViewById(R.id.txtlevel);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Khởi tạo Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Lấy dữ liệu từ Intent với kiểm tra null
        Intent intent = getIntent();
        String username = intent.getStringExtra("name") != null ? intent.getStringExtra("name") : "";
        String email = intent.getStringExtra("email") != null ? intent.getStringExtra("email") : "";
        int level = intent.getIntExtra("level", 3);

        // Kiểm tra email từ Intent
        if (email.isEmpty()) {
            Log.e(TAG, "Email from Intent is empty");
            Toast.makeText(this, "Không tìm thấy email user hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin user trong các EditText
        txtNameUsers.setText(username);
        txtEmails.setText(email);
        txtLevel.setText(String.valueOf(level));

        // Truy vấn Firebase để lấy userId dựa trên email
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userId = snapshot.getKey(); // Lấy userId từ key của node
                        Log.d(TAG, "Found userId: " + userId + " for email: " + email);
                        break; // Lấy node đầu tiên khớp
                    }
                } else {
                    Log.e(TAG, "No user found with email: " + email);
                    Toast.makeText(UpdateDeleteUser.this, "Không tìm thấy user với email này", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Firebase query cancelled: " + databaseError.getMessage());
                Toast.makeText(UpdateDeleteUser.this, "Lỗi truy vấn: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Gắn sự kiện cho nút "Chỉnh sửa" (lưu thông tin)
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = txtNameUsers.getText().toString().trim();
                String updatedEmail = txtEmails.getText().toString().trim();
                String updatedLevelStr = txtLevel.getText().toString().trim();

                if (!updatedName.isEmpty() && !updatedEmail.isEmpty()) {
                    if (!userId.isEmpty()) {
                        // Cập nhật thông tin user lên Firebase
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("name", updatedName);
                        updates.put("email", updatedEmail);
                        try {
                            int updatedLevel = updatedLevelStr.isEmpty() ? -1 : Integer.parseInt(updatedLevelStr);
                            if (updatedLevel != 0 && updatedLevel != 1) {
                                Toast.makeText(UpdateDeleteUser.this, "Level chỉ được là 0 (Admin) hoặc 1 (User)", Toast.LENGTH_SHORT).show();
                                return; // Dừng không cho cập nhật
                            }
                            updates.put("level", updatedLevel);
                        } catch (NumberFormatException e) {
                            Toast.makeText(UpdateDeleteUser.this, "Level phải là số nguyên 0 hoặc 1", Toast.LENGTH_SHORT).show();
                            return; // Dừng không cho cập nhật
                        }

                        databaseReference.child(userId).updateChildren(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(UpdateDeleteUser.this, "Đã cập nhật thông tin user", Toast.LENGTH_SHORT).show();

                                    // Trả kết quả về nếu cần
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("updatedName", updatedName);
                                    resultIntent.putExtra("updatedEmail", updatedEmail);
                                    resultIntent.putExtra("updatedLevel", updatedLevelStr.isEmpty() ? 1 : Integer.parseInt(updatedLevelStr));
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(UpdateDeleteUser.this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(UpdateDeleteUser.this, "Không tìm thấy ID user để cập nhật", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateDeleteUser.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gắn sự kiện cho nút "Xóa"
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateDeleteUser.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa user " + txtNameUsers.getText().toString() + "?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!userId.isEmpty()) {
                                    databaseReference.child(userId).removeValue()
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(UpdateDeleteUser.this, "Đã xóa user", Toast.LENGTH_SHORT).show();
                                                setResult(RESULT_OK);
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(UpdateDeleteUser.this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(UpdateDeleteUser.this, "Không tìm thấy ID user để xóa", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }
}

package com.example.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;



public class EditUser extends AppCompatActivity {
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_accout); // Layout đầu tiên hiển thị thông tin user
        mainLayout = findViewById(R.id.mainLayout);

        // Gắn sự kiện nhấn vào mainLayout để chuyển sang trang chỉnh sửa
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(EditUser.this, UpdateDeleteUser.class);
                startActivity(new Intent(EditUser.this, UpdateDeleteUser.class));
                // Sử dụng startActivityForResult để nhận kết quả trả về
            }
        });
    }


}

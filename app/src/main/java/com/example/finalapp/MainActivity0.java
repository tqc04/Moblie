//package com.example.finalapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity0 extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main0);
//        Thread bamgio=new Thread(){
//            public void run()
//            {
//                try {
//                    sleep(5000);
//                } catch (Exception e) {
//
//                }
//                finally
//                {
//                    Intent activitymoi=new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(activitymoi);
//                }
//            }
//        };
//        bamgio.start();
//    }
//    protected void onPause(){
//        super.onPause();
//        finish();
//    }
//}
package com.example.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity0 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        // Sử dụng Handler để trì hoãn việc chuyển đến MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent activitymoi = new Intent(MainActivity0.this, MainActivity.class);
                startActivity(activitymoi);
                finish(); // Kết thúc MainActivity0 để không quay lại
            }
        }, 5000); // Thời gian chờ 5 giây
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        // Không cần gọi finish() ở đây, chỉ cần để hoạt động tự động kết thúc khi chuyển đến MainActivity
    }
}

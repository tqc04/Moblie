package com.example.finalapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalapp.Admin;
import com.example.finalapp.MainActivity;
import com.example.finalapp.QLBV;
import com.example.finalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User extends Fragment {
    private Button bt_login;
    private RelativeLayout layoutNonLogin, layoutLogin;
    private TextView tv_name, email;
    private ImageView imgMenu;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private int userLevel = 1;
    private boolean isUserLevelFetched = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        // Khởi tạo các view
        layoutNonLogin = root.findViewById(R.id.layoutNonLogin);
        layoutLogin = root.findViewById(R.id.layoutLogin);
        tv_name = root.findViewById(R.id.tv_name);
        email = root.findViewById(R.id.email);
        bt_login = root.findViewById(R.id.button);
        imgMenu = root.findViewById(R.id.imgMenu);

        // Khởi tạo Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").keepSynced(true);
        // Xử lý sự kiện nút login
        bt_login.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });

        // Xử lý sự kiện menu
        imgMenu.setOnClickListener(v -> {
            if (user != null && !isUserLevelFetched) {
                Toast.makeText(getContext(), "Đang tải thông tin người dùng, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                checkUserPermission(() -> showPopupMenu(v));
            } else {
                showPopupMenu(v);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Chỉ gọi updateUI() nếu user thay đổi
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != user) {
            updateUI();
        }
    }

    private void updateUI() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            layoutNonLogin.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            tv_name.setText("");
            email.setText("");
            userLevel = 1;
            isUserLevelFetched = true;
        } else {
            layoutNonLogin.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
            email.setText(user.getEmail());
            // Gọi checkUserPermission để lấy userLevel
            checkUserPermission(null);
        }
    }

    private void checkUserPermission(Runnable callback) {
        if (user == null) {
            userLevel = 1; // Mặc định là user thường
            tv_name.setText("");
            isUserLevelFetched = true;
            if (callback != null) callback.run();
            return;
        }

        Log.d("UserFragment", "Fetching data for UID: " + user.getUid());
        databaseReference.child("Users").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("UserFragment", "DataSnapshot exists: " + dataSnapshot.exists());
                        if (dataSnapshot.exists()) {
                            Long level = dataSnapshot.child("level").getValue(Long.class);
                            Log.d("UserFragment", "Raw level value: " + level);
                            userLevel = (level != null && (level == 0 || level == 1)) ? level.intValue() : 1;
                            // Lấy thêm thông tin nếu có
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String address = dataSnapshot.child("address").getValue(String.class);
                            tv_name.setText(name != null ? name : user.getEmail());
                        } else {
                            userLevel = 1;
                            tv_name.setText(user.getEmail());
                            Log.d("UserFragment", "No data at users/" + user.getUid());
                        }
                        isUserLevelFetched = true;
                        Log.d("UserFragment", "userLevel fetched: " + userLevel);
                        if (callback != null) callback.run();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        userLevel = 1;
                        tv_name.setText(user.getEmail());
                        isUserLevelFetched = true;
                        Toast.makeText(getContext(), "Error fetching user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UserFragment", "Database error: " + databaseError.getMessage());
                        if (callback != null) callback.run();
                    }
                });
    }

//    private void showPopupMenu(View v) {
//        if (getContext() == null) return;
//        PopupMenu popupMenu = new PopupMenu(getContext(), v);
//        Log.d("UserFragment", "Showing menu for userLevel: " + userLevel);
//
//        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());
//
//        if (userLevel == 1) {
//            // Ẩn 2 mục không dành cho user thường
//            popupMenu.getMenu().findItem(R.id.admin).setVisible(false);
//            popupMenu.getMenu().findItem(R.id.qlbv).setVisible(false);
//        }
//
//
//        popupMenu.setOnMenuItemClickListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.admin) {
//                startActivity(new Intent(getActivity(), Admin.class));
//                return true;
//            } else if (id == R.id.qlbv) {
//                startActivity(new Intent(getActivity(), QLBV.class));
//                return true;
//            } else if (id == R.id.logout) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(getActivity(), MainActivity.class));
//                return true;
//            }
//            return false;
//        });
//        popupMenu.show();
//    }
private void showPopupMenu(View v) {
    if (getContext() == null) return;
    PopupMenu popupMenu = new PopupMenu(getContext(), v);
    Log.d("UserFragment", "Showing menu for userLevel: " + userLevel);

    // Xóa tất cả các mục cũ
    popupMenu.getMenu().clear();

    if (userLevel == 0) {
        // Menu cho admin
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());
    } else {
        // Chỉ thêm mục Đăng xuất cho user thường
        popupMenu.getMenu().add(0, R.id.logout, 0, "Đăng xuất");
    }

    popupMenu.setOnMenuItemClickListener(item -> {
        int id = item.getItemId();
        if (id == R.id.admin) {
            startActivity(new Intent(getActivity(), Admin.class));
            return true;
        } else if (id == R.id.qlbv) {
            startActivity(new Intent(getActivity(), QLBV.class));
            return true;
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            return true;
        }
        return false;
    });
    popupMenu.show();
}
}
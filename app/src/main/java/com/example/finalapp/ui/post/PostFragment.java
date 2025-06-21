//package com.example.finalapp.ui.post;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.MimeTypeMap;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//
//import com.example.finalapp.PostActivity;
//import com.example.finalapp.R;
//import com.example.finalapp.model.BaiDang;
//import com.example.finalapp.model.Motel;
//import com.example.finalapp.model.QuanHuyen;
//import com.example.finalapp.model.TinhTP;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.content.Context.MODE_PRIVATE;
//
//public class PostFragment extends Fragment implements PaymentMethodDialog.PaymentMethodListener {
//
//    private static final int RESULT_LOAD_IMAGE = 1;
//    private static final int PAYPAL_REQUEST_CODE = 7171;
//    ArrayAdapter<TinhTP> adapter_Tinh;
//    ArrayAdapter<QuanHuyen> adapter_QH;
//    String amount = "";
//    EditText ten, mota, sdtlh, diachi, giatien, songay, dientich;
//    Spinner gioitinh, hinhthuc, loaitin, loaingay, tinh, huyen;
//    Button chontep, post;
//    ImageView imageView;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
//    StorageReference storageReference;
//    private Uri mImageUri;
//
//    String strEmail;
//
//    private ActivityResultLauncher<Intent> imagePickerLauncher;
//    private ActivityResultLauncher<Intent> postActivityLauncher;
//
//
//
//
//
//
//    @Override
//    public void onCreate(@NonNull Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Initialize Firebase Storage
//        storageReference = FirebaseStorage.getInstance().getReference("images");
//
//        // Register image picker launcher
//        imagePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                        mImageUri = result.getData().getData();
//                        if (mImageUri != null) {
//                            Picasso.get().load(mImageUri).into(imageView);
//                        }
//                    }
//                }
//        );
//
//        // Register post activity launcher
//        postActivityLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        if (data != null) {
//                            handlePostSuccess(data);
//                        }
//                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
//                        Toast.makeText(getContext(), "Đăng bài đã bị hủy", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//    }
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_post, container, false);
//        anhxa(root);
//        actionSql();
//        actionImage();
//        actionPost();
//        return root;
//    }
//
//    private void actionImage() {
//        chontep.setOnClickListener(v -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
//                }
//            } else {
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                }
//            }
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            imagePickerLauncher.launch(intent);
//        });
//    }
//
//    private String getImage(Uri uri) {
//        ContentResolver contentResolver = requireActivity().getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        String ext = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//        if (ext == null) return "jpg";
//        return ext;
//    }
//
//    public int checkLogin(){
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_FILE", MODE_PRIVATE);
//        boolean chk = sharedPreferences.getBoolean("LOGIN", false);
//        if (chk) {
//            strEmail = sharedPreferences.getString("EMAIL", "");
//            return 1;
//        }
//        return -1;
//    }
//
//    private void actionPost() {
//        post.setOnClickListener(v -> {
//            if (validateForm()) {
//                showPaymentMethodDialog();
//            }
//        });
//    }
//
//    private boolean validateForm() {
//        if (TextUtils.isEmpty(ten.getText().toString())) {
//            Toast.makeText(getActivity(), "Vui lòng nhập tiêu đề!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtils.isEmpty(mota.getText().toString())) {
//            Toast.makeText(getActivity(), "Vui lòng nhập mô tả!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtils.isEmpty(sdtlh.getText().toString())) {
//            Toast.makeText(getActivity(), "Vui lòng nhập Sđt liên hệ!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtils.isEmpty(diachi.getText().toString())) {
//            Toast.makeText(getActivity(), "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtils.isEmpty(giatien.getText().toString())) {
//            Toast.makeText(getActivity(), "Vui lòng nhập giá tiền!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtils.isEmpty(songay.getText().toString())) {
//            Toast.makeText(getActivity(), "Vui lòng nhập số ngày!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (mImageUri == null) {
//            Toast.makeText(getActivity(), "Bạn chưa chọn ảnh, vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//
//    private void showPaymentMethodDialog() {
//        PaymentMethodDialog dialog = new PaymentMethodDialog();
//        dialog.setPaymentMethodListener(this);
//        dialog.show(getChildFragmentManager(), "payment_method");
//    }
//
//    @Override
//    public void onPaymentMethodSelected(boolean isPayPal) {
//        if (isPayPal) {
//            String st_loaingay = loaingay.getSelectedItem().toString();
//            int ngay = 1;
//            int vnd = 1;
//            if (st_loaingay.equalsIgnoreCase("/Ngày")) {
//                ngay = 1;
//                vnd = 3000;
//            } else if (st_loaingay.equalsIgnoreCase("/Tuần")) {
//                ngay = 7;
//                vnd = 20000;
//            } else if (st_loaingay.equalsIgnoreCase("/Tháng")) {
//                ngay = 30;
//                vnd = 60000;
//            }
//
//            int tmp = Integer.parseInt(songay.getText().toString());
//            vnd = vnd * tmp;
//
//            Intent intent = new Intent(getActivity(), PostActivity.class);
//            intent.putExtra("amount", vnd);
//            intent.putExtra("payment_method", "paypal");
//            postActivityLauncher.launch(intent);
//        } else {
//            String base64Image = "";
//            if (mImageUri != null) {
//                try {
//                    base64Image = encodeImageToBase64(mImageUri);
//                } catch (IOException e) {
//                    Toast.makeText(getActivity(), "Lỗi khi mã hóa ảnh: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//            savePostToFirebase(base64Image);
//        }
//    }
//
//    private void uploadPost(boolean isCashPayment) {
//        String tenStr = ten.getText().toString().trim();
//        String motaStr = mota.getText().toString().trim();
//        String diachiStr = diachi.getText().toString().trim();
//        String sdtlhStr = sdtlh.getText().toString().trim();
//        String giatienStr = giatien.getText().toString().trim();
//        String dientichStr = dientich.getText().toString().trim();
//        String tinhStr = tinh.getSelectedItem() != null ? tinh.getSelectedItem().toString() : "";
//        String huyenStr = huyen.getSelectedItem() != null ? huyen.getSelectedItem().toString() : "";
//        String gioitinhStr = gioitinh.getSelectedItem() != null ? gioitinh.getSelectedItem().toString() : "";
//        String hinhthucStr = hinhthuc.getSelectedItem() != null ? hinhthuc.getSelectedItem().toString() : "";
//        String loaitinStr = loaitin.getSelectedItem() != null ? loaitin.getSelectedItem().toString() : "";
//        String loaingayStr = loaingay.getSelectedItem() != null ? loaingay.getSelectedItem().toString() : "";
//        String songayStr = songay.getText().toString().trim();
//
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("DangBai");
//
//        if (mImageUri != null) {
//            try {
//                File file = getFileFromUri(mImageUri);
//                Uri fileUri = Uri.fromFile(file);
//                StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getImage(mImageUri));
//                storage.putFile(fileUri)
//                        .addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl().addOnSuccessListener(uri -> {
//                            String imageUrl = uri.toString();
//                            BaiDang baiDang = new BaiDang(
//                                    tenStr,
//                                    motaStr,
//                                    sdtlhStr,
//                                    diachiStr,
//                                    giatienStr,
//                                    dientichStr,
//                                    songayStr,
//                                    gioitinhStr,
//                                    huyenStr,
//                                    tinhStr,
//                                    loaingayStr,
//                                    hinhthucStr,
//                                    loaitinStr,
//                                    imageUrl
//                            );
//                            dbRef.push().setValue(baiDang)
//                                    .addOnSuccessListener(aVoid -> {
//                                        Toast.makeText(getActivity(), "Đăng bài thành công", Toast.LENGTH_LONG).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Toast.makeText(getActivity(), "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                    });
//                        }))
//                        .addOnFailureListener(e -> {
//                            Toast.makeText(getActivity(), "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        });
//            } catch (IOException e) {
//                Toast.makeText(getActivity(), "Lỗi khi lấy file từ URI: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        } else {
//            BaiDang baiDang = new BaiDang(
//                    tenStr,
//                    motaStr,
//                    sdtlhStr,
//                    diachiStr,
//                    giatienStr,
//                    dientichStr,
//                    songayStr,
//                    gioitinhStr,
//                    huyenStr,
//                    tinhStr,
//                    loaingayStr,
//                    hinhthucStr,
//                    loaitinStr,
//                    ""
//            );
//            dbRef.push().setValue(baiDang)
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(getActivity(), "Đăng bài thành công (không có ảnh)", Toast.LENGTH_LONG).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(getActivity(), "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    });
//        }
//    }
//
//    public void actionSql() {
//        DatabaseReference tinhRef = FirebaseDatabase.getInstance().getReference("TinhTP");
//        tinhRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<TinhTP> tinhTPS = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    TinhTP tp = dataSnapshot.getValue(TinhTP.class);
//                    tinhTPS.add(tp);
//                }
//                adapter_Tinh = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tinhTPS);
//                tinh.setAdapter(adapter_Tinh);
//                tinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        TinhTP tinhTP1 = (TinhTP) parent.getAdapter().getItem(position);
//                        int idtinh = tinhTP1.getId();
//                        DatabaseReference huyenRef = FirebaseDatabase.getInstance().getReference("QuanHuyen").child(String.valueOf(idtinh));
//                        huyenRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                List<QuanHuyen> quanHuyens = new ArrayList<>();
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    QuanHuyen qh = dataSnapshot.getValue(QuanHuyen.class);
//                                    quanHuyens.add(qh);
//                                }
//                                adapter_QH = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, quanHuyens);
//                                huyen.setAdapter(adapter_QH);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {}
//                        });
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {}
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void anhxa(View root) {
//        ten = root.findViewById(R.id.ten);
//        mota = root.findViewById(R.id.tuoi);
//        sdtlh = root.findViewById(R.id.sdtlh);
//        diachi = root.findViewById(R.id.diachi);
//        giatien = root.findViewById(R.id.giatien);
//        songay = root.findViewById(R.id.songay);
//        dientich = root.findViewById(R.id.dientich);
//        gioitinh = root.findViewById(R.id.spiner_gioitinh);
//        hinhthuc = root.findViewById(R.id.spiner_hinhthucdang);
//        loaitin = root.findViewById(R.id.spiner_loaitin);
//        loaingay = root.findViewById(R.id.spiner_loaingay);
//        tinh = root.findViewById(R.id.spiner_tinh);
//        huyen = root.findViewById(R.id.spiner_huyen);
//
//        chontep = root.findViewById(R.id.btchontep);
//        post = root.findViewById(R.id.btluu);
//        imageView = root.findViewById(R.id.imv);
//    }
//
//
//    private File getFileFromUri(Uri uri) throws IOException {
//        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
//        File tempFile = File.createTempFile("upload", ".jpg", getContext().getCacheDir());
//        OutputStream outputStream = new FileOutputStream(tempFile);
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            outputStream.write(buffer, 0, length);
//        }
//        outputStream.close();
//        inputStream.close();
//        return tempFile;
//    }
//
//    private void handlePostSuccess(Intent data) {
//        String paymentId = data.getStringExtra("payment_id");
//        String paymentState = data.getStringExtra("payment_state");
//        boolean isCashPayment = data.getBooleanExtra("is_cash_payment", true);
//
//        if (isCashPayment) {
//            Toast.makeText(getContext(), "Đăng bài thành công - Thanh toán tiền mặt", Toast.LENGTH_SHORT).show();
//        } else {
//            String message = "Đăng bài thành công - Thanh toán PayPal\n" +
//                    "Payment ID: " + paymentId + "\n" +
//                    "Trạng thái: " + paymentState;
//            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private String encodeImageToBase64(Uri imageUri) throws IOException {
//        ContentResolver contentResolver = getContext().getContentResolver();
//        InputStream inputStream = contentResolver.openInputStream(imageUri);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            byteArrayOutputStream.write(buffer, 0, length);
//        }
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        inputStream.close();
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//    private void savePostToFirebase(String base64Image) {
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("DangBai");
//        // thêm tối 8/6.
//
//        long now = System.currentTimeMillis(); // Thời điểm hiện tại
//// Ví dụ: người dùng chọn thời gian đăng bài
//        String ln = (String) loaingay.getSelectedItem();
//        int sn = Integer.parseInt(songay.getText().toString());
//        Log.d("TIME_CHECK", "Số ngày: " + sn+"  loại: "+ln);
//        Log.d("TIME_CHECK", "Now1: " + now);
//        long durationMillis = 0;
//        if(ln.equalsIgnoreCase("/Ngày")){
//             durationMillis = (long) sn * 24 * 60 * 60 * 1000;
//            Log.d("TIME_CHECK", "durationMillis: Ngày" + durationMillis);
//
//        }if(ln.equalsIgnoreCase("/Tuần")){
//             durationMillis = (long) sn * 7 * 24 * 60 * 60 * 1000;
//            Log.d("TIME_CHECK", "durationMillis:Tuần " + durationMillis);
//
//        }if (ln.equalsIgnoreCase("/Tháng")) {
//             durationMillis = (long) sn * 30 * 24 * 60 * 60 * 1000;
//            Log.d("TIME_CHECK", "durationMillis: Tháng " + durationMillis);
//
//        }
//        long expirationTimestamp = (long)now + durationMillis;
//
//        Log.d("TIME_CHECK", "Expiration1: " + expirationTimestamp);
//
//// Sau đó gán vào bài đăng
//
//
//        BaiDang baiDang = new BaiDang(
//                ten.getText().toString(),
//                mota.getText().toString(),
//                sdtlh.getText().toString(),
//                diachi.getText().toString(),
//                giatien.getText().toString(),
//                dientich.getText().toString(),
//                songay.getText().toString(),
//                gioitinh.getSelectedItem() != null ? gioitinh.getSelectedItem().toString() : "",
//                huyen.getSelectedItem() != null ? huyen.getSelectedItem().toString() : "",
//                tinh.getSelectedItem() != null ? tinh.getSelectedItem().toString() : "",
//                loaingay.getSelectedItem() != null ? loaingay.getSelectedItem().toString() : "",
//                hinhthuc.getSelectedItem() != null ? hinhthuc.getSelectedItem().toString() : "",
//                loaitin.getSelectedItem() != null ? loaitin.getSelectedItem().toString() : "",
//                base64Image,
//                expirationTimestamp
//        );
//        dbRef.push().setValue(baiDang)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(getActivity(), "Đăng bài thành công", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getActivity(), "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//
//
//    private Bitmap decodeBase64ToBitmap(String base64Image) {
//        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//    }
//
//
//
//
//
//}

package com.example.finalapp.ui.post;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.finalapp.PayPalPaymentActivity;
import com.example.finalapp.PostActivity;
import com.example.finalapp.R;
import com.example.finalapp.model.BaiDang;
import com.example.finalapp.model.LocationUtil;
import com.example.finalapp.model.Motel;
import com.example.finalapp.model.QuanHuyen;
import com.example.finalapp.model.TinhTP;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class PostFragment extends Fragment implements PaymentMethodDialog.PaymentMethodListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    ArrayAdapter<TinhTP> adapter_Tinh;
    ArrayAdapter<QuanHuyen> adapter_QH;
    String amount = "";
    EditText ten, mota, sdtlh, diachi, giatien, songay, dientich;
    Spinner gioitinh, hinhthuc, loaitin, loaingay, tinh, huyen;
    Button chontep, post;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private Uri mImageUri;

    String strEmail;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> postActivityLauncher;

    TextView tvFeeVnd;
    private double exchangeRate = 25000; // Có thể lấy từ API hoặc cấu hình

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference("images");

        // Register image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        mImageUri = result.getData().getData();
                        if (mImageUri != null) {
                            Picasso.get().load(mImageUri).into(imageView);
                        }
                    }
                }
        );

        // Register post activity launcher
        postActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            handlePostSuccess(data);
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(getContext(), "Đăng bài đã bị hủy", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post, container, false);
        anhxa(root);
        actionSql();
        actionImage();
        actionPost();
        return root;
    }

    private void actionImage() {
        chontep.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
                }
            } else {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            imagePickerLauncher.launch(intent);
        });
    }

    private String getImage(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String ext = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        if (ext == null) return "jpg";
        return ext;
    }

    public int checkLogin(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_FILE", MODE_PRIVATE);
        boolean chk = sharedPreferences.getBoolean("LOGIN", false);
        if (chk) {
            strEmail = sharedPreferences.getString("EMAIL", "");
            return 1;
        }
        return -1;
    }

    private void actionPost() {
        post.setOnClickListener(v -> {
            if (validateForm()) {
                showPaymentMethodDialog();
            }
        });
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(ten.getText().toString())) {
            Toast.makeText(getActivity(), "Vui lòng nhập tiêu đề!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mota.getText().toString())) {
            Toast.makeText(getActivity(), "Vui lòng nhập mô tả!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(sdtlh.getText().toString())) {
            Toast.makeText(getActivity(), "Vui lòng nhập Sđt liên hệ!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(diachi.getText().toString())) {
            Toast.makeText(getActivity(), "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(giatien.getText().toString())) {
            Toast.makeText(getActivity(), "Vui lòng nhập giá tiền!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(songay.getText().toString())) {
            Toast.makeText(getActivity(), "Vui lòng nhập số ngày!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mImageUri == null) {
            Toast.makeText(getActivity(), "Bạn chưa chọn ảnh, vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void showPaymentMethodDialog() {
        PaymentMethodDialog dialog = new PaymentMethodDialog();
        dialog.setPaymentMethodListener(this);
        dialog.show(getChildFragmentManager(), "payment_method");
    }

    @Override
    public void onPaymentMethodSelected(boolean isPayPal) {
        String st_loaingay = loaingay.getSelectedItem().toString();
        int vnd = 1;
        if (st_loaingay.equalsIgnoreCase("/Ngày")) {
            vnd = 30000;
        } else if (st_loaingay.equalsIgnoreCase("/Tuần")) {
            vnd = 200000;
        } else if (st_loaingay.equalsIgnoreCase("/Tháng")) {
            vnd = 600000;
        }
        int tmp = 1;
        if (!TextUtils.isEmpty(songay.getText().toString())) {
            tmp = Integer.parseInt(songay.getText().toString());
        }
        int totalVnd = vnd * tmp;
        if (isPayPal) {
            double usdAmount = totalVnd / 25000.0; // hoặc 250000.0 nếu bạn muốn
            createPayPalOrder(usdAmount);
        } else {
            String base64Image = "";
            if (mImageUri != null) {
                try {
                    base64Image = encodeImageToBase64(mImageUri);
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Lỗi khi mã hóa ảnh: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            savePostToFirebaseWithVndPrice(base64Image, totalVnd);
        }
    }

    private void uploadPost(boolean isCashPayment) {
        String tenStr = ten.getText().toString().trim();
        String motaStr = mota.getText().toString().trim();
        String diachiStr = diachi.getText().toString().trim();
        String sdtlhStr = sdtlh.getText().toString().trim();
        String giatienStr = giatien.getText().toString().trim();
        String dientichStr = dientich.getText().toString().trim();
        String tinhStr = tinh.getSelectedItem() != null ? tinh.getSelectedItem().toString() : "";
        String huyenStr = huyen.getSelectedItem() != null ? huyen.getSelectedItem().toString() : "";
        String gioitinhStr = gioitinh.getSelectedItem() != null ? gioitinh.getSelectedItem().toString() : "";
        String hinhthucStr = hinhthuc.getSelectedItem() != null ? hinhthuc.getSelectedItem().toString() : "";
        String loaitinStr = loaitin.getSelectedItem() != null ? loaitin.getSelectedItem().toString() : "";
        String loaingayStr = loaingay.getSelectedItem() != null ? loaingay.getSelectedItem().toString() : "";
        String songayStr = songay.getText().toString().trim();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("DangBai");

        if (mImageUri != null) {
            try {
                File file = getFileFromUri(mImageUri);
                Uri fileUri = Uri.fromFile(file);
                StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getImage(mImageUri));
                storage.putFile(fileUri)
                        .addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            BaiDang baiDang = new BaiDang(
                                    tenStr,
                                    motaStr,
                                    sdtlhStr,
                                    diachiStr,
                                    giatienStr,
                                    dientichStr,
                                    songayStr,
                                    gioitinhStr,
                                    huyenStr,
                                    tinhStr,
                                    loaingayStr,
                                    hinhthucStr,
                                    loaitinStr,
                                    imageUrl
                            );
                            dbRef.push().setValue(baiDang)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getActivity(), "Đăng bài thành công", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getActivity(), "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }))
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Lỗi khi lấy file từ URI: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            BaiDang baiDang = new BaiDang(
                    tenStr,
                    motaStr,
                    sdtlhStr,
                    diachiStr,
                    giatienStr,
                    dientichStr,
                    songayStr,
                    gioitinhStr,
                    huyenStr,
                    tinhStr,
                    loaingayStr,
                    hinhthucStr,
                    loaitinStr,
                    ""
            );
            dbRef.push().setValue(baiDang)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Đăng bài thành công (không có ảnh)", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
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
                            public void onCancelled(@NonNull DatabaseError error) {}
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

    private void anhxa(View root) {
        ten = root.findViewById(R.id.ten);
        mota = root.findViewById(R.id.tuoi);
        sdtlh = root.findViewById(R.id.sdtlh);
        diachi = root.findViewById(R.id.diachi);
        giatien = root.findViewById(R.id.giatien);
        songay = root.findViewById(R.id.songay);
        dientich = root.findViewById(R.id.dientich);
        gioitinh = root.findViewById(R.id.spiner_gioitinh);
        hinhthuc = root.findViewById(R.id.spiner_hinhthucdang);
        loaitin = root.findViewById(R.id.spiner_loaitin);
        loaingay = root.findViewById(R.id.spiner_loaingay);
        tinh = root.findViewById(R.id.spiner_tinh);
        huyen = root.findViewById(R.id.spiner_huyen);

        chontep = root.findViewById(R.id.btchontep);
        post = root.findViewById(R.id.btluu);
        imageView = root.findViewById(R.id.imv);

        // Gọi cập nhật phí khi nhập liệu
        giatien.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override public void afterTextChanged(Editable s) {}
        });
        songay.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {  }
            @Override public void afterTextChanged(Editable s) {}
        });
        loaingay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private File getFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("upload", ".jpg", getContext().getCacheDir());
        OutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
        inputStream.close();
        return tempFile;
    }

    private void handlePostSuccess(Intent data) {
        String paymentId = data.getStringExtra("payment_id");
        String paymentState = data.getStringExtra("payment_state");
        boolean isCashPayment = data.getBooleanExtra("is_cash_payment", true);

        if (isCashPayment) {
            Toast.makeText(getContext(), "Đăng bài thành công - Thanh toán tiền mặt", Toast.LENGTH_SHORT).show();
        } else {
            String message = "Đăng bài thành công - Thanh toán PayPal\n" +
                    "Payment ID: " + paymentId + "\n" +
                    "Trạng thái: " + paymentState;
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private String encodeImageToBase64(Uri imageUri) throws IOException {
        ContentResolver contentResolver = getContext().getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(imageUri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        inputStream.close();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void savePostToFirebaseWithVndPrice(String base64Image, int vndPrice) {
        String tenStr = ten.getText().toString().trim();
        String motaStr = mota.getText().toString().trim();
        String diachiStr = diachi.getText().toString().trim();
        String sdtlhStr = sdtlh.getText().toString().trim();
        String dientichStr = dientich.getText().toString().trim();
        String tinhStr = tinh.getSelectedItem() != null ? tinh.getSelectedItem().toString() : "";
        String huyenStr = huyen.getSelectedItem() != null ? huyen.getSelectedItem().toString() : "";
        String gioitinhStr = gioitinh.getSelectedItem() != null ? gioitinh.getSelectedItem().toString() : "";
        String hinhthucStr = hinhthuc.getSelectedItem() != null ? hinhthuc.getSelectedItem().toString() : "";
        String loaitinStr = loaitin.getSelectedItem() != null ? loaitin.getSelectedItem().toString() : "";
        String loaingayStr = loaingay.getSelectedItem() != null ? loaingay.getSelectedItem().toString() : "";
        String songayStr = songay.getText().toString().trim();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("DangBai");
        String fullAddress = diachi.getText().toString() + ", " + huyen.getSelectedItem().toString() + ", " + tinh.getSelectedItem().toString();
        LatLng latLng = LocationUtil.getLocationFromAddress(getContext(), fullAddress);
        String lat = latLng != null ? String.valueOf(latLng.latitude) : "";
        String lng = latLng != null ? String.valueOf(latLng.longitude) : "";
        long now = System.currentTimeMillis(); // Thời điểm hiện tại
// Ví dụ: người dùng chọn thời gian đăng bài
        String ln = (String) loaingay.getSelectedItem();
        int sn = Integer.parseInt(songay.getText().toString());
        Log.d("TIME_CHECK", "Số ngày: " + sn+"  loại: "+ln);
        Log.d("TIME_CHECK", "Now1: " + now);
        long durationMillis = 0;
        if(ln.equalsIgnoreCase("/Ngày")){
             durationMillis = (long) sn * 24 * 60 * 60 * 1000;
        }else if(ln.equalsIgnoreCase("/Tuần")){
             durationMillis = (long) sn * 7 * 24 * 60 * 60 * 1000;
        } else if (ln.equalsIgnoreCase("/Tháng")) {
             durationMillis = (long) sn * 30 * 24 * 60 * 60 * 1000;
        }
        long expirationTimestamp = now + durationMillis;
        Log.d("TIME_CHECK", "Expiration1: " + expirationTimestamp);

        BaiDang baiDang = new BaiDang(
                tenStr,
                motaStr,
                sdtlhStr,
                diachiStr,
                giatien.getText().toString(),
                dientichStr,
                songayStr,
                gioitinhStr,
                huyenStr,
                tinhStr,
                loaingayStr,
                hinhthucStr,
                loaitinStr,
                base64Image,
                expirationTimestamp,
                false,
                lat,
                lng

        );
        dbRef.push().setValue(baiDang)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Đăng bài thành công", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void createPayPalOrder(double usdAmount) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("amount", String.format("%.2f", usdAmount));
            json.put("currency", "USD");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toString()
        );
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/paypal/create-order")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Lỗi kết nối server PayPal", Toast.LENGTH_SHORT).show()
                );
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject obj = new JSONObject(responseBody);
                    JSONArray links = obj.getJSONArray("links");
                    String approvalUrl = null;
                    for (int i = 0; i < links.length(); i++) {
                        JSONObject link = links.getJSONObject(i);
                        Log.d("ApprovalUrl", "link : " + link);
                        if ("approve".equals(link.getString("rel"))) {
                            approvalUrl = link.getString("href");
                            break;
                        }
                    }
                    Log.d("ApprovalUrl", "approvalUrl: " + approvalUrl);

                    if (approvalUrl != null) {
                        String finalApprovalUrl = approvalUrl;
                        requireActivity().runOnUiThread(() -> openPayPalWebView(finalApprovalUrl));
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Không lấy được link thanh toán PayPal", Toast.LENGTH_SHORT).show()
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openPayPalWebView(String url) {
        Intent intent = new Intent(getContext(), com.example.finalapp.ui.post.PayPalWebViewActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            // Thanh toán thành công, cho phép lưu bài đăng lên Firebase
            String base64Image = "";
            if (mImageUri != null) {
                try {
                    base64Image = encodeImageToBase64(mImageUri);
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Lỗi khi mã hóa ảnh: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            // Tính lại giá VNĐ để lưu
            String st_loaingay = loaingay.getSelectedItem().toString();
            int vnd = 1;
            if (st_loaingay.equalsIgnoreCase("/Ngày")) {
                vnd = 30000;
            } else if (st_loaingay.equalsIgnoreCase("/Tuần")) {
                vnd = 200000;
            } else if (st_loaingay.equalsIgnoreCase("/Tháng")) {
                vnd = 600000;
            }
            int tmp = 1;
            if (!TextUtils.isEmpty(songay.getText().toString())) {
                tmp = Integer.parseInt(songay.getText().toString());
            }
            int totalVnd = vnd * tmp;
            savePostToFirebaseWithVndPrice(base64Image, totalVnd);
        }
    }

    private void updateFeeVnd() {
        try {
            String st_loaingay = loaingay.getSelectedItem() != null ? loaingay.getSelectedItem().toString() : "/Ngày";
            int vnd = 0;
            if (st_loaingay.equalsIgnoreCase("/Ngày")) {
                vnd = 30000;
            } else if (st_loaingay.equalsIgnoreCase("/Tuần")) {
                vnd = 200000;
            } else if (st_loaingay.equalsIgnoreCase("/Tháng")) {
                vnd = 600000;
            }
            int tmp = 1;
            if (!TextUtils.isEmpty(songay.getText().toString())) {
                tmp = Integer.parseInt(songay.getText().toString());
            }
            int totalVnd = vnd * tmp;
            tvFeeVnd.setText("Phí đăng bài: " + totalVnd + " VNĐ");
        } catch (Exception e) {
            tvFeeVnd.setText("Phí đăng bài: 0 VNĐ");
        }
    }

}


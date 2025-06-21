package com.example.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapp.model.Motel;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE = 123;
    private PayPalConfiguration paypalConfig;
    private BigDecimal amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Initialize PayPal configuration
        paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfig.PAYPAL_ENVIRONMENT)
            .clientId(PayPalConfig.getClientId());

        // Start PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);

        // Find the submit button
        Button submitButton = findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(v -> handleSubmission());
    }

    private void handleSubmission() {
        // Validate form fields
        if (!validateForm()) {
            return;
        }

        // Calculate amount based on your pricing logic
        amount = calculateAmount();

        // Show payment method dialog
        PaymentMethodDialog dialog = new PaymentMethodDialog(this, isPayPal -> {
            if (isPayPal) {
                // Start PayPal payment immediately
                startPayPalPayment();
            } else {
                // Handle cash payment
                returnResult(true, null, null);
            }
        });
        dialog.show();
    }

    private void startPayPalPayment() {
        try {
            // Create payment with amount and currency
            PayPalPayment payment = new PayPalPayment(
                amount,
                PayPalConfig.CURRENCY_VND,
                getString(R.string.title_post),
                PayPalConfig.INTENT_SALE
            );

            // Create intent for PayPal payment
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            
            // Start PayPal payment activity
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khởi tạo thanh toán: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        // Get payment details
                        String paymentId = confirmation.getProofOfPayment().getPaymentId();
                        String paymentState = confirmation.getProofOfPayment().getState();
                        
                        // Save post data to Firebase
                        savePostToFirebase(paymentId);
                        
                        // Return result to fragment
                        returnResult(false, paymentId, paymentState);
                    } catch (Exception e) {
                        Toast.makeText(this, "Lỗi xử lý thanh toán: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Payment canceled by user
                Toast.makeText(this, "Thanh toán đã bị hủy", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Invalid payment / configuration
                Toast.makeText(this, "Cấu hình thanh toán không hợp lệ", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    private void savePostToFirebase(String imageUrl) {
        // Lấy dữ liệu từ form
        EditText tieudeInput = findViewById(R.id.etTieude);
        EditText motaInput = findViewById(R.id.etMota);
        EditText priceInput = findViewById(R.id.etPrice);
        EditText phoneInput = findViewById(R.id.etPhone);
        EditText addressInput = findViewById(R.id.etAddress);
        EditText areaInput = findViewById(R.id.etArea);
        EditText tinhInput = findViewById(R.id.etTinh);
        EditText huyenInput = findViewById(R.id.etHuyen);

        // Tạo object Motel đúng constructor
        Motel motel = new Motel(
            imageUrl,
            tieudeInput.getText().toString(),
            motaInput.getText().toString(),
            addressInput.getText().toString(),
            tieudeInput.getText().toString(),
            tinhInput.getText().toString(),
            huyenInput.getText().toString(),
            phoneInput.getText().toString(),
            priceInput.getText().toString(),
            areaInput.getText().toString()
        );

        // Lưu vào Firebase Database
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("DangBai");
        postsRef.push().setValue(motel)
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Đăng bài thành công", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Lỗi đăng bài: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void returnResult(boolean isCashPayment, String paymentId, String paymentState) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("is_cash_payment", isCashPayment);
        if (!isCashPayment) {
            resultIntent.putExtra("payment_id", paymentId);
            resultIntent.putExtra("payment_state", paymentState);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private BigDecimal calculateAmount() {
        EditText daysInput = findViewById(R.id.etDays);
        EditText priceInput = findViewById(R.id.etPrice);
        
        int days = Integer.parseInt(daysInput.getText().toString());
        double pricePerDay = Double.parseDouble(priceInput.getText().toString());
        
        return new BigDecimal(days * pricePerDay);
    }

    private boolean validateForm() {
        EditText daysInput = findViewById(R.id.etDays);
        EditText priceInput = findViewById(R.id.etPrice);
        EditText phoneInput = findViewById(R.id.etPhone);
        EditText addressInput = findViewById(R.id.etAddress);
        EditText areaInput = findViewById(R.id.etArea);
        
        // Reset errors
        daysInput.setError(null);
        priceInput.setError(null);
        phoneInput.setError(null);
        addressInput.setError(null);
        areaInput.setError(null);
        
        // Check empty fields
        if (daysInput.getText().toString().trim().isEmpty()) {
            daysInput.setError("Vui lòng nhập số ngày");
            return false;
        }
        
        if (priceInput.getText().toString().trim().isEmpty()) {
            priceInput.setError("Vui lòng nhập giá phòng");
            return false;
        }

        if (phoneInput.getText().toString().trim().isEmpty()) {
            phoneInput.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        if (addressInput.getText().toString().trim().isEmpty()) {
            addressInput.setError("Vui lòng nhập địa chỉ");
            return false;
        }

        if (areaInput.getText().toString().trim().isEmpty()) {
            areaInput.setError("Vui lòng nhập diện tích");
            return false;
        }
        
        // Validate phone number format
        String phone = phoneInput.getText().toString().trim();
        if (!phone.matches("^[0-9]{10,11}$")) {
            phoneInput.setError("Số điện thoại không hợp lệ");
            return false;
        }
        
        try {
            int days = Integer.parseInt(daysInput.getText().toString().trim());
            double price = Double.parseDouble(priceInput.getText().toString().trim());
            double area = Double.parseDouble(areaInput.getText().toString().trim());
            
            if (days <= 0) {
                daysInput.setError("Số ngày phải lớn hơn 0");
                return false;
            }
            
            if (price <= 0) {
                priceInput.setError("Giá phòng phải lớn hơn 0");
                return false;
            }

            if (area <= 0) {
                areaInput.setError("Diện tích phải lớn hơn 0");
                return false;
            }
            
            // Validate maximum values
            if (days > 365) {
                daysInput.setError("Số ngày không được vượt quá 365");
                return false;
            }
            
            if (price > 1000000000) { // 1 tỷ VND
                priceInput.setError("Giá phòng không được vượt quá 1 tỷ VND");
                return false;
            }
            
            if (area > 1000) { // 1000 m2
                areaInput.setError("Diện tích không được vượt quá 1000 m2");
                return false;
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
} 

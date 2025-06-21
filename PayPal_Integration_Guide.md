# PayPal Integration Guide

## Tích hợp PayPal vào ứng dụng Android

### 1. Cấu hình PayPal Developer Account

Trước khi sử dụng, bạn cần:

1. Đăng ký tài khoản PayPal Developer tại: https://developer.paypal.com/
2. Tạo ứng dụng mới trong PayPal Developer Dashboard
3. Lấy Client ID cho Sandbox và Live environment
4. Cập nhật thông tin trong `PayPalConfig.java`:

```java
public static final String PAYPAL_CLIENT_ID_SANDBOX = "your_actual_sandbox_client_id";
public static final String PAYPAL_CLIENT_ID_LIVE = "your_actual_live_client_id";
```

### 2. Cách sử dụng PayPal trong ứng dụng

#### Cách 1: Sử dụng PaymentHelper (Đơn giản nhất)

```java
// Thanh toán với số tiền định trước
PaymentHelper.startPayment(this, "10.99");

// Thanh toán để người dùng nhập số tiền
PaymentHelper.startPayment(this);
```

#### Cách 2: Sử dụng Intent trực tiếp

```java
Intent intent = new Intent(this, PaymentActivity.class);
intent.putExtra("amount", "25.50"); // Tùy chọn
startActivity(intent);
```

### 3. Các file đã được tạo

1. **PayPalConfig.java** - Cấu hình PayPal constants
2. **PaymentActivity.java** - Activity xử lý thanh toán
3. **PaymentHelper.java** - Helper class để sử dụng dễ dàng
4. **activity_payment.xml** - Layout cho màn hình thanh toán
5. **Các drawable resources** - Styling cho UI

### 4. Permissions cần thiết

Đã được thêm vào AndroidManifest.xml:
- INTERNET permission
- PayPal activities và services

### 5. Dependencies đã thêm

```gradle
implementation 'com.paypal.android:payment-core:1.2.0'
implementation 'com.paypal.android:paypal-web-payments:1.5.0'
```

### 6. Testing

- Sử dụng Sandbox environment để test
- Sử dụng PayPal test accounts để thực hiện giao dịch test
- Chuyển sang Live environment khi deploy production

### 7. Lưu ý bảo mật

- Không commit Client ID thật vào git
- Sử dụng environment variables hoặc config files riêng
- Luôn validate payment ở server side

### 8. Ví dụ sử dụng trong các Activity khác

```java
// Trong MainActivity hoặc bất kỳ Activity nào
Button payButton = findViewById(R.id.pay_button);
payButton.setOnClickListener(v -> {
    PaymentHelper.startPayment(this, "19.99");
});
```

### 9. Customization

Bạn có thể tùy chỉnh:
- Merchant name trong PayPalConfiguration
- Currency (USD, VND, etc.)
- Payment intent (sale, authorize, order)
- UI colors và styling trong layout files

### 10. Error Handling

PaymentActivity đã xử lý các trường hợp:
- Payment successful
- Payment canceled
- Payment failed
- Invalid configuration

Kết quả sẽ được hiển thị trong TextView và Toast messages. 
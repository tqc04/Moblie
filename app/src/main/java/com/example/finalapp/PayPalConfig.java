package com.example.finalapp;

public class PayPalConfig {

    // PayPal environment constants - KHÔNG THAY ĐỔI
    public static final String PAYPAL_ENVIRONMENT_SANDBOX = "sandbox";
    public static final String PAYPAL_ENVIRONMENT_LIVE = "live";

    // Chọn môi trường - Dùng SANDBOX để test, LIVE cho production
    public static final String PAYPAL_ENVIRONMENT = PAYPAL_ENVIRONMENT_SANDBOX;

    // Sandbox Client ID từ PayPal Developer Dashboard
    public static final String PAYPAL_CLIENT_ID_SANDBOX = "Ac7t9q0dU-4TxEGMk-1MqUCvnKPOtyfEC-GlIfXwQu48rN3FesNFg-VZlIynTLdN9w4syhqKS7pBGAsE";

    // Live Client ID - Sẽ được cấp khi app sẵn sàng lên production
    public static final String PAYPAL_CLIENT_ID_LIVE = ""; // Để trống cho đến khi có Live Client ID

    // Các loại tiền tệ hỗ trợ
    public static final String CURRENCY_USD = "USD";
    public static final String CURRENCY_VND = "VND";

    // Các kiểu thanh toán
    public static final String INTENT_SALE = "sale";           // Thanh toán ngay
    public static final String INTENT_AUTHORIZE = "authorize"; // Ủy quyền thanh toán
    public static final String INTENT_ORDER = "order";         // Đặt hàng

    // Lấy Client ID dựa vào môi trường hiện tại
    public static String getClientId() {
        if (PAYPAL_ENVIRONMENT.equals(PAYPAL_ENVIRONMENT_SANDBOX)) {
            return PAYPAL_CLIENT_ID_SANDBOX;
        } else {
            return PAYPAL_CLIENT_ID_LIVE;
        }
    }
}
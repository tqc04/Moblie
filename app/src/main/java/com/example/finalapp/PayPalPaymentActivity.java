package com.example.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PayPalPaymentActivity extends AppCompatActivity {

    private static final String TAG = "PayPalPaymentActivity";
    private static final int PAYPAL_REQUEST_CODE = 123;

    private EditText etAmount;
    private Button btnPayNow;
    private TextView tvResult;

    private PayPalConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        etAmount = findViewById(R.id.et_amount);
        btnPayNow = findViewById(R.id.btn_pay_now);
        tvResult = findViewById(R.id.tv_result);

        // Check if amount was passed from intent
        String predefinedAmount = getIntent().getStringExtra("amount");
        if (predefinedAmount != null && !predefinedAmount.isEmpty()) {
            etAmount.setText(predefinedAmount);
        }

        // PayPal configuration
        config = new PayPalConfiguration()
                .environment(PayPalConfig.PAYPAL_ENVIRONMENT)
                .clientId(PayPalConfig.getClientId())
                // Optional settings
                .acceptCreditCards(true)
                .merchantName("Your Store Name")
                .merchantPrivacyPolicyUri(android.net.Uri.parse("https://yourstore.com/privacy"))
                .merchantUserAgreementUri(android.net.Uri.parse("https://yourstore.com/legal"));

        // Start PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        String amountStr = etAmount.getText().toString().trim();
        
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            
            // Create PayPal payment
            PayPalPayment payment = new PayPalPayment(
                    amount,
                    PayPalConfig.CURRENCY_USD,
                    "Payment for your order",
                    PayPalConfig.INTENT_SALE
            );

            // Start PayPal payment activity
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        // Payment successful
                        tvResult.setText("Payment Successful!\nPayment ID: " + 
                                confirm.getProofOfPayment().getPaymentId());
                        
                        Toast.makeText(this, "Payment completed successfully!", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
                tvResult.setText("Payment Canceled");
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted.");
                tvResult.setText("Payment Failed - Invalid configuration");
                Toast.makeText(this, "Payment failed - Invalid configuration", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Stop PayPal service
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
} 
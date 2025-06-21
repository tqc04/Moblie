package com.example.finalapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PaymentMethodDialog extends Dialog {
    private OnPaymentMethodSelectedListener listener;
    private RadioGroup paymentMethodGroup;
    private Button btnCancel, btnConfirm;

    public interface OnPaymentMethodSelectedListener {
        void onPaymentMethodSelected(boolean isPayPal);
    }

    public PaymentMethodDialog(Context context, OnPaymentMethodSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payment_method_dialog);

        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnCancel.setText(R.string.payment_cancel);
        btnConfirm.setText(R.string.payment_confirm);

        btnCancel.setOnClickListener(v -> dismiss());

        btnConfirm.setOnClickListener(v -> {
            RadioButton selectedRadioButton = findViewById(paymentMethodGroup.getCheckedRadioButtonId());
            boolean isPayPal = selectedRadioButton.getId() == R.id.radioPayPal;
            listener.onPaymentMethodSelected(isPayPal);
            dismiss();
        });

        RadioButton cashButton = findViewById(R.id.radioCash);
        RadioButton paypalButton = findViewById(R.id.radioPayPal);
        cashButton.setText(R.string.payment_cash);
        paypalButton.setText(R.string.payment_paypal);
    }
} 
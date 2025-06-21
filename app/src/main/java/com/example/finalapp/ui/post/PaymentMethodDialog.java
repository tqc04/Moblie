package com.example.finalapp.ui.post;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PaymentMethodDialog extends DialogFragment {
    
    public interface PaymentMethodListener {
        void onPaymentMethodSelected(boolean isPayPal);
    }

    private PaymentMethodListener listener;

    public void setPaymentMethodListener(PaymentMethodListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Chọn phương thức thanh toán")
               .setItems(new CharSequence[]{"PayPal"}, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (listener != null) {
                           // which == 0 là PayPal, which == 1 là tiền mặt
                           listener.onPaymentMethodSelected(which == 0);
                       }
                   }
               });
        return builder.create();
    }
} 
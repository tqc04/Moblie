package com.example.finalapp;

import android.content.Context;
import android.content.Intent;

/**
 * Helper class to make PayPal integration easier from other activities
 */
public class PaymentHelper {
    
    /**
     * Start PayPal payment activity with specified amount
     * @param context Current context
     * @param amount Payment amount (optional, user can enter manually if null)
     */
    public static void startPayment(Context context, String amount) {
        Intent intent = new Intent(context, PayPalPaymentActivity.class);
        if (amount != null && !amount.isEmpty()) {
            intent.putExtra("amount", amount);
        }
        context.startActivity(intent);
    }
    
    /**
     * Start PayPal payment activity without predefined amount
     * @param context Current context
     */
    public static void startPayment(Context context) {
        startPayment(context, null);
    }
} 
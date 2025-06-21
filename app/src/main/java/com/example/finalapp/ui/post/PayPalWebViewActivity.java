package com.example.finalapp.ui.post;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayPalWebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);

        String url = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://your-app.com/paypal-success")) {
                    Uri uri = Uri.parse(url);
                    String orderId = uri.getQueryParameter("token");
                    capturePayPalOrder(orderId);
                    return true;
                } else if (url.startsWith("https://your-app.com/paypal-cancel")) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    return true;
                }
                return false;
            }
        });

        webView.loadUrl(url);
    }

    private void capturePayPalOrder(String orderId) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("orderId", orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toString()
        );
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/paypal/capture-order")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(PayPalWebViewActivity.this, "Lỗi xác nhận thanh toán", Toast.LENGTH_SHORT).show()
                );
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                // Có thể kiểm tra responseBody để xác nhận thanh toán thành công
                runOnUiThread(() -> {
                    Toast.makeText(PayPalWebViewActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                });
            }
        });
    }
}
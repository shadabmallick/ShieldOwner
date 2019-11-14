package com.sketch.securityowner.ui;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sketch.securityowner.R;

public class ShowInvoice extends AppCompatActivity {
    String TAG="ShowInvoice";
  WebView  webView ;
  WebViewClient webViewClient;
    String url;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_invoice);
        webView=findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");

        }
        Log.d(TAG, "onCreate: "+url);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            }
        });

    }
}

package com.example.lottonumber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrScanner extends AppCompatActivity {

    WebView wv;
    EditText et;
    Button bt;
    IntentIntegrator integrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);
        et = findViewById(R.id.et);
        wv = findViewById(R.id.wv);

        bt = findViewById(R.id.bt);
        WebSettings webSettings = wv.getSettings();

        webSettings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                Toast.makeText(QrScanner.this, "로딩 끝", Toast.LENGTH_SHORT).show();
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    bt.callOnClick();

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return  true;
                }
                return false;
            }
        });

        integrator = new IntentIntegrator(this);

        integrator.setPrompt("바코드를 사각형 안에 비춰주세요");

        integrator.setBeepEnabled(false);

        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CaptureActivity.class);

        integrator.initiateScan();
    }

    public void onClick(View view){
        String address = et.getText().toString();

        if(!address.startsWith("http://")){
            address = "http://"+ address;
        }

        wv.loadUrl(address);
    }

    @Override
    public void onBackPressed() {
        if(wv.isActivated()){
            if(wv.canGoBack()){
                wv.goBack();
            }
            else {
                integrator.initiateScan();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if (result.getContents() == null) {

            }
            else  {
                et.setText(result.getContents());

                bt.callOnClick();

                Toast.makeText(this, "Scanned" + result.getContents(), Toast.LENGTH_SHORT).show();
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}

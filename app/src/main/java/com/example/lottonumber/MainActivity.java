package com.example.lottonumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;

    String lotto_No;
    String[] lotto_number = {"drwtNo1", "drwtNo2", "drwtNo3", "drwtNo4", "drwtNo5", "drwtNo6", "bnusNo"};
    JsonObject jsonObject;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLottoNumber();
            }
        });
    }

    public void requestLottoNumber() {
        lotto_No = editText.getText().toString();
        if (lotto_No.equals("")) {
            Toast.makeText(this, "로또 회차 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + lotto_No;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonObject = (JsonObject) JsonParser.parseString(response);

                String str = lotto_No + "회차 당첨번호 : ";
                for (int i = 0; i < lotto_number.length - 1; i++) {
                    str += jsonObject.get(lotto_number[i]) + " , ";

                }
                str += " bonus: " + jsonObject.get(lotto_number[lotto_number.length - 1]);

                textView.setText(str);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return  params;

            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}
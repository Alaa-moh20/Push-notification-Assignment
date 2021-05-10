package com.example.notificationmcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText email,password;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        loginButton=(Button)findViewById(R.id.submit2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "{" +
                        "\"email\"" + ":" + "\"" + email.getText().toString() + "\"," +
                        "\"password\"" + ":" + "\"" + password.getText().toString() + "\"" +
                        "}";
                Submit(data);
            }
        });

    }
    private void Submit(String data) {

        if (TextUtils.isEmpty((email.getText().toString()))) {
            email.setError("enter your email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty((password.getText().toString()))) {
            password.setError("enter your password");
            password.requestFocus();
            return;
        }

        String savedata = data;
        String URL = "https://mcc-users-api.herokuapp.com/login";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "requestQueue: " + requestQueue);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    Log.d("TAG", "onResponse: " + objres.toString());
                    Intent i = new Intent(MainActivity2.this, MainActivity3.class);
                    startActivity(i);
                } catch (JSONException e) {
                    Log.d("TAG", "Server Error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
        };
        requestQueue.add(stringRequest);
    }
}
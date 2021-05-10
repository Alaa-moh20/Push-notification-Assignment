package com.example.notificationmcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText firstName, secondName, email, password;
    Button signupButton;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grtRegToken();

        firstName = (EditText) findViewById(R.id.firstName);
        secondName = (EditText) findViewById(R.id.secondName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signupButton = (Button) findViewById(R.id.submit);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "{" +
                        "\"firstName\"" + ":" + "\"" + firstName.getText().toString() + "\"," +
                        "\"secondName\"" + ":" + "\"" + secondName.getText().toString() + "\"," +
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
        String URL = "https://mcc-users-api.herokuapp.com/add_new_user";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "requestQueue: " + requestQueue);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    Log.d("TAG", "onResponse: " + objres.toString());
                    Intent i = new Intent(MainActivity.this, MainActivity2.class);
                    i.putExtra("email", email.getText().toString());
                    i.putExtra("pas", password.getText().toString());
                    i.putExtra("tok", token);
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



    private void grtRegToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Failed to get token: " + task.getException());
                    return;
                }
                token = task.getResult();
                Log.d("TAG", "Token: " + token); //راح يطبع لكل جهاز توكن مختلف وهادا اللي بلزمني لحتى ارسل اشعار للجهاز
                //بتخزن مع كل يوزر في الفيربيز
            }
        });
    }

}
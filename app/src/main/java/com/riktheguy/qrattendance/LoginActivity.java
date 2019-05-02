package com.riktheguy.qrattendance;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity{

    TextView tv_id, pass;
    Spinner type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_id = (TextView)findViewById(R.id.et_id);
        pass = (TextView)findViewById(R.id.et_pass);
        type = (Spinner)findViewById(R.id.spinner_login_type);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(position == 0){
                    pass.setVisibility(View.VISIBLE);
                }else{
                    pass.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                String aid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                String url="";
                if(type.getSelectedItem().toString().equals("Student")) {
                    url = "id=" + tv_id.getText() + "&type=" + type.getSelectedItem() + "&aId=" + aid;
                }else {
                    url = "id=" + tv_id.getText() + "&type=" + type.getSelectedItem() + "&pass=" + pass.getText();
                }

                JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, DatabaseConnection.BASEURL + DatabaseConnection.LOGIN+url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (type.getSelectedItem().toString().equals("Teacher")) {
                                if (response.getBoolean("success")) {
                                    Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                                    getApplicationContext().startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                                }
                            }else{

                                int ms = response.getInt("msg");

                                if (response.getBoolean("success")) {
                                    if (ms == 1) {
                                        Toast.makeText(getApplicationContext(), "Mobile Registered!", Toast.LENGTH_LONG).show();
                                    }
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("passid",tv_id.getText().toString());
                                    getApplicationContext().startActivity(intent);
                                } else {

                                    if (ms == 2) {
                                        Toast.makeText(getApplicationContext(), "Registered With Another Device", Toast.LENGTH_LONG).show();
                                    } else if (ms == 3) {
                                        Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                                    } else if (ms == 4) {
                                        Toast.makeText(getApplicationContext(), "Device bound with another ID", Toast.LENGTH_LONG).show();
                                    }else if(ms == 0){
                                        Toast.makeText(getApplicationContext(), "Unknown2!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Rik",error.toString());
                    }
                });
                rq.add(jr);
            }
        });

    }
}

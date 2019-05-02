package com.riktheguy.qrattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManualActivity extends AppCompatActivity {

    ArrayList<StudentItem> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManualActivity.this, ChangeAttendanceActivity.class);
                startActivity(intent);
                }
        });

        final RecyclerView myRecyclerView = findViewById(R.id.myReCyclerView);

        RequestQueue rq = Volley.newRequestQueue(this);
        Log.d("Rik","Working1");
        JsonArrayRequest jar = new JsonArrayRequest(Request.Method.GET, DatabaseConnection.BASEURL + DatabaseConnection.GETSTUDENT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++){
                        list.add(new StudentItem(response.getJSONObject(i).getString("st_id"), response.getJSONObject(i).getString("name")));
                    }
                    myRecyclerView.setVisibility(View.VISIBLE);
                }catch (JSONException e){
                        e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Rik",error.toString());
            }
        });

        rq.add(jar);

        AttendanceAdapter adp = new AttendanceAdapter(this, list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(adp);
    }
}
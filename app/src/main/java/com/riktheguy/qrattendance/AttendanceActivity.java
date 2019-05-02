package com.riktheguy.qrattendance;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bindChart(getIntent().getStringExtra("pass_id"));
    }

    void bindChart(String id){
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

        String reqString ="id="+id;
        JsonObjectRequest req =  new JsonObjectRequest(Request.Method.GET, DatabaseConnection.BASEURL + DatabaseConnection.ATTENDANCE+reqString, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            if (response.getBoolean("success")) {
                                Log.d("Rik", "Succeeded");
                                ArrayList<PieEntry> pieEntries = new ArrayList<>();

                                pieEntries.add(new PieEntry((float)response.getInt("absent"),"Absent"));
                                pieEntries.add(new PieEntry((float)response.getInt("present"),"Present"));

                                PieDataSet dataSet  = new PieDataSet(pieEntries," ");
                                dataSet.setColors(new int[]{Color.parseColor("#FFDDDDDD"),
                                        Color.parseColor("#FF555555")});
                                PieChart pieChart = findViewById(R.id.attchart);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setData(new PieData(dataSet));
                                pieChart.getLegend().setEnabled(false);
                                pieChart.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("Rik", "Failed");
                            }
                        }catch(JSONException e){
                            Log.d("Rik","Exception!");
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Rik","Error");
            }
        });
        rq.add(req);
    }
}
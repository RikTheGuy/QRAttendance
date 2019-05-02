package com.riktheguy.qrattendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonScan;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);

        //intializing scan object

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(true);
        qrScan.setPrompt("Scan your Teacher's QR-Code");
        qrScan.setBeepEnabled(true);
        //attaching onclick listener
        buttonScan.setOnClickListener(this);

        bindChart(getIntent().getStringExtra("passid"));
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());

                    GetAttendance(this, obj.getString("address"),"U16CS156",obj.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }

    public void GetAttendance(final Context context, String date, String id, String subject){

        RequestQueue rq = Volley.newRequestQueue(context);

        String reqString ="id="+id+"&date="+date+"&subject="+subject;

        JsonObjectRequest req =  new JsonObjectRequest(Request.Method.GET, DatabaseConnection.BASEURL + DatabaseConnection.PRESENTURL+reqString, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {

                            Log.d("Rik",response.getString("message"));
                            if (response.getBoolean("success")) {
                                Log.d("Rik","Successfully Updated!");
                            } else {
                                Log.d("Rik","Failed Miserably!");
                            }
                        }catch(JSONException e){
                            Log.d("Rik","Exception2!");
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(req);
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

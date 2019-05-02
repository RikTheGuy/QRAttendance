package com.riktheguy.qrattendance;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ChangeAttendanceActivity extends AppCompatActivity {

    Calendar myCalendar;
    EditText dateText;
    EditText idText;
    Spinner subject;
    Spinner present;

    Button bt_change;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_attendance);

        myCalendar = Calendar.getInstance();
        idText = (EditText)findViewById(R.id.tv_st_id);
        subject = (Spinner)findViewById(R.id.spinner_subject_type);
        present = (Spinner)findViewById(R.id.spinner_attendance_type);

        dateText= (EditText) findViewById(R.id.cdate);
        bt_change = (Button)findViewById(R.id.bt_change);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        bt_change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String url = "id="+idText.getText()+"&date="+dateText.getText()+"&subject="+subject.getSelectedItem()+"&present="+present.getSelectedItemPosition();

                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, DatabaseConnection.BASEURL + DatabaseConnection.UPDATE+url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getBoolean("success")){
                                Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }else{
                                Toast.makeText(getApplicationContext(),"Failed Miserably",Toast.LENGTH_LONG).show();
                                Log.d("Rik","Failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Rik",error.toString());
                    }
                });

                rq.add(jor);
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ChangeAttendanceActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel(){
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
    }

}

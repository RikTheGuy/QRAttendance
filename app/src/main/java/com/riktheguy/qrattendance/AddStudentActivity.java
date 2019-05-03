package com.riktheguy.qrattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddStudentActivity extends AppCompatActivity {

    EditText idText, nameText;
    Button bt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        idText = (EditText)findViewById(R.id.et_st_id);
        nameText = (EditText)findViewById(R.id.et_name_S);

        bt_add = (Button)findViewById(R.id.bt_add_st);

        bt_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String url = "id="+idText.getText()+"&name="+nameText.getText();

                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, DatabaseConnection.BASEURL + DatabaseConnection.ADD+url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getBoolean("success")){
                                Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }else{
                                Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
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


    }

}

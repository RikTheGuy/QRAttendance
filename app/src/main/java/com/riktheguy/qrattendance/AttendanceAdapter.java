package com.riktheguy.qrattendance;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<StudentItem> list;

    public AttendanceAdapter(Context context, ArrayList<StudentItem> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View myView = LayoutInflater.from(context).inflate(R.layout.student_row,viewGroup, false);
        return new MyAttendance(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MyAttendance)viewHolder).bind(list.get(i).id, list.get(i).name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyAttendance extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_id;

        public MyAttendance(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.st_name);
            tv_id = itemView.findViewById(R.id.st_id);
        }

        public void bind(String id, String name){
            tv_name.setText(name);
            tv_id.setText(id);
            itemView.findViewById(R.id.bt_deatils).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, LoginActivity.class);
                    i.putExtra("pass_id",tv_id.getText());
                    context.startActivity(i);
                }
            });
        }
    }
}
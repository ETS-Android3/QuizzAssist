package com.example.quizza;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;


import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private List<String> studentsList = new ArrayList<String>();
    private List<String> studentUID = new ArrayList<String>();
    private String studentName;
    private String courseName;
    private String eventName;
    private String questionTitle;
    private Context context;

    public StudentListAdapter(Context context, List<String> studentsList, String courseName,
                              String eventName, String questionTitle, List<String> studentUID){
        this.studentsList = studentsList;
        this.courseName = courseName;
        this.eventName = eventName;
        this.questionTitle = questionTitle;
        this.studentUID = studentUID;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("studentUID", studentUID.get(position));
        holder.studentName.setText(studentUID.get(position));
        holder.studentList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //TODO: implement method to display picture submitted online on firebase storage
                final Intent intent;
                intent = new Intent(context, ViewPNGAnswers.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("eventName", eventName);
                intent.putExtra("questionTitle", questionTitle);
                intent.putExtra("studentUID", studentUID.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return studentsList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView studentList;
        TextView studentName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            studentList = itemView.findViewById(R.id.studentListCardView);
            studentName = itemView.findViewById(R.id.tv_studentName);
        }
    }
}

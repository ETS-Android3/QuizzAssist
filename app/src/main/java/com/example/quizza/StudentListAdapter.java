package com.example.quizza;

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
    private String studentName1;
    private Context context;

    public StudentListAdapter(Context context, String studentName){
        this.context = context;
        this.studentName1 = studentName;
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
        holder.studentName.setText(studentName1);
        holder.studentList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //TODO: implement method to display picture submitted online on firebase storage

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

package com.example.quizza;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.ViewHolder> {

    private List<String> questionsList = new ArrayList<>();
    private Context context;
    LinkingInterface mInterface;

    public AnswerListAdapter(Context context, List<String> questionsList, LinkingInterface mInterface){
        this.context = context;
        this.questionsList = questionsList;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerListAdapter.ViewHolder holder, int position) {
        holder.questionTitle.setText(questionsList.get(position));
        holder.questionsList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //TODO: implement method to navigate to list of answers submitted by students
                mInterface.sendData(questionsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView questionsList;
        TextView questionTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            questionsList = itemView.findViewById(R.id.questionListCardView);
            questionTitle = itemView.findViewById(R.id.tv_questionTitleDetails);
        }
    }
}

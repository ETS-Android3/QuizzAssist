/***
 * AddFragment.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class implements the user choice page between creating a course or joining one.
 * Choice between the two will be displayed and will prompt user for necessary information
 * depending on their choice (class name for creation, invite code for joining).
 */

package com.example.quizza;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {

    private List<String> questionList = new ArrayList<>();
    private String eventName;
    private String courseName;
    private Context context;

    public QuestionListAdapter(Context context, List<String> questionList, String eventName, String courseName){
        this.context = context;
        this.questionList = questionList;
        this.eventName = eventName;
        this.courseName = courseName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionTitle.setText(questionList.get(position));
        holder.questionList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //TODO: implement method to navigate to White Board Activity
                final Intent intent;
                intent = new Intent(context, WhiteBoard.class);
                intent.putExtra("courseName", courseName);
                Log.d("courseName", courseName);
                intent.putExtra("eventName", eventName);
                Log.d("eventName", eventName);
                intent.putExtra("questionTitle", questionList.get(position));
                Log.d("poop", (String) questionList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView questionList;
        TextView questionTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            questionList = itemView.findViewById(R.id.questionListCardView);
            questionTitle = itemView.findViewById(R.id.tv_questionTitleDetails);
        }
    }
}

package com.example.quizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {

    private List<String> questionList = new ArrayList<>();
    private Context mContext;

    public QuestionListAdapter(Context context, List<String> questionList){
        this.mContext = context;
        this.questionList = questionList;
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
            @Override
            public void onClick(View v) {
                //TODO: implement method to navigate to White Board Activity
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
            questionList = itemView.findViewById(R.id.questionListCardView);
            questionTitle = itemView.findViewById(R.id.tv_questionTitleDetails);

        }
    }
}

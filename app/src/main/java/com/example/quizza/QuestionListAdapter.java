package com.example.quizza;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {

    private List<String> questionList = new ArrayList<>();
    private Context mContext;
    private Context context;

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //TODO: implement method to navigate to White Board Activity
                final Intent intent;
                intent = new Intent(context, WhiteBoard.class);

                intent.putExtra("questionTitle", holder.questionTitle.getText());
                Log.d("poop", (String) holder.questionTitle.getText());
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

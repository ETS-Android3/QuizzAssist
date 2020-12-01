package com.example.quizza;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> classesList = new ArrayList<>();
    private List<String> eventList = new ArrayList<>();
    Context mContext;

    public RecyclerViewAdapter(Context mContext, List<String> classesList, List<String> eventList) {
        this.classesList = classesList;
        this.eventList = eventList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_classroom_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("tag", "onbindViewHolder: called.");
        holder.courseTitle.setText(classesList.get(position));
        holder.eventNumber.setText("This course has " + eventList.size() + " number of events !");

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, classesList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitle;
        RelativeLayout parent_layout;
        CardView myCardView;
        TextView eventNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.CourseTitle);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            eventNumber = itemView.findViewById(R.id.tv_eventNumber);
            myCardView = itemView.findViewById(R.id.classCardView);
        }
    }
}

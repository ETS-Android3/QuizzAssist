/***
 * RecyclerViewAdapter.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents').
 * Handles displaying multiple allocations of a specific XML file
 * In our case, this is used to display the users (potentially) many courses that
 * they are enrolled in, or are the host of (course bubbles on home page).
 */

package com.example.quizza;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> classesList = new ArrayList<>();
//    private List<String> eventList = new ArrayList<>();
    Context mContext;
    private LinkingInterface anInterface;

    public RecyclerViewAdapter(Context mContext, List<String> classesList, LinkingInterface mInterface) {
        this.classesList = classesList;
        this.mContext = mContext;
        this.anInterface = mInterface;
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
        holder.courseTitle.setText(classesList.get(position));
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity myActivity = (MainActivity) mContext;
                String clickedValue = classesList.get(position);
                Toast.makeText(mContext, clickedValue, Toast.LENGTH_SHORT).show();
                anInterface.sendData(clickedValue);
//                ClassDetailsFragment classDetailsFragment = new ClassDetailsFragment();
//                myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, classDetailsFragment).addToBackStack(null).commit();
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
//        TextView eventNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.CourseTitle);
            parent_layout = itemView.findViewById(R.id.parent_layout);
//            eventNumber = itemView.findViewById(R.id.tv_eventNumber);
            myCardView = itemView.findViewById(R.id.classCardView);

        }
    }

}


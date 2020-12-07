/***
 * RecyclerViewAdapter_EventList.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Handles displaying multiple allocations of a specific XML file
 * In our case, this is used to display the users (potentially) many events that
 * they may exist in a course.
 */

package com.example.quizza;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter_EventList extends RecyclerView.Adapter<RecyclerViewAdapter_EventList.ViewHolder> {

    private List<String> eventList = new ArrayList<>();
    Context mContext;
    LinkingInterface mInterface;

    public RecyclerViewAdapter_EventList(Context mContext, List<String> eventList, LinkingInterface mInterface){
        this.eventList = eventList;
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_eventlist_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("tag", "onbindViewHolder: called.");
        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnap : snapshot.getChildren()) {
                    if (itemSnap.getKey().equals(eventList.get(position))) {
                        Event myEvent = itemSnap.getValue(Event.class);
                        holder.EventListTitle.setText(myEvent.getEventTitle());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        holder.EventParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, eventList.get(position), Toast.LENGTH_SHORT).show();
                EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                MainActivity myActivity = (MainActivity) mContext;
                mInterface.sendData(eventList.get(position));
                //Navigate to Questions list and Whiteboard
                //myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, eventDetailsFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() { return eventList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView EventListTitle;
        RelativeLayout EventParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            EventListTitle = itemView.findViewById(R.id.tv_eventListTitle);
            EventParentLayout = itemView.findViewById(R.id.event_parent_layout);
        }
    }
}

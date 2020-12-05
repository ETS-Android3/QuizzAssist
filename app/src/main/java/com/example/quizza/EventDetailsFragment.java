package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsFragment extends Fragment {

    TextView eventTitle;
    TextView numOfQuestions;
    FloatingActionButton FAB;
    RecyclerView questionListView;


    List<String> questionList = new ArrayList<>();

    String eventName;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        eventTitle = (TextView) view.findViewById(R.id.tv_eventTitleDetails);
        numOfQuestions = (TextView) view.findViewById(R.id.tv_eventQuestion);
        FAB = (FloatingActionButton) view.findViewById(R.id.floatingActionQuestion);
        questionListView = (RecyclerView) view.findViewById(R.id.questionListView);
        Bundle bundle = this.getArguments();
        eventName = bundle.getString("keyValue");

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateQuestionFragment createQuestionFragment = new CreateQuestionFragment();
                FragmentManager manager = getFragmentManager();
                Bundle zBundle = new Bundle();
                zBundle.putString("eventName", eventName);
                createQuestionFragment.setArguments(zBundle);
                manager.beginTransaction().replace(R.id.flFragment, createQuestionFragment).addToBackStack(null).commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    if(itemSnap.getKey().equals(eventName)){
                        Event myEvent = itemSnap.getValue(Event.class);
                        questionList.addAll(myEvent.getQuestionList());
                        eventTitle.setText(myEvent.getEventTitle());
                        numOfQuestions.setText(Integer.toString(myEvent.getNumberOfQuestions()));
                        QuestionListAdapter adapter = new QuestionListAdapter(getActivity(), questionList);
                        questionListView.setAdapter(adapter);
                        questionListView.setHasFixedSize(true);
                        questionListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}
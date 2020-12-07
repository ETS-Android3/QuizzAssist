/***
 * ViewAnswersFragment.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Class that displays all questions for an event, along with provided answers
 * for each provided question
 */

package com.example.quizza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAnswersFragment extends Fragment {

    List<String> questionsList = new ArrayList<>();
    String eventName;
    String courseName;
    RecyclerView questionsListView;
    Button back;

    public ViewAnswersFragment(){
        //Requires empty constructor
    }

    LinkingInterface mInterface = new LinkingInterface() {
        @Override
        public void sendData(String questionTitle) {
            StudentListFragment studentListFragment = new StudentListFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString("questionTitle", questionTitle);
            mBundle.putString("eventName", eventName);
            mBundle.putString("courseName", courseName);
            studentListFragment.setArguments(mBundle);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.flFragment, studentListFragment).addToBackStack(null).commit();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_answers, container, false);

        Bundle jBundle = this.getArguments();
        eventName = jBundle.getString("eventName");
        courseName = jBundle.getString("courseName");
        questionsList = jBundle.getStringArrayList("questionsList");
        questionsListView = (RecyclerView) view.findViewById(R.id.questionAnswersView);
        back = (Button) view.findViewById(R.id.bt_questionAnswersBack);

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    if(itemSnap.getKey().equals(eventName)){
                        AnswerListAdapter adapter = new AnswerListAdapter(getActivity(), questionsList, mInterface);
                        questionsListView.setAdapter(adapter);
                        questionsListView.setHasFixedSize(true);
                        questionsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

/***
 * ViewAnswersFragment.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Class that displays all questions for an event, along with provided answers
 * for each provided question
 */

package com.example.quizza;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewAnswersFragment extends Fragment {

    List<String> questionsList = new ArrayList<>();
    List<String> questionNamesList = new ArrayList<>();
    String eventName;
    Event myEvent;
    String courseName;
    RecyclerView questionsListView;
    Button back;

    TextView tv_eventTitleAnswers;

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
        tv_eventTitleAnswers = (TextView) view.findViewById(R.id.tv_eventTitleAnswers);
        back = (Button) view.findViewById(R.id.bt_questionAnswersBack);
        questionsListView = (RecyclerView) view.findViewById(R.id.questionAnswersView);

        Bundle jBundle = this.getArguments();
        eventName = jBundle.getString("eventName");
        courseName = jBundle.getString("courseName");
        questionsList = jBundle.getStringArrayList("questionsList");


        FirebaseDatabase.getInstance().getReference("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    if(questionsList.contains(snap.getKey())){
                        questionNamesList.add(snap.getValue(Question.class).getQuestionTitle());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    if(itemSnap.getKey().equals(eventName)){
                        myEvent = itemSnap.getValue(Event.class);
                        tv_eventTitleAnswers.setText(myEvent.getEventTitle() + " Answers");
                        AnswerListAdapter adapter = new AnswerListAdapter(getActivity(), questionNamesList, mInterface);
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

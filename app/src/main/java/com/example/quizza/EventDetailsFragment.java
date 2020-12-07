package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
    Button viewAnswers;
    RecyclerView questionListView;
    User myUser;


    List<String> questionList = new ArrayList<>();
    List<String> questionTitleList = new ArrayList<>();
    Event myEvent;
    Course myCourse;
    Question objQuestion;

    String eventName;
    String courseName;

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
        viewAnswers = (Button) view.findViewById(R.id.bt_viewAnswers);
        questionListView = (RecyclerView) view.findViewById(R.id.questionListView);
        Bundle bundle = this.getArguments();
        eventName = bundle.getString("keyValue");
        courseName = bundle.getString("courseName");
        Log.d("FUCK", courseName);

        //Setting Current User to myUser Local
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    if(snap.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        myUser = snap.getValue(User.class);
                        Log.d("FUCK1", myUser.getUserName());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Setting current course with courseName match from firebase
        FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    if(snap.getValue(Course.class).getCourseName().equals(courseName)){
                        myCourse = snap.getValue(Course.class);
                        if(myCourse.getCourseOwner().equals(myUser.getUserName())){
                            viewAnswers.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionList.size() > myEvent.getNumberOfQuestions() + 1) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Question Limit Reached");
                    builder.setMessage("You have created the Maximum Number of Questions in this Event !");
                    builder.setIcon(R.drawable.icon_error);
                    builder.show();
//                    Toast.makeText(getActivity(), "Cant generate more Questions", Toast.LENGTH_SHORT).show();
                } else {
                    CreateQuestionFragment createQuestionFragment = new CreateQuestionFragment();
                    FragmentManager manager = getFragmentManager();
                    Bundle zBundle = new Bundle();
                    zBundle.putString("eventName", eventName);
                    zBundle.putString("courseName", courseName);
                    createQuestionFragment.setArguments(zBundle);
                    manager.beginTransaction().replace(R.id.flFragment, createQuestionFragment).addToBackStack(null).commit();
                }
            }
        });

        viewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnswersFragment viewAnswersFragment = new ViewAnswersFragment();
                FragmentManager manager = getFragmentManager();
                Bundle zBundle = new Bundle();
                zBundle.putString("eventName", eventName);
                zBundle.putString("courseName", courseName);
                zBundle.putStringArrayList("questionsList", (ArrayList<String>) questionList);
                viewAnswersFragment.setArguments(zBundle);
                manager.beginTransaction().replace(R.id.flFragment, viewAnswersFragment).addToBackStack(null).commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    if(itemSnap.getKey().equals(eventName)){
                        myEvent = itemSnap.getValue(Event.class);
                        questionList.clear();
                        questionList.addAll(myEvent.getQuestionList());
                        eventTitle.setText(myEvent.getEventTitle());
                        numOfQuestions.setText(Integer.toString(myEvent.getNumberOfQuestions()));
                        /*QuestionListAdapter adapter = new QuestionListAdapter(getActivity(), questionList, eventName, courseName);
                        questionListView.setAdapter(adapter);
                        questionListView.setHasFixedSize(true);
                        questionListView.setLayoutManager(new LinearLayoutManager(getActivity()));*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(myCourse.getCourseOwner().equals(myUser.getUserName())){
            FAB.setVisibility(View.VISIBLE);
        }

        //Passing the question List to the Recycler View adapter to show on the UI
        FirebaseDatabase.getInstance().getReference("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    for (String myQuestion : questionList){
                        if(itemSnap.getKey().equals(myQuestion)){
                            objQuestion = itemSnap.getValue(Question.class);
                            if(objQuestion.getEventLink().equals(eventName)){
                                questionTitleList.add(objQuestion.getQuestionTitle());
                            }
                            QuestionListAdapter adapter = new QuestionListAdapter(getActivity(), questionTitleList, eventName, courseName);
                            questionListView.setAdapter(adapter);
                            questionListView.setHasFixedSize(true);
                            questionListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
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
package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateQuestionFragment extends Fragment {

    RelativeLayout createQuestionView;
    EditText questionString;
    EditText classLinked;
    EditText questionTitle;
    Button createQuestionButton;
    List<String> createdCourses = new ArrayList<>();
    Set<String> questionIDs = new HashSet<String>();
    String questionTitleInput;
    User currentUser;
    String eventTitle;
    Event currentEvent;

    public CreateQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_question, container, false);
        questionString = (EditText) view.findViewById(R.id.tv_questionToAdd);
        classLinked = (EditText) view.findViewById(R.id.tv_courseLinked);
        createQuestionView = (RelativeLayout) view.findViewById(R.id.createQuestion);
        createQuestionButton = (Button) view.findViewById(R.id.createQuestionButton);
        questionTitle = (EditText) view.findViewById(R.id.tv_questionTitle);

        Bundle jBundle = this.getArguments();
        eventTitle = jBundle.getString("eventName");
        Log.d("logvalue", eventTitle);

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap: snapshot.getChildren()){
                    if(itemSnap.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        currentUser = itemSnap.getValue(User.class);
                        createdCourses = currentUser.getCreatedCourses();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(currentUser!= null) {
            if (currentUser.getCreatedCourses().isEmpty()) {
                createdCourses = new ArrayList<>();
            } else {
                createdCourses = new ArrayList<>(currentUser.getCreatedCourses());
            }
        }

        createQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionString.getText().toString();
                String courseLink = classLinked.getText().toString();
                questionTitleInput = questionTitle.getText().toString();
                if(question.isEmpty()){
                    questionString.setError("Empty Question string");
                }
                else if (courseLink.isEmpty()){
                    classLinked.setError("Please Input a Non empty class");
                }
                else if (!(createdCourses.contains(courseLink))){
                    classLinked.setError("Please Input a valid created Course");
                }
                else {
                    Question myQuestion = new Question(questionTitleInput, question, courseLink, currentUser.getUserName(), eventTitle);
                    DatabaseReference yReference = FirebaseDatabase.getInstance().getReference("Questions").push();
                    String myQuestionID = yReference.getKey();
                    yReference.setValue(myQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DatabaseReference tReference = FirebaseDatabase.getInstance().getReference("Courses");
                                tReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot mySnapshot : snapshot.getChildren()){
                                            if(mySnapshot.getValue(Course.class).getCourseName().equals(courseLink)){
                                                Course nCourse = mySnapshot.getValue(Course.class);
                                                if(nCourse.getQuestionList().isEmpty()) {
                                                    questionIDs = new HashSet<>();
                                                }
                                                else {
                                                    questionIDs = new HashSet<>(nCourse.getQuestionList());
                                                }
                                                questionIDs.add(myQuestionID);
                                                myQuestion.setEnrolledUsers(nCourse.getEnrolledUsers());
                                                nCourse.setQuestionList(new ArrayList<>(questionIDs));
                                                FirebaseDatabase.getInstance().getReference("Courses/" + mySnapshot.getKey()).setValue(nCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(getActivity(), "Question Created and Linked!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                FirebaseDatabase.getInstance().getReference("Questions/"+ myQuestionID).setValue(myQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getActivity(), "Question Updated!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot itemSnap: snapshot.getChildren()){
                                                            if(itemSnap.getKey().equals(eventTitle)){
                                                                currentEvent = itemSnap.getValue(Event.class);
                                                                currentEvent.setQuestionList(new ArrayList<>(questionIDs));
                                                                FirebaseDatabase.getInstance().getReference("Events/"+itemSnap.getKey()).setValue(currentEvent);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });

                }
            }
        });

        return view;
    }

}
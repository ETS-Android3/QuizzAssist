package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    Button createQuestionButton;
    List<String> createdCourses = new ArrayList<>();
    Set<String> questionIDs = new HashSet<String>();

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

        User currentUser = getCurrentUser();
        if(currentUser.getCreatedCourses().isEmpty()) {
            List<String> createdCourses = new ArrayList<>();
        } else {
            List<String> createdCourses = new ArrayList<>(currentUser.getCreatedCourses());
        }

        createQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionString.getText().toString();
                String courseLink = classLinked.getText().toString();
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
                    Question myQuestion = new Question(question, courseLink, currentUser.getUserName());
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

    private User getCurrentUser(){
        final User[] myUser = {new User()};
        DatabaseReference uReference = FirebaseDatabase.getInstance().getReference("Users");
        uReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapshot: snapshot.getChildren()){
                    if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        myUser[0] = item_snapshot.getValue(User.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myUser[0];
    }

}
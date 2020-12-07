/***
 * JoinCourseFragment.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Fragment that handles retrieving course data from Firebase Database and
 * displays it onto main home fragment (HomeFragment.java)
 */

package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class JoinCourseFragment extends Fragment {

    User currentUser;
    Button returnToUserOptionFromJoinCourse;
    EditText et_userInputInviteCode;
    Button bt_joiningCourse;

    private final String invalidInviteCodeError = "Invite code is invalid. Try again";

    public JoinCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_course, container, false);
        returnToUserOptionFromJoinCourse = (Button) view.findViewById(R.id.bt_returnToUserOptionFromJoining);
        et_userInputInviteCode = (EditText) view.findViewById(R.id.et_joinCourseInviteCode);
        bt_joiningCourse = (Button) view.findViewById(R.id.bt_joiningCourse);

        returnToUserOptionFromJoinCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFragment addFragment = new AddFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, addFragment).addToBackStack(null).commit();
            }
        });

        bt_joiningCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputInviteCode = et_userInputInviteCode.getText().toString();

                DatabaseReference currentDatabase = FirebaseDatabase.getInstance().getReference("Users");
                currentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                            if (currentSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                currentUser = currentSnapshot.getValue(User.class);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //do later
                    }
                });

                currentDatabase = FirebaseDatabase.getInstance().getReference("Courses");
                if (currentUser != null) {
                    currentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                                Course currentCourse = currentSnapshot.getValue(Course.class);
                                if (currentCourse.getInviteCode().equals(userInputInviteCode)) {
                                    Set<String> myUsers = new HashSet<String>(currentCourse.getEnrolledUsers());
                                    myUsers.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    currentCourse.setEnrolledUsers(new ArrayList<String>(myUsers));
                                    FirebaseDatabase.getInstance().getReference("Courses/" + currentSnapshot.getKey()).setValue(currentCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Set<String> enrolledCourses = new HashSet<>();
                                                enrolledCourses.add(currentCourse.getCourseName());
                                                currentUser.setEnrolledCourses(new ArrayList<String>(enrolledCourses));
                                                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentUser);

                                                DatabaseReference tReference = FirebaseDatabase.getInstance().getReference("Events");
                                                tReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot item_snapshot : snapshot.getChildren()){
                                                            if(item_snapshot.getValue(Event.class).getCourseLink().equals(currentCourse.getCourseName())){
                                                                Event myEvent = item_snapshot.getValue(Event.class);
                                                                List<String> enrolledUsers = myEvent.getEnrolledUsers();
                                                                enrolledUsers.add(currentUser.getUserName());
                                                                myEvent.setEnrolledUsers(enrolledUsers);
                                                                FirebaseDatabase.getInstance().getReference("Events/"+item_snapshot.getKey()).setValue(myEvent);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Questions");
                                                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot item_snapshot: snapshot.getChildren()){
                                                            if(item_snapshot.getValue(Question.class).getCourseLink().equals(currentCourse.getCourseName())){
                                                                Question myQuestion = item_snapshot.getValue(Question.class);
                                                                List<String> myEnrolledUsers = myQuestion.getEnrolledUsers();
                                                                myEnrolledUsers.add(currentUser.getUserName());
                                                                myQuestion.setEnrolledUsers(myEnrolledUsers);
                                                                FirebaseDatabase.getInstance().getReference("Questions/" + item_snapshot.getKey()).setValue(myQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getActivity(), "User enrolled Updated !", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                AddFragment addFragment = new AddFragment();
                                                FragmentManager manager = getFragmentManager();
                                                manager.beginTransaction().replace(R.id.flFragment, addFragment).addToBackStack(null).commit();
                                                Toast.makeText(getContext(), "Successfully Joined Class!",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                et_userInputInviteCode.setError(invalidInviteCodeError);
                                            }
                                        }
                                    });
//
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

        return view;
    }
}
/***
 * StudentListFragment.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Class that handles data transfer from StudentListAdapter, and properly displaying
 * fragment_student_list.xml
 */

package com.example.quizza;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentListFragment extends Fragment {

    RecyclerView studentsListView;
    List<String> myUsers = new ArrayList<String>();
    List<String> studentUID = new ArrayList<String>();
    String courseName;
    String eventName;
    String questionTitle;
    List<String> studentList = new ArrayList<String>();
    List<String> studentNamesList = new ArrayList<String>();

    public StudentListFragment(){
        //Requires empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);

        Bundle bundle = this.getArguments();
        courseName = bundle.getString("courseName");
        eventName = bundle.getString("eventName");
        questionTitle = bundle.getString("questionTitle");
        studentsListView = (RecyclerView) view.findViewById(R.id.studentListView);

        Log.d("courseName", courseName);

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap: snapshot.getChildren()){
                    if(itemSnap.getKey().equals(eventName)){
                        studentList.clear();
                        studentList.addAll(itemSnap.getValue(Event.class).getEnrolledUsers());
                        studentList.remove("initial");
                        Log.d("studentList", String.valueOf(studentList));
                    }
                    Log.d("testing", courseName);
                    Log.d("testing", eventName);
                    Log.d("testing", questionTitle);
                    Log.d("testing", String.valueOf(studentUID));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap: snapshot.getChildren()){
                    if(studentList.contains(itemSnap.getValue(User.class).getUserName())){
                        studentUID.add(itemSnap.getKey());
                        studentNamesList.add(itemSnap.getValue(User.class).getUserName());
                        StudentListAdapter adapter = new StudentListAdapter(getActivity(),
                                studentNamesList, courseName, eventName, questionTitle,
                                studentUID );
                        studentsListView.setAdapter(adapter);
                        //studentsListView.setHasFixedSize(true);
                        studentsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

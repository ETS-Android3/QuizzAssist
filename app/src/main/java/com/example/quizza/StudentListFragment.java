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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentListFragment extends Fragment {

    RecyclerView studentsListView;
    String courseName;
    String eventName;
    String questionTitle;
    String studentUID;
    List<String> studentList = new ArrayList<String>();

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
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap: snapshot.getChildren()){
                    List<String> enrolledCourses = itemSnap.getValue(User.class).getEnrolledCourses();
                    for(int i=0; i<enrolledCourses.size(); i++){
                        Log.d("enrolledCourses", enrolledCourses.get(i));
                        if(enrolledCourses.get(i).equals(courseName)){
                            Log.d("WENT", "IN");
                            studentList.add(itemSnap.getValue(User.class).getUserName());
                            studentUID = itemSnap.getKey();
                            Log.i("SIZE", String.valueOf(studentList.size()));
                        }
                    }
                        StudentListAdapter adapter = new StudentListAdapter(getActivity(),
                                studentList, courseName, eventName, questionTitle,
                                studentUID , itemSnap.getValue(User.class).getUserName());
                        studentsListView.setAdapter(adapter);
                        studentsListView.setHasFixedSize(true);
                        studentsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        StorageReference sReference =  FirebaseStorage.getInstance().getReference(courseName + "/" + eventName + "/" + questionTitle);

        return view;
    }
}

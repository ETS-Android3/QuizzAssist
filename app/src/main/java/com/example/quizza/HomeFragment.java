package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class HomeFragment extends Fragment {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");
    private FirebaseAuth fAuth;

    ListView classList;

    Course course1 = new Course();


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser User = fAuth.getCurrentUser();

        classList = (ListView) view.findViewById(R.id.classList);

        ArrayList<Course> myCourses = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapShot : snapshot.getChildren()){
                    if(item_snapShot.getKey().equals(User.getUid())){
                        course1 =  item_snapShot.getValue(Course.class);
                        myCourses.add(course1);
                        Log.d("classname", course1.getCourseName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CourseAdapter myAdapter = new CourseAdapter(getContext(), R.layout.list_view_courses, myCourses);
        classList.setAdapter(myAdapter);



        return view;
    }
}
/***
 * HomeFragment.java
 * Developers: Andrew Yeon, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class implements the dynamic UI elements which are generated based on the
 * created courses and enrolled courses of the user.
 */

package com.example.quizza;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.DTDHandler;

import java.security.CryptoPrimitive;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {

    Context context;
//    Button bt_addEvent;
//    Button bt_joinedCourses;
//    Button bt_createdCourses;

    User currentUser;
    RecyclerView recyclerView;
    List<String> enrolledCourses = new ArrayList<>();
    List<String> createdCourses = new ArrayList<>();


    List<String> classList = new ArrayList<>();
    List<String> eventList = new ArrayList<>();
    Event myEvent;

    TabLayout tabLayout;


    LinkingInterface mInterface = new LinkingInterface() {
        @Override
        public void sendData(String value) {
            Bundle bundle = new Bundle();
            bundle.putString("courseName", value);
            ClassDetailsFragment classDetailsFragment = new ClassDetailsFragment();
            classDetailsFragment.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.flFragment, classDetailsFragment).addToBackStack(null).commit();
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        bt_addEvent = (Button)view.findViewById(R.id.bt_addEvent);
//        bt_joinedCourses = (Button)view.findViewById(R.id.bt_joinedCourses);
//        bt_createdCourses = (Button)view.findViewById(R.id.bt_createdCourses);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        context = getContext();

        String join = "Joined Courses";
        String joinCh = "加入课程";
        String create = "Created Courses";
        String createCh = "创建课程";

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapshot : snapshot.getChildren()){
                        if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            currentUser = item_snapshot.getValue(User.class);
                            //AddAll was added
                            enrolledCourses.addAll(currentUser.getEnrolledCourses());
                            createdCourses.addAll(currentUser.getCreatedCourses());
                            Log.d("keyVlue", enrolledCourses.toString());
                            eventList.clear();
                            classList.clear();
                            //createdCourses = currentUser.getCreatedCourses();
                            classList.addAll(enrolledCourses);
                            for(String className : classList){
                                FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot itemSnap : snapshot.getChildren()){
                                            if (itemSnap.getValue(Course.class).getCourseName().equals(className)){
                                                eventList.addAll(itemSnap.getValue(Course.class).getEventLinkID());
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(join) || tab.getText().equals(joinCh)){
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot item_snapshot : snapshot.getChildren()){
                                if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    currentUser = item_snapshot.getValue(User.class);
                                    enrolledCourses = currentUser.getEnrolledCourses();
                                    classList.clear();
                                    eventList.clear();
                                    classList.addAll(enrolledCourses);
                                    for(String className : classList){
                                        FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot itemSnap : snapshot.getChildren()){
                                                    if (itemSnap.getValue(Course.class).getCourseName().equals(className)){
                                                        eventList.addAll(itemSnap.getValue(Course.class).getEventLinkID());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    Log.d("size in Home View", Integer.toString(eventList.size()));
                                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), classList, mInterface);
                                    //recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else if (tab.getText().equals(create) || tab.getText().equals(createCh)){
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot item_snapshot : snapshot.getChildren()){
                                if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    currentUser = item_snapshot.getValue(User.class);
                                    createdCourses = currentUser.getCreatedCourses();
                                    eventList.clear();
                                    classList.clear();
                                    classList.addAll(createdCourses);
                                    for(String className : classList){
                                        FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot itemSnap : snapshot.getChildren()){
                                                    if (itemSnap.getValue(Course.class).getCourseName().equals(className)){
                                                        eventList.addAll(itemSnap.getValue(Course.class).getEventLinkID());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), classList, mInterface);
                                    //recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                recyclerView.removeAllViews();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getText().equals(join)){

                } else if (tab.getText().equals(create)){

                }
            }
        });


        return view;
    }




    //Establishes UI needed to see class when clicking on any of the classes

    public int DpToPix(float sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        return dpAsPixels;
    }

}
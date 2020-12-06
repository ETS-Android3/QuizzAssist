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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.DTDHandler;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {

    Context context;
    android.widget.GridLayout GridLayout_home;
    android.widget.GridLayout GridLayout_classroom;
    Button button;
    Button bt_addEvent;
    Button bt_joinedCourses;
    Button bt_createdCourses;
    TextView textview;
    CardView cardview;
    ImageView imageview;
    LinearLayout linear_layout;
    RelativeLayout relativelayout;
    LinearLayout.LayoutParams Card_View_Params;
    LinearLayout.LayoutParams Text_View_Params_Lin;
    LinearLayout.LayoutParams Image_View_Params;
    RelativeLayout.LayoutParams Button_Params;
    RelativeLayout.LayoutParams Text_View_Params_Rel;

    LinearLayout classRoom;
    LinearLayout classRoom1;
    RelativeLayout dashboard;

    User currentUser;
    List<String> enrolledCourses = new ArrayList<>();
    List<String> createdCourses = new ArrayList<>();

    Set<String> questionIDs;

    RelativeLayout eventCreationView;
    RelativeLayout questionCreationView;

    CardView startDateView;
    CardView startTimeView;
    CardView endDateView;
    CardView endTimeView;
    TextView startDateText;
    TextView startTimeText;
    TextView endDateText;
    TextView endTimeText;
    EditText numberOfQuestions;
    EditText classLinkedEvent;
    Button startDateButton;
    Button startTimeButton;
    Button endDateButton;
    Button endTimeButton;
    Button saveEventButton;

    RecyclerView recyclerView;

    List<String> classList = new ArrayList<>();
    List<String> eventList = new ArrayList<>();

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
        bt_joinedCourses = (Button)view.findViewById(R.id.bt_joinedCourses);
        bt_createdCourses = (Button)view.findViewById(R.id.bt_createdCourses);
        classRoom = (LinearLayout) view.findViewById(R.id.classRoom);
        classRoom1 = (LinearLayout) view.findViewById(R.id.parentLinearLayout_activity_classroom);
        dashboard = (RelativeLayout) view.findViewById(R.id.coursesView);

        context = getContext();
        GridLayout_classroom = (GridLayout)view.findViewById(R.id.gridLayout_activity_classroom);
        GridLayout_home = (GridLayout)view.findViewById(R.id.gridLayout_activity_home);

        eventCreationView = (RelativeLayout) view.findViewById(R.id.eventCreationView);
        startDateView = (CardView) view.findViewById(R.id.startDateCardView);
        startTimeView = (CardView) view.findViewById(R.id.startTimeCardView);
        endDateView = (CardView) view.findViewById(R.id.endDateCardView);
        endTimeView = (CardView) view.findViewById(R.id.endTimeCardView);
        startDateText = (TextView) view.findViewById(R.id.tv_startDate);
        endDateText = (TextView) view.findViewById(R.id.tv_endDate);
        startTimeText = (TextView) view.findViewById(R.id.tv_startTime);
        endTimeText = (TextView) view.findViewById(R.id.tv_endTime);
        numberOfQuestions = (EditText) view.findViewById(R.id.et_numberOfQuestion);
        classLinkedEvent = (EditText) view.findViewById(R.id.et_classLinked);
        startDateButton = (Button) view.findViewById(R.id.startDateButton);
        startTimeButton = (Button) view.findViewById(R.id.startTimeButton);
        endDateButton = (Button) view.findViewById(R.id.endDateButton);
        endTimeButton = (Button) view.findViewById(R.id.endTimeButton);
        saveEventButton = (Button) view.findViewById(R.id.saveEventButton);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapshot : snapshot.getChildren()){
                        if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            currentUser = item_snapshot.getValue(User.class);
                            enrolledCourses = currentUser.getEnrolledCourses();
                            createdCourses.addAll(currentUser.getCreatedCourses());
                            Log.d("keyVlue", enrolledCourses.toString());
                            //createdCourses = currentUser.getCreatedCourses();
                            classList.addAll(enrolledCourses);
                            classList.addAll(createdCourses);
                            for(String className : classList){
                                FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot itemSnap : snapshot.getChildren()){
                                            if (itemSnap.getValue(Course.class).getCourseName().equals(className)){
                                                eventList.addAll(itemSnap.getValue(Course.class).getEventLinkID());
                                                Log.d("SizeFirebse", Integer.toString(eventList.size()));
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            /*for(String className: createdCourses){
                                eventList.addAll(generateEventList(className));
                            }*/
                            Log.d("SizeBeforeCall", Integer.toString(eventList.size()));
//                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), classList, eventList, mInterface);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setAdapter(adapter);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bt_joinedCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.removeAllViews();

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item_snapshot : snapshot.getChildren()){
                            if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                currentUser = item_snapshot.getValue(User.class);
                                enrolledCourses = currentUser.getEnrolledCourses();
                                classList.clear();
                                classList.addAll(enrolledCourses);
                                for(String className : enrolledCourses){
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
                                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), classList, eventList, mInterface);
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
        });

        bt_createdCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item_snapshot : snapshot.getChildren()){
                            if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                currentUser = item_snapshot.getValue(User.class);
                                createdCourses = currentUser.getCreatedCourses();

                                classList.clear();
                                classList.addAll(createdCourses);
                                for(String className : enrolledCourses){
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
                                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), classList, eventList, mInterface);
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
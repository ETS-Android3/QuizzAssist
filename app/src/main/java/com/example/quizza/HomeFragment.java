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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {

    Context context;
    Button bt_joinedCourses;
    Button bt_createdCourses;
    User currentUser;
    RecyclerView recyclerView;
    List<String> enrolledCourses = new ArrayList<>();
    List<String> createdCourses = new ArrayList<>();
    List<String> classList = new ArrayList<>();
    List<String> eventList = new ArrayList<>();
    Event myEvent;


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
        context = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);




        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    myEvent = itemSnap.getValue(Event.class);

                    try {
                        Date startDate = setDateStyle(myEvent.getStartDate(), myEvent.getStartHour(), myEvent.getStartMns());
                        Date endDate = setDateStyle(myEvent.getEndDate(), myEvent.getEndHours(), myEvent.getEndMns());
                        Date currentDate = new Date();
                        Log.d("startDate", String.valueOf(startDate));
                        Log.d("startDate", String.valueOf(endDate));

                        if(currentDate.after(startDate)){
                            myEvent.setEventStarted(true);
                            Log.d("startStatus", String.valueOf(myEvent.getEventStarted()));
                        }
                        if (currentDate.after(endDate)){
                            myEvent.setEventEnded(true);
                            Log.d("endStatus", String.valueOf(myEvent.getEventEnded()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.d("error", e.getMessage());
                    }
                    FirebaseDatabase.getInstance().getReference("Events/"+itemSnap.getKey()).setValue(myEvent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        DatabaseReference Ureference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        Ureference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapshot : snapshot.getChildren()){
                        if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            currentUser = item_snapshot.getValue(User.class);
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
                Ureference.addListenerForSingleValueEvent(new ValueEventListener() {
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
            }
        });

        bt_createdCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ureference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });

        return view;
    }

    private Date setDateStyle(String dateInput, Integer startH, Integer startMn) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Integer monthInt = 0;
        String month = dateInput.substring(0, 3);
        Integer day = Integer.parseInt(dateInput.substring(5, 6));
        Integer year = Integer.parseInt(dateInput.substring(8, 12));
        monthInt = getMonthInt(month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthInt);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, startH);
        cal.set(Calendar.MINUTE, startMn);
        Date mDate = cal.getTime();
        return mDate;
    }

    private Integer getMonthInt(String month){
        int result = 0;
        if(month.equals("Dec")){
            result = 11;
            return result;
        } else if (month.equals("Nov")){
            result = 10;
            return result;
        }
        else if (month.equals("Oct")){
            result = 9;
            return result;
        }
        else if (month.equals("Sep")){
            result = 8;
            return result;
        }else if (month.equals("Aug")){
            result = 7;
            return result;
        } else if (month.equals("Jul")){
            result = 6;
            return result;
        }else if (month.equals("Jun")){
            result = 5;
            return result;
        } else if (month.equals("May")){
            result = 4;
            return result;
        } else if (month.equals("Apr")){
            result = 3;
            return result;
        } else if (month.equals("Mar")){
            result = 2;
            return result;
        } else if (month.equals("Feb")){
            result = 1;
            return result;
        } else {
            result = 0;
            return result;
        }
    }
}
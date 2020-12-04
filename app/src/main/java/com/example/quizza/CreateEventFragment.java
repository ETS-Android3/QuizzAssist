package com.example.quizza;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventFragment extends Fragment {

    RelativeLayout eventCreationView;
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
    String startDate;
    String endDate;
    String startTime;
    String endTime;
    String eventTitleInput;
    Integer numOfQuestions = 0;
    String courseLinkEvent;
    Integer startHour = 0;
    Integer startMins = 0;
    Integer endHour = 0;
    Integer endMins = 0;
    EditText eventTitle;

    String courseName;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    CreateEventFragment(String courseName){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_event, container, false);
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
        eventTitle = (EditText) view.findViewById(R.id.tv_eventTitle);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            courseName = bundle.getString("courseName");
            classLinkedEvent.setText(courseName);
        }

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select a Start Date");
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getFragmentManager(), "DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        startDate = materialDatePicker.getHeaderText();
                        startDateText.setText(startDate);
                    }
                });
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startHour = hourOfDay;
                                startMins = minute;
                                startTime = hourOfDay + ":" + minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0, hourOfDay, minute);
                                startTimeText.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.show();
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endHour = hourOfDay;
                                endMins = minute;
                                endTime = hourOfDay + ":" + minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0, hourOfDay, minute);
                                endTimeText.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.show();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select a Start Date");
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getFragmentManager(), "DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        endDate = materialDatePicker.getHeaderText();
                        endDateText.setText(endDate);
                    }
                });
            }
        });


        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTitleInput = eventTitle.getText().toString();
                numOfQuestions = Integer.parseInt(numberOfQuestions.getText().toString());
                courseLinkEvent = classLinkedEvent.getText().toString();
                Event myEvent = new Event(eventTitleInput, startDate, startHour, startMins, endDate, endHour, endMins, numOfQuestions, courseLinkEvent);
                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Events").push();
                String eventID = mReference.getKey();
                mReference.setValue(myEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference yReference = FirebaseDatabase.getInstance().getReference("Courses");
                        yReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot item_snapshot : snapshot.getChildren()){
                                    if(item_snapshot.getValue(Course.class).getCourseName().equals(courseLinkEvent)){
                                        Course mCourse = item_snapshot.getValue(Course.class);
                                        List<String> eventIDS = new ArrayList<>(mCourse.getEventLinkID());
                                        eventIDS.add(eventID);
                                        mCourse.setEventLinkID(eventIDS);
                                        FirebaseDatabase.getInstance().getReference("Courses/" + item_snapshot.getKey()).setValue(mCourse);
                                        List<String> enrolledUsers = new ArrayList<>(mCourse.getEnrolledUsers());
                                        myEvent.setEnrolledUsers(enrolledUsers);
                                        FirebaseDatabase.getInstance().getReference("Events/"+eventID).setValue(myEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(), "ALL GOOD!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(getActivity(), "Event Created !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        return view;
    }
}
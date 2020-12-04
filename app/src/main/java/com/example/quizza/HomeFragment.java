/***
 * HomeFragment.java
 * Developers: Andrew Yeon, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class implements the dynamic UI elements which are generated based on the
 * created courses and enrolled courses of the user.
 */

package com.example.quizza;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.List;


//Date and time stuff
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.SystemClock;
import android.provider.Settings;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//graph stuff
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import java.util.Collections;

public class HomeFragment extends Fragment {

    Context context;
    android.widget.GridLayout GridLayout_home;
    android.widget.GridLayout GridLayout_classroom;
    Button bt_addEvent,bt_scheduleEventDate,bt_scheduleEventTime,bt_closeEventPage,
            bt_generateEventGraph,bt_returnToEvent;
    Button button;
    TextView test,tv_eventDateDisplay,tv_eventTimeDisplay;
    TextView textview;
    CardView cardview;
    ImageView imageview;
    LinearLayout linearlayout;
    RelativeLayout relativelayout;
    LinearLayout.LayoutParams Card_View_Params;
    LinearLayout.LayoutParams Text_View_Params_Lin;
    LinearLayout.LayoutParams Image_View_Params;
    RelativeLayout.LayoutParams Button_Params;
    RelativeLayout.LayoutParams Text_View_Params_Rel;

    LinearLayout classRoom;
    LinearLayout classRoom1;
    RelativeLayout eventPage;
    LinearLayout dashboard;
    
    User currentUser;
    List<String> enrolledCourses = new ArrayList<>();
    List<String> createdCourses = new ArrayList<>();
    int currentCourseIndex = -1;
    int currentEventIndex = -1;

    //for barchart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<Integer> grades;
    RelativeLayout graphPage;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bt_addEvent = (Button)view.findViewById(R.id.bt_addEvent);
        test = (TextView)view.findViewById(R.id.test1);
        classRoom = (LinearLayout) view.findViewById(R.id.classRoom);
        classRoom1 = (LinearLayout) view.findViewById(R.id.parentLinearLayout_activity_classroom);
        eventPage = (RelativeLayout) view.findViewById(R.id.eventPage);
        dashboard = (LinearLayout) view.findViewById(R.id.coursesView);

        context = getContext();
        GridLayout_classroom = (GridLayout)view.findViewById(R.id.gridLayout_activity_classroom);
        GridLayout_home = (GridLayout)view.findViewById(R.id.gridLayout_activity_home);

        //date and time stuff
        bt_scheduleEventDate = (Button)view.findViewById(R.id.bt_scheduleEventDate);
        bt_scheduleEventTime = (Button)view.findViewById(R.id.bt_scheduleEventTime);
        bt_closeEventPage = (Button)view.findViewById(R.id.bt_closeEventPage);
        tv_eventDateDisplay = (TextView)view.findViewById(R.id.tv_eventDateDisplay);
        tv_eventTimeDisplay = (TextView)view.findViewById(R.id.tv_eventTimeDisplay);

        //graph stuff
        barChart = (BarChart) view.findViewById(R.id.bargraph);
        bt_generateEventGraph = (Button)view.findViewById(R.id.bt_generateEventGraph);
        graphPage = (RelativeLayout) view.findViewById(R.id.graphPage);
        bt_returnToEvent = (Button)view.findViewById(R.id.bt_returnToEvent);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapshot : snapshot.getChildren()){
                    if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        currentUser = item_snapshot.getValue(User.class);
                        enrolledCourses = currentUser.getEnrolledCourses();
                        createdCourses = currentUser.getCreatedCourses();
                        Log.d("createCourse", createdCourses.toString());
                        Log.d("enrollCourse", enrolledCourses.toString());
                        for(String currentCourse : enrolledCourses){
                            Log.d("createdCourse",currentCourse);
                            AddClassroomUI(currentCourse);
                        }
                        for(String currentCourse : createdCourses){
                            AddClassroomUI(currentCourse);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddClassroomUI("CMPT 6969");
            }
        });

        //Only make the button show if the UID is equal to course owner
        bt_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent();
            }
        });

        bt_scheduleEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        bt_scheduleEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        bt_closeEventPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEventPage();
            }
        });

        bt_generateEventGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateEventGraph();
            }
        });

        bt_returnToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventPage();
            }
        });


        return view;
    }


    //generates and displays the graph for the event (ie statistical breakdown of event)
    public void generateEventGraph() {
        barEntries = new ArrayList<>();

        grades = new ArrayList<>();
        grades.add(3);//TODO get barEntries from actual grades
        grades.add(3);//TODO get barEntries from actual grades
        grades.add(4);//TODO get barEntries from actual grades
        grades.add(4);//TODO get barEntries from actual grades
        grades.add(1);//TODO get barEntries from actual grades
        Collections.sort(grades);

        ArrayList<String> labels = new ArrayList<String>();
        int maxGrade = 5; //TODO get max grade
        for (int i = 0 ; i <= maxGrade ; i++)
            labels.add(Integer.toString(i));

        for(int j = 0; j< grades.size();j++)
            barEntries.add(new BarEntry(grades.get(j),j));

        BarDataSet barDataSet = new BarDataSet(barEntries,"Grades");
        BarData barData = new BarData( labels,barDataSet);
        barChart.setDescription("Grades");
        barChart.setData(barData);

        dashboard.setVisibility(View.INVISIBLE);
        classRoom.setVisibility(View.INVISIBLE);
        eventPage.setVisibility(View.INVISIBLE);
        graphPage.setVisibility(View.VISIBLE);
    }

    //Dynamically creates Event buttons on the fragment_home.xml.
    public void AddEvent(){
        //Initialize the CardView and set properties
        cardview = new CardView(context);
        Card_View_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f);
        Card_View_Params.setMargins(DpToPix(12), DpToPix(12), DpToPix(12)
                , DpToPix(12));
        cardview.setLayoutParams(Card_View_Params);
        cardview.setCardElevation(DpToPix(6));
        cardview.setRadius(DpToPix(12));

        //Initialize the Linear Layout
        linearlayout = new LinearLayout(context);
        LinearLayout.LayoutParams Linear_Layout_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayout.setLayoutParams(Linear_Layout_params);
        linearlayout.setPadding(DpToPix(8),DpToPix(8),0,DpToPix(8));
        linearlayout.setGravity(Gravity.CENTER);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        //Initialize the TextView
        button = new Button(context);
        Text_View_Params_Lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Text_View_Params_Lin.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
        button.setText("Event Number");
        button.setTextColor(Color.BLACK);
        button.setTextSize(20);

        //Set children and parent relationship between each component
        linearlayout.addView(button);
        cardview.addView(linearlayout);
        GridLayout_classroom.addView(cardview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventPage();
            }
        });
    }

    public void openEventPage() {
        Toast.makeText(getActivity(), "Hello boyos", Toast.LENGTH_LONG).show();
        dashboard.setVisibility(View.INVISIBLE);
        classRoom.setVisibility(View.INVISIBLE);
        graphPage.setVisibility(View.INVISIBLE);
        eventPage.setVisibility(View.VISIBLE);
    }

    public void closeEventPage() {
        dashboard.setVisibility(View.INVISIBLE);
        classRoom.setVisibility(View.VISIBLE);
        eventPage.setVisibility(View.INVISIBLE);
    }

    public void handleDateButton() {

        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                tv_eventDateDisplay.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();

    }

    public void handleTimeButton() {

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(getActivity());

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //Log.i("Text", "onTimeSet: " + hour + minute);
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.HOUR_OF_DAY, hour);
                tempCalendar.set(Calendar.MINUTE, minute);
                String timeText = DateFormat.format("h:mm aa", tempCalendar).toString();
                tv_eventTimeDisplay.setText(timeText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }


    public void SetClassView(String className){
        //Initialize the RelativeLayout and it's properties
        relativelayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams Relative_Layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Relative_Layout_params.setMargins(DpToPix(20), DpToPix(30), DpToPix(20), 0);

        //Initialize the Button and it's properties for: AddEvent
//        button = new Button(context);
//        Button_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        Button_Params.setMargins(DpToPix(290), DpToPix(20),0,0);
//        button.setText("ADD EVENT");
//        if(1 == 2/*current user is not the course creator*/){
//            //button.setVisibility(View.INVISIBLE);
//        }
//
//        //Initialize the TextView and set properties
//        textview = new TextView(context);
//        Text_View_Params_Rel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        //Text_View_Params.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
//        textview.setText(courseName);
//        textview.setTextColor(Color.WHITE);
//        textview.setTextSize(40);

        //Set children and parents relationship between each component
//        relativelayout.addView(button);
//        relativelayout.addView(textview);
        classRoom1.addView(relativelayout);
    }

    //Dynamically creates class buttons in scrollview on the fragment_home.xml
    public void AddClassroomUI(String className){
        //Initialize the CardView and set properties
        cardview = new CardView(context);
        Card_View_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        Card_View_Params.setMargins(DpToPix(12), DpToPix(12), DpToPix(12)
                , DpToPix(12));
        cardview.setClickable(true);
        cardview.setLayoutParams(Card_View_Params);
        cardview.setCardElevation(DpToPix(7));
        cardview.setRadius(DpToPix(12));
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard.setVisibility(View.INVISIBLE);
                classRoom.setVisibility(View.VISIBLE);
                SetClassView(className);
                Toast.makeText(getActivity(), "Hello ldies", Toast.LENGTH_LONG).show();
            }
        });

        //Initialize the Linear Layout and set properties
        linearlayout = new LinearLayout(context);
        LinearLayout.LayoutParams Linear_Layout_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayout.setLayoutParams(Linear_Layout_params);
        linearlayout.setPadding(DpToPix(33),DpToPix(16),DpToPix(33),DpToPix(16));
        linearlayout.setGravity(Gravity.CENTER);
        linearlayout.setOrientation(LinearLayout.VERTICAL);
        linearlayout.setBackgroundResource(R.drawable.gradient);

        //Initialize the TextView and set properties
        textview = new TextView(context);
        Text_View_Params_Lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Text_View_Params.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
        textview.setText(className);
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(20);

        //Initialize the ImageView and set properties
        imageview = new ImageView(context);
        Image_View_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageview.setBackgroundResource(R.drawable.course_icon);

        //Set children and parents relationship between each component
        linearlayout.addView(textview);
        linearlayout.addView(imageview);
        cardview.addView(linearlayout);
        GridLayout_home.addView(cardview);
    }


    //Establishes UI needed to see class when clicking on any of the classes


    public int DpToPix(float sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        return dpAsPixels;
    }
}
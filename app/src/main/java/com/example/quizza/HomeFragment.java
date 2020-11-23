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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    Context context;
    android.widget.GridLayout GridLayout_home;
    android.widget.GridLayout GridLayout_classroom;
    Button bt_addEvent;
    TextView test;
    TextView textview;
    CardView cardview;
    ImageView imageview;
    LinearLayout linearlayout;
    LinearLayout.LayoutParams Card_View_Params;
    LinearLayout.LayoutParams Text_View_Params;
    LinearLayout.LayoutParams Image_View_Params;

    CardView courseBox1;
    CardView courseBox2;
    CardView courseBox3;
    CardView courseBox4;
    LinearLayout classRoom;
    LinearLayout dashboard;

    User currentUser;
    List<String> enrolledCourses = new ArrayList<>();
    List<String> createdCourses = new ArrayList<>();


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
        //courseBox1 = (CardView) view.findViewById(R.id.courseBox1);
        //courseBox2 = (CardView) view.findViewById(R.id.courseBox2);
        //courseBox3 = (CardView) view.findViewById(R.id.courseBox3);
        //courseBox4 = (CardView) view.findViewById(R.id.courseBox4);
        classRoom = (LinearLayout) view.findViewById(R.id.classRoom);
        dashboard = (LinearLayout) view.findViewById(R.id.coursesView);

        context = getContext();
        GridLayout_classroom = (GridLayout)view.findViewById(R.id.gridLayout_activity_classroom);
        GridLayout_home = (GridLayout)view.findViewById(R.id.gridLayout_activity_home);

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

        /*courseBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard.setVisibility(View.INVISIBLE);
                classRoom.setVisibility(View.VISIBLE);
            }
        });*/

        return view;
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
        LinearLayout.LayoutParams Linear_Layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayout.setLayoutParams(Linear_Layout);
        linearlayout.setPadding(DpToPix(8),DpToPix(8),0,DpToPix(8));
        linearlayout.setGravity(Gravity.CENTER);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        //Initialize the TextView
        textview = new TextView(context);
        Text_View_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Text_View_Params.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
        textview.setText("Event Number");
        textview.setTextColor(Color.BLACK);
        textview.setTextSize(20);

        //Set children and parent relationship between each component
        linearlayout.addView(textview);
        cardview.addView(linearlayout);
        GridLayout_classroom.addView(cardview);
    }

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

        //Initialize the Linear Layout and set properties
        linearlayout = new LinearLayout(context);
        LinearLayout.LayoutParams Linear_Layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayout.setLayoutParams(Linear_Layout);
        linearlayout.setPadding(DpToPix(33),DpToPix(16),DpToPix(33),DpToPix(16));
        linearlayout.setGravity(Gravity.CENTER);
        linearlayout.setOrientation(LinearLayout.VERTICAL);
        linearlayout.setBackgroundResource(R.drawable.gradient);

        //Initialize the TextView and set properties
        textview = new TextView(context);
        Text_View_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

    public int DpToPix(float sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        return dpAsPixels;
    }
}
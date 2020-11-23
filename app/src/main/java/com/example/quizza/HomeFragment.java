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
    Button button;
    TextView test;
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
        classRoom = (LinearLayout) view.findViewById(R.id.classRoom);
        classRoom1 = (LinearLayout) view.findViewById(R.id.parentLinearLayout_activity_classroom);
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
                        for(String currentCourse : enrolledCourses){
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
        LinearLayout.LayoutParams Linear_Layout_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayout.setLayoutParams(Linear_Layout_params);
        linearlayout.setPadding(DpToPix(8),DpToPix(8),0,DpToPix(8));
        linearlayout.setGravity(Gravity.CENTER);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        //Initialize the TextView
        textview = new TextView(context);
        Text_View_Params_Lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Text_View_Params_Lin.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
        textview.setText("Event Number");
        textview.setTextColor(Color.BLACK);
        textview.setTextSize(20);

        //Set children and parent relationship between each component
        linearlayout.addView(textview);
        cardview.addView(linearlayout);
        GridLayout_classroom.addView(cardview);
    }


    //Stil have to work on this class to make it appear as we want it
    public void SetClassView(String courseName){
        //Initialize the RelativeLayout and it's properties
        relativelayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams Relative_Layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Relative_Layout_params.setMargins(DpToPix(20), DpToPix(30), DpToPix(20), 0);

//        //Initialize the Button and it's properties for: AddEvent
//        button = new Button(context);
//        Button_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        Button_Params.setMargins(DpToPix(290), DpToPix(20),0,0);
//        button.setText("ADD EVENT");
//        if(1 == 2/*current user is not the course creator*/){
//            //button.setVisibility(View.INVISIBLE);
//        }

        //Initialize the TextView and set properties
        textview = new TextView(context);
        Text_View_Params_Rel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //Text_View_Params.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
        textview.setText(courseName);
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(40);

        //Set children and parents relationship between each component
//        relativelayout.addView(button);
//        relativelayout.addView(textview);
        classRoom1.addView(relativelayout);
    }


    //Dynamically creates class buttons in scrollview on the fragment_home.xml
    //Works Perfectly
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
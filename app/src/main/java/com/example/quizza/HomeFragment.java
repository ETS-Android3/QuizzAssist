package com.example.quizza;

import android.content.Context;
import android.graphics.Color;
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
    android.widget.GridLayout GridLayout;
    Button bt_addEvent;
    TextView textview;
    CardView cardview;
    LinearLayout linearlayout;
    LinearLayout.LayoutParams Card_View_Params;
    LinearLayout.LayoutParams Text_View_Params;

    CardView courseBox1;
    CardView courseBox2;
    CardView courseBox3;
    CardView courseBox4;
    LinearLayout classRoom;
    RelativeLayout dashboard;

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
        courseBox1 = (CardView) view.findViewById(R.id.courseBox1);
        courseBox2 = (CardView) view.findViewById(R.id.courseBox2);
        courseBox3 = (CardView) view.findViewById(R.id.courseBox3);
        courseBox4 = (CardView) view.findViewById(R.id.courseBox4);
        classRoom = (LinearLayout) view.findViewById(R.id.classRoom);
        dashboard = (RelativeLayout) view.findViewById(R.id.coursesView);

        context = getContext();
        GridLayout = (GridLayout)view.findViewById(R.id.gridLayout_activity_classroom);

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
                        Log.d("enrollCoures", enrolledCourses.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bt_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent();
            }
        });

        courseBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard.setVisibility(View.INVISIBLE);
                classRoom.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void AddEvent(){
        //Initialize the CardView
        cardview = new CardView(context);

        //Creating parameters for CardView
        Card_View_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f);
        Card_View_Params.setMargins(DpToPix(12), DpToPix(12), DpToPix(12)
                , DpToPix(12));
        cardview.setLayoutParams(Card_View_Params);
        cardview.setCardElevation(DpToPix(6));
        cardview.setRadius(DpToPix(12));


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

        linearlayout.addView(textview);
        cardview.addView(linearlayout);
        GridLayout.addView(cardview);


    }

    public int DpToPix(float sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        return dpAsPixels;
    }
}
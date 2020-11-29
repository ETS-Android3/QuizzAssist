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
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {

    Context context;
    android.widget.GridLayout GridLayout_home;
    android.widget.GridLayout GridLayout_classroom;
    Button bt_addEvent;
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

    RelativeLayout createQuestion;
    EditText questionString;
    EditText classLinked;
    Button createQuestionButton;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bt_addEvent = (Button)view.findViewById(R.id.bt_addEvent);
        classRoom = (LinearLayout) view.findViewById(R.id.classRoom);
        classRoom1 = (LinearLayout) view.findViewById(R.id.parentLinearLayout_activity_classroom);
        dashboard = (RelativeLayout) view.findViewById(R.id.coursesView);
        questionString = (EditText) view.findViewById(R.id.tv_questionToAdd);
        classLinked = (EditText) view.findViewById(R.id.tv_courseLinked);
        createQuestion = (RelativeLayout) view.findViewById(R.id.createQuestion);
        createQuestionButton = (Button) view.findViewById(R.id.createQuestionButton);
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


        createQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionString.getText().toString();
                String courseLink = classLinked.getText().toString();
                if(question.isEmpty()){
                    questionString.setError("Please Input a Valid Question");
                }
                else if (courseLink.isEmpty()){
                    classLinked.setError("Please Input a Valid class to Link");
                }
                else {
                    Question myQuestion = new Question(question, courseLink, currentUser.getUserName());
                    DatabaseReference tDatabase = FirebaseDatabase.getInstance().getReference("Courses");
                    tDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item_snapshot:snapshot.getChildren()){
                                if(item_snapshot.getValue(Course.class).getCourseName().equals(courseLink)){
                                    Course theCourse = item_snapshot.getValue(Course.class);
                                    List<String> questionIds = new ArrayList<>();

                                    DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Questions");
                                    mReference.push().setValue(myQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot item_snapshot:snapshot.getChildren()){
                                                        if(item_snapshot.getValue(Question.class).getCourseLink().equals(theCourse.getCourseName())){
                                                            questionIds.add(item_snapshot.getKey());
                                                            Log.d("questionID", questionIds.toString());
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
//
                                        }
                                    });
                                    theCourse.setQuestionList(questionIds);
                                    FirebaseDatabase.getInstance().getReference("Courses/" + item_snapshot.getKey()).setValue(theCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), "QuestionCreated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });

        bt_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent();
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


        linear_layout = new LinearLayout(context);
        LinearLayout.LayoutParams Linear_Layout_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linear_layout.setLayoutParams(Linear_Layout_params);
        linear_layout.setPadding(DpToPix(8),DpToPix(8),0,DpToPix(8));
        linear_layout.setGravity(Gravity.CENTER);
        linear_layout.setOrientation(LinearLayout.VERTICAL);

        //Initialize the TextView
        textview = new TextView(context);
        Text_View_Params_Lin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Text_View_Params_Lin.setMargins(DpToPix(12), DpToPix(12),0, DpToPix(12));
        textview.setText("Event Number");
        textview.setTextColor(Color.BLACK);
        textview.setTextSize(20);

        //Set children and parent relationship between each component
        linear_layout.addView(textview);
        cardview.addView(linear_layout);
        GridLayout_classroom.addView(cardview);
    }

    public void SetClassView(String courseName){
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
        linear_layout = new LinearLayout(context);
        LinearLayout.LayoutParams Linear_Layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linear_layout.setLayoutParams(Linear_Layout);
        linear_layout.setPadding(DpToPix(33),DpToPix(16),DpToPix(33),DpToPix(16));
        linear_layout.setGravity(Gravity.CENTER);
        linear_layout.setOrientation(LinearLayout.VERTICAL);
        linear_layout.setBackgroundResource(R.drawable.gradient);

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
        linear_layout.addView(textview);
        linear_layout.addView(imageview);
        cardview.addView(linear_layout);
        GridLayout_home.addView(cardview);
    }

    //Establishes UI needed to see class when clicking on any of the classes

    public int DpToPix(float sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        return dpAsPixels;
    }
}
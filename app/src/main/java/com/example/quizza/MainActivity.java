package com.example.quizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button mlogout, addCourses, deleteCourses;
    TextView userInfo1, userInfo2, courseDetails;
    EditText courseInfo;
    FirebaseAuth fAuth;
    User user1 = null;
    DatabaseReference mDatabase;
    //private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlogout = (Button) findViewById(R.id.bt_logout);
        addCourses = (Button) findViewById(R.id.addCourse);
        courseDetails = (TextView) findViewById(R.id.courseDetails);
        deleteCourses = (Button) findViewById(R.id.deleteCourse);
        courseInfo = (EditText) findViewById(R.id.courseInfo);

        userInfo1 = (TextView) findViewById(R.id.userInfo1);
        userInfo2 = (TextView) findViewById(R.id.userInfo2);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snapshot:snapshot.getChildren()){
                    if(item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        user1 = item_snapshot.getValue(User.class);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userInfo1.setText(user1.getName());
                                userInfo2.setText(fAuth.getInstance().getCurrentUser().getEmail());
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = courseInfo.getText().toString();
                Integer courseID = 276;
                if(!(user1 == null)){
                    Course course1 = new Course(courseName, user1, courseID);
                    mDatabase = FirebaseDatabase.getInstance().getReference("Courses");
                    mDatabase.setValue(course1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                courseDetails.setText(course1.getCourseName());
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Error occured in adding class", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        deleteCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), loginPage.class));
        finish();
    }

}

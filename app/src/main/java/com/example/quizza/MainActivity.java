/***
 * MainAcitivity.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class is strictly to provide developers with a testing unit for application functionality
 * regarding creating (adding) and deleting courses from their enrolled/owned list, displaying
 * user information (tests Firebase Database data retrieval), and logout functionality.
 * Some features may not be fully implemented, but will be updated as collaborative team decisions
 * have been made.
 * No known bugs.
 */


package com.example.quizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    Button logoutButton;
    Button addCoursesButton;
    Button deleteCoursesButton;
    Button homeBt;
    TextView userName;
    TextView userEmail;
    TextView courseDetails;
    EditText userInputtedCourseName;
    FirebaseAuth fAuth;
    User currentUser;
    DatabaseReference currentDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = (Button) findViewById(R.id.bt_logout);
        addCoursesButton = (Button) findViewById(R.id.addCourse);
        deleteCoursesButton = (Button) findViewById(R.id.deleteCourse);
        courseDetails = (TextView) findViewById(R.id.courseDetails);
        userInputtedCourseName = (EditText) findViewById(R.id.courseInfo);
        homeBt = (Button) findViewById(R.id.homeBt);

        userName = (TextView) findViewById(R.id.userInfo1);
        userEmail = (TextView) findViewById(R.id.userInfo2);

        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });

        currentDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        currentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item_snapshot:snapshot.getChildren()) {
                    if (item_snapshot.getKey().equals
                            (FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        currentUser = item_snapshot.getValue(User.class);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userName.setText(currentUser.getName());
                                userEmail.setText(fAuth.getInstance().getCurrentUser().getEmail());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = userInputtedCourseName.getText().toString();
                Integer courseID = 276; //testing value
                if (!(currentUser == null)) {
                    Course course1 = new Course(courseName, currentUser, courseID);
                    currentDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    currentDatabaseReference.child("Courses").push().setValue(course1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        courseDetails.setText(course1.getCourseName());
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error occurred in adding class",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        deleteCoursesButton.setOnClickListener(new View.OnClickListener() {
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
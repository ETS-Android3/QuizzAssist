/***
 * HomePage.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class has been newly created to manage the courses that users of application create.
 * From the home screen, once the plus button has been pressed, a new layout will
 * appear, providing the user with three options: Create Class, Join Class, or return to home screen.
 *
 * Features to implement: Layout appearing on the foreground of the homepage and asks user which
 * option they want to execute (Join/Create/Return).
 * No known bugs.
 */

package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class HomePage extends AppCompatActivity {

    Button createButton;
    Button joinButton;
    Button addButton;
    Button settingsButton;
    Button backButton;
    RelativeLayout relativeLayoutOne;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        relativeLayoutOne = (RelativeLayout) findViewById(R.id.relat1);
        createButton = (Button) findViewById(R.id.createBt);
        joinButton = (Button) findViewById(R.id.joinBt);
        addButton = (Button) findViewById(R.id.createOrJoinButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        backButton = (Button) findViewById(R.id.backBt);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutOne.setVisibility(View.VISIBLE);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClassCreation.class));
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClassJoining.class));
            }
        });
    }
}
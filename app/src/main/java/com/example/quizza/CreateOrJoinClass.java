/***
 * CreateOrJoin.java
 * Developers: Brandon Yip
 * CMPT 276 Team 'ForTheStudents'
 * Deprecated, will (most likely) be replaced with the home page implementation:
 * HomePage.java & activity_home_page.xml
 * Save just in case.
 *
 * No known bugs.
 */

package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateOrJoinClass extends AppCompatActivity {

    Button createClassButton;
    Button joinClassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join_class);

        createClassButton = (Button) findViewById(R.id.createClassButton);
        joinClassButton = (Button) findViewById(R.id.joinClassButton);

        createClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ClassCreation.class));
            }
        });

        joinClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ClassCreation.class));
            }
        });
    }
}
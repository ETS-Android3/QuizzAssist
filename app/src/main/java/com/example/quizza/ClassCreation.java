/***
 * ClassCreation.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class provides functionality for when the user opts to create a class of their own.
 *
 * No known bugs.
 */

package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ClassCreation extends AppCompatActivity {

    RadioButton privateSettingButton;
    RadioButton publicSettingButton;
    Button backButton;
    Button createClassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creation);

        backButton = (Button) findViewById(R.id.backBt);
        privateSettingButton = (RadioButton) findViewById(R.id.privacySetting_Private);
        publicSettingButton = (RadioButton) findViewById(R.id.privacySetting_Public);
        createClassButton = (Button) findViewById(R.id.createClassButton);

        createClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreatedClass.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });
    }
}
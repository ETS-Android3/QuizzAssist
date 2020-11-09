package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ClassCreation extends AppCompatActivity {

    RadioButton privateBt;
    RadioButton publicBt;
    Button backBt;
    Button createClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creation);

        backBt = (Button) findViewById(R.id.backBt);

        privateBt = (RadioButton) findViewById(R.id.classSetting_Private);
        publicBt = (RadioButton) findViewById(R.id.classSetting_Public);

        createClass = (Button) findViewById(R.id.CreateClass);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //user successfully created a class, prompt user with screen that displays invite code and
        //something along lines of "successfully created class!"
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreatedClass.class));
            }
        });

        //user cancelled option to create a new class
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });
    }
}
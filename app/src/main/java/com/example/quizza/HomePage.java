package com.example.quizza;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class HomePage extends AppCompatActivity {

    Button createBt;
    Button joinBt;
    Button addBt;
    Button settingsBt;
    RelativeLayout relay1;
    Button backBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        relay1 = (RelativeLayout) findViewById(R.id.joinOrCreate);
        createBt = (Button) findViewById(R.id.createBt);
        joinBt = (Button) findViewById(R.id.joinBt);
        addBt = (Button) findViewById(R.id.createOrJoinButton);
        settingsBt = (Button) findViewById(R.id.settingsButton);
        backBt = (Button) findViewById(R.id.backBt);

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relay1.setVisibility(View.VISIBLE);
            }
        });

        createBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClassCreation.class));
            }
        });

        joinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClassJoining.class));
            }
        });



    }
}
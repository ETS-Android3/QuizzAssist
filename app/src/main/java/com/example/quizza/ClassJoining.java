package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClassJoining extends AppCompatActivity {


    Button backBt;
    EditText classCode;
    Button joinBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_joining);

        backBt = (Button) findViewById(R.id.backBt);
        joinBt = (Button) findViewById(R.id.joinButton);
        classCode = (EditText) findViewById(R.id.classCode);

        joinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = classCode.getText().toString();
                Toast.makeText(ClassJoining.this, "You have succesfully joined " + code, Toast.LENGTH_SHORT).show();
            }
        });

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });

    }
}
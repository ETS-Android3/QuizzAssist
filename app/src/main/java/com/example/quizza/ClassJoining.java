/***
 * ClassJoining.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class implements the page of the QuizzAssist application for users joining a class. The
 * page will have a space for an invite code to be entered along with an optional password field,
 * which will have to be filled in under the circumstances that a room is set to private.
 *
 * Features to implement: Page is currently not fully functional, finish up UI elements. Add the
 * fields for a password, toast messages for incorrect passwords, asking user to enter password if
 * required and none has been entered, etc..
 * No known bugs.
 */

package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClassJoining extends AppCompatActivity {

    Button backButton;
    Button joinButton;
    EditText classCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_joining);

        backButton = (Button) findViewById(R.id.backBt);
        joinButton = (Button) findViewById(R.id.joinClassButton);
        classCode = (EditText) findViewById(R.id.classCodeEditText);
        String joinSuccess = "You have successfully joined.\n";

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = classCode.getText().toString();
                Toast.makeText(ClassJoining.this, joinSuccess + code, Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });
    }
}
/***
 * CreatedClass.java
 * Developers: Brandon Yip
 * CMPT 276 Team 'ForTheStudents'
 * This class implements (and provides button functionality) for the post-class creation screen.
 * Just prompts the user with a friendly 'success' message, and provides user with a button to
 * return to the home screen.
 * No known bugs.
 */

package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreatedClass extends AppCompatActivity {

    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_class);

        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });
    }
}
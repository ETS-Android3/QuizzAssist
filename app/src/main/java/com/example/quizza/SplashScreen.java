/***
 * SplashScreen.java
 * Developers: Brandon Yip
 * CMPT 276 Team 'ForTheStudents'
 * This class provides the application with the introduction screen of the application for users
 * who have not made an account yet, or have just installed the application. Prompting user with a
 * "Get Started" button will lead the user to the sign-up/sign-in page.
 *
 * Features to implement: Make page show for users who have just installed the application.
 * Page will not show for users who have simply logged out (hopefully).
 * No known bugs.
 */

package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startActivity(new Intent(getApplicationContext(), loginPage.class));
        Button getStartedButton = (Button) findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), loginPage.class));
            }
        });
    }
}
/***
 * loginPage.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class handles the login page of the QuizzAssist application. Functionality such as
 * linking various buttons and text, providing functionality for said buttons, and storing users
 * entered information on the Firebase Database. Generic user input checking (currently not very
 * elaborated).
 * Features to implement: Better checking for user input (more sophisticated passwords,
 * valid email checking) UI enhancements (back button border improvement, text font improvements)
 * No known bugs.
 */

package com.example.quizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    Button loginButton;
    Button signUp;

    EditText userName;
    EditText userPassword;

    ProgressBar mProgressBar;
    private FirebaseAuth fAuth;
    RelativeLayout relativeLayoutOne;
    RelativeLayout relativeLayoutTwo;
    public final int minPasswordLength = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        relativeLayoutOne = (RelativeLayout) findViewById(R.id.appLogoRelativeView);
        relativeLayoutTwo = (RelativeLayout) findViewById(R.id.userSignInRelativeView);

        Button test =(Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WhiteBoard.class ));
            }
        });


        userName = (EditText) findViewById(R.id.tv_username);
        userPassword = (EditText) findViewById(R.id.tv_password);

        loginButton = (Button) findViewById(R.id.bt_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();

        signUp = (Button) findViewById(R.id.bt_signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpPage.class));
            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (fAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                relativeLayoutOne.setVisibility(View.VISIBLE);
                relativeLayoutTwo.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(runnable, 2000);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //make error checking & user input checks more secure after
            public void onClick(View v) {
                String email = userName.getText().toString();
                final String password = userPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    userName.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    userPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < minPasswordLength) {
                    userPassword.setError("Password must be longer than 6 Characters");
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                //Authentication of the User
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPage.this, "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(LoginPage.this, "Error !" +
                                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

 /* signUpPage.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class links various buttons and provides functionality for said buttons for the QuizzAssist
 * application sign-up page. New users input various fields of information (username, email, pw)
 * and will have their newly created account stored on the Firebase Database.
 *
 * Features to implement: Better checking for user input (more sophisticated passwords,
 * valid email checking) UI enhancements (back button border improvement, text font improvements)
 * No known bugs.
 */


package com.example.quizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUpPage extends AppCompatActivity {

    EditText userName;
    EditText userEmail;
    EditText userPassword;

    Button signUpButton;
    Button returnToLoginButton;

    private FirebaseAuth fAuth;
    ProgressBar mProgressBar;
    DatabaseReference mDatabase;
    public final int minPasswordLength = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        userName = (EditText) findViewById(R.id.et_userName);
        userEmail = (EditText) findViewById(R.id.et_userEmail);
        userPassword = (EditText) findViewById(R.id.et_userPassword);

        signUpButton = (Button) findViewById(R.id.bt_signUp);
        returnToLoginButton = (Button) findViewById(R.id.backToLogin);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginPage.class));
            }
        });

        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //throw exception here later, return stmt for now
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                final String name = userName.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    userEmail.setError("Email is Required");
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

                //Registration Methods
                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User newUser = new User(name, email);
                                    mDatabase.child(FirebaseAuth.getInstance()
                                            .getCurrentUser().getUid()).setValue(newUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(signUpPage.this, "User Created",
                                                                Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(),
                                                                MainActivity.class));
                                                        finish();
                                                    } else {
                                                        if (task.getException() instanceof
                                                                FirebaseAuthUserCollisionException) {
                                                            Toast.makeText(signUpPage.this, "User already exists! "
                                                                            + task.getException(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(signUpPage.this, "Error, sign up failed!"
                                                                            + task.getException(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(signUpPage.this, "Error Occured !" + task.getException()
                                            .getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
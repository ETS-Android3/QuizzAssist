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

public class signUpPage extends AppCompatActivity {

    EditText mName, mEmail, mPass;
    Button mSignup, backToLogin;
    FirebaseAuth fAuth;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        backToLogin = (Button) findViewById(R.id.backToLogin);
        mName = (EditText) findViewById(R.id.et_name);
        mEmail = (EditText) findViewById(R.id.et_email);
        mPass = (EditText) findViewById(R.id.et_pass);
        mSignup = (Button) findViewById(R.id.bt_signupPage);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginPage.class));
            }
        });

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        fAuth = FirebaseAuth.getInstance();

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPass.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPass.setError("Password is Required");
                    return;
                }
                if(password.length() < 6){
                    mPass.setError("Password must be longer than 6 Characters");
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                //Registration MEthods

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signUpPage.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(signUpPage.this, "Error Occured !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }
}
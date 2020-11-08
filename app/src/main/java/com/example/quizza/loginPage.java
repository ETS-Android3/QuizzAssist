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

public class loginPage extends AppCompatActivity {

    Button bt_login, bt_signup;
    EditText et_user, et_pass;
    ProgressBar mProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        et_user = (EditText) findViewById(R.id.tv_username);
        et_pass = (EditText) findViewById(R.id.tv_password);
        fAuth = FirebaseAuth.getInstance();
        bt_login = (Button) findViewById(R.id.bt_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);

        bt_signup= (Button) findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signUpPage.class ));
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_user.getText().toString();
                String password = et_pass.getText().toString();

                if(TextUtils.isEmpty(email)){
                    et_user.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    et_pass.setError("Password is Required");
                    return;
                }
                if(password.length() < 6){
                    et_pass.setError("Password must be longer than 6 Characters");
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                //Authentication of the User
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(loginPage.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(loginPage.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
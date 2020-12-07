
 /*** signUpPage.java
  * Developers: Brandon Yip, Vatsal Parmar
  * CMPT 276 Team 'ForTheStudents'
  * This class links various buttons and provides functionality for said buttons for the QuizzAssist
  * application sign-up page. New users input various fields of information (username, email, pw)
  * and will have their newly created account stored on the Firebase Database.
  */

 //STILL HAVE TO ASSIGN BACK BUTTON WITH LISTENER TO GO BACK TO SIGNUP

 package com.example.quizza;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import android.content.Intent;
 import android.os.Bundle;
 import android.text.TextUtils;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.ProgressBar;
 import android.widget.Toast;
 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthResult;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseAuthUserCollisionException;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;

 public class SignUpPage extends AppCompatActivity {

     EditText userName;
     EditText userEmail;
     EditText userFirstName;
     EditText userMiddleName;
     EditText userLastName;
     EditText userStudentNumber;
     EditText userPassword;

     Button signUpButton;

     ImageView returnToLogin;

     private FirebaseAuth fAuth;
     ProgressBar mProgressBar;
     DatabaseReference mDatabase;
     public final int minPasswordLength = 6;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_sign_up_page);

         userName = (EditText) findViewById(R.id.et_userName);
         userFirstName = (EditText) findViewById(R.id.et_userFirstName);
         userMiddleName = (EditText) findViewById(R.id.et_userMiddleName);
         userLastName = (EditText) findViewById(R.id.et_userLastName);
         userStudentNumber = (EditText) findViewById(R.id.et_userStudentNumber);
         userEmail = (EditText) findViewById(R.id.et_userEmail);
         userPassword = (EditText) findViewById(R.id.et_userPassword);

         returnToLogin = (ImageView) findViewById(R.id.iv_returnToLogin);

         signUpButton = (Button) findViewById(R.id.bt_signUpPage);
         mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

         returnToLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), LoginPage.class));
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



                 mProgressBar.setVisibility(View.VISIBLE);

                 //Registration Methods
                 fAuth.createUserWithEmailAndPassword(email, password)
                         .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {

                                     User newUser = new User(userName.getText().toString(),
                                             userFirstName.getText().toString(),
                                             userLastName.getText().toString(),
                                             userEmail.getText().toString());

                                     if (!userMiddleName.getText().toString().isEmpty())
                                         newUser.setUserMiddleName(userMiddleName.getText().toString());
                                     if (!userStudentNumber.getText().toString().isEmpty())
                                         newUser.setUserStudentNumber(userStudentNumber.getText().toString());

                                     mDatabase.child(FirebaseAuth.getInstance()
                                             .getCurrentUser().getUid()).setValue(newUser)
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()) {
                                                         Toast.makeText(SignUpPage.this, "User Created",
                                                                 Toast.LENGTH_SHORT).show();
                                                         startActivity(new Intent(getApplicationContext(),
                                                                 MainActivity.class));
                                                         finish();
                                                     } else {
                                                         if (task.getException() instanceof
                                                                 FirebaseAuthUserCollisionException) {
                                                             Toast.makeText(SignUpPage.this, "User already exists! "
                                                                             + task.getException(),
                                                                     Toast.LENGTH_SHORT).show();
                                                         } else {
                                                             Toast.makeText(SignUpPage.this, "Error, sign up failed!"
                                                                             + task.getException(),
                                                                     Toast.LENGTH_SHORT).show();
                                                         }
                                                     }
                                                 }
                                             });
                                 } else {
                                     Toast.makeText(SignUpPage.this, "Error Occurred !" + task.getException()
                                             .getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });
             }
         });
     }
 }
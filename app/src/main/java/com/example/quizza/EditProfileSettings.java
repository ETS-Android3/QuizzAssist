package com.example.quizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileSettings extends AppCompatActivity {

    TextInputEditText userName;
    TextInputEditText userFirstName;
    TextInputEditText userMiddleName;
    TextInputEditText userLastName;
    TextInputEditText userEmail;
    TextInputEditText userStudentNumber;

    Button saveUserProfileChangesButton;

    User currentUser;

    FirebaseAuth myFirebaseAuthenticator;
    DatabaseReference myFirebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_settings);

        userName = (TextInputEditText) findViewById(R.id.ti_et_userName);
        userFirstName = (TextInputEditText) findViewById(R.id.ti_et_userFirstName);
        userMiddleName = (TextInputEditText) findViewById(R.id.ti_et_userMiddleName);
        userLastName = (TextInputEditText) findViewById(R.id.ti_et_userLastName);
        userEmail = (TextInputEditText) findViewById(R.id.ti_et_userEmail);
        userStudentNumber = (TextInputEditText) findViewById(R.id.ti_et_userStudentNumber);

        saveUserProfileChangesButton = (Button) findViewById(R.id.bt_saveUserProfileChanges);

        String userNameString = userName.getText().toString();
        String userFirstNameString = userFirstName.getText().toString();
        String userMiddleNameString = userMiddleName.getText().toString();
        String userLastNameString = userLastName.getText().toString();
        String userEmailString = userEmail.getText().toString();
        String userStudentNumberString = userStudentNumber.getText().toString();
        String userProfileUpdated = "User Profile Updated";
        String userProfileUpdateError = "User Profile Updated";

        //String invalidUserNameError = "Invalid username";
        String emptyUserNameError = "Username cannot be empty";
        String invalidUserFirstNameError = "Invalid first name";
        String emptyUserFirstNameError = "First name cannot be empty";
        String invalidUserMiddleNameError = "Invalid middle name";
        String emptyUserMiddleNameError = "Middle name cannot be empty";
        String invalidUserLastNameError = "Invalid last name";
        String emptyUserLastNameError = "Last name cannot be empty";
        String invalidUserEmailError = "Invalid email"; //I think this is already done automatically
        String emptyUserEmailError = "Email cannot be empty";
        String emptyStudentNumberError = "Student number cannot be empty";

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();

        if (settingsFragment != null) {
            fragmentTransaction.replace(R.id.rv_editUserSettingsPage, settingsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        myFirebaseAuthenticator = FirebaseAuth.getInstance();
        myFirebaseReference = FirebaseDatabase.getInstance().getReference("Users");
        myFirebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                    if (currentSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))
                        currentUser = currentSnapshot.getValue(User.class);
                }
                if (currentUser != null) {

                    //didn't do email because i think xml code checks it with inputType method?
                    if (userNameString.isEmpty() || userFirstNameString.matches(".*\\d.*")
                            || userFirstNameString.isEmpty() || userMiddleNameString.matches(".*\\d.*")
                            || userMiddleNameString.isEmpty() || userLastNameString.matches(".*\\d.*")
                            || userLastNameString.isEmpty() || userStudentNumberString.isEmpty()) {

                        if (userNameString.isEmpty())
                            userName.setError(emptyUserNameError);

                        if (userFirstNameString.isEmpty())
                            userFirstName.setError(emptyUserFirstNameError);
                        else if (userFirstNameString.matches(".*\\d.*"))
                            userFirstName.setError((invalidUserFirstNameError));

                        if (userMiddleNameString.isEmpty())
                            userMiddleName.setError(emptyUserMiddleNameError);
                        else if (userMiddleNameString.matches(".*\\d.*"))
                            userMiddleName.setError((invalidUserMiddleNameError));

                        if (userLastNameString.isEmpty())
                            userLastName.setError(emptyUserLastNameError);
                        else if (userLastNameString.matches(".*\\d.*"))
                            userLastName.setError((invalidUserLastNameError));

                        if (userStudentNumberString.isEmpty())
                            userStudentNumber.setError(emptyStudentNumberError);
                        return;
                    }

                    currentUser.setUserName(userNameString);
                    currentUser.setUserFirstName(userFirstNameString);
                    currentUser.setUserMiddleName(userMiddleNameString);
                    currentUser.setUserLastName(userLastNameString);
                    currentUser.setUserEmail(userEmailString);
                    currentUser.setUserStudentNumber(userStudentNumberString);

                    saveUserProfileChangesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).setValue(currentUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), userProfileUpdated,
                                                        Toast.LENGTH_SHORT).show();

                                                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                                    getSupportFragmentManager().popBackStack();
                                                    return;
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), userProfileUpdateError,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
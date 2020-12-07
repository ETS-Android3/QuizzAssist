/***
 * EditProfileFragmnet.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * This class is the main profile page of the user and displays a button that
 * allows user to edit their account settings (name, email, etc.)
 */

package com.example.quizza;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class EditProfileFragment extends Fragment {

    // TODO: Link all the elements from the .xml layout file and write the edit profile logic,
    //  Already linked Back button for easier navigation
    //  Should be already on TempBranchBrandon on GIT.

    ImageView backView;
    TextInputEditText userName;
    TextInputEditText userFirstName;
    TextInputEditText userMiddleName;
    TextInputEditText userLastName;
    TextInputEditText userEmail;
    TextInputEditText userStudentNumber;

    Button saveUserProfileChangesButton;

    User currentUser;

    FirebaseAuth myFirebaseAuthenticator;
    DatabaseReference currentDatabaseReference;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        backView = (ImageView) view.findViewById(R.id.iv_backToUserProfile);
        userName = (TextInputEditText) view.findViewById(R.id.ti_et_userName);
        userFirstName = (TextInputEditText) view.findViewById(R.id.ti_et_userFirstName);
        userMiddleName = (TextInputEditText) view.findViewById(R.id.ti_et_userMiddleName);
        userLastName = (TextInputEditText) view.findViewById(R.id.ti_et_userLastName);
        userEmail = (TextInputEditText) view.findViewById(R.id.ti_et_userEmail);
        userStudentNumber = (TextInputEditText) view.findViewById(R.id.ti_et_userStudentNumber);
        saveUserProfileChangesButton = (Button) view.findViewById(R.id.bt_saveUserProfileChanges);

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

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, settingsFragment).addToBackStack(null).commit();
            }
        });

        currentDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        currentDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item_snapshot : snapshot.getChildren()) {
                    if (item_snapshot.getKey().equals(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid())) {
                        currentUser = item_snapshot.getValue(User.class);
                        String currentUserName = currentUser.getUserName();
                        String currentUserFirstName = currentUser.getUserFirstName();
                        String currentUserMiddleName = currentUser.getUserMiddleName();
                        String currentUserLastName = currentUser.getUserLastName();
                        String currentUserEmail = currentUser.getUserEmail();
                        String currentUserStudentNumber = currentUser.getUserStudentNumber();


                        //Set data visible for User Profile Page
//                        if (!currentUserMiddleName.isEmpty())
//                            userFullNameProfile.setText(currentUserFirstName + " " + currentUserMiddleName
//                                    + " " + currentUserLastName);
//                        else
//                            userFullNameProfile.setText(currentUserFirstName + " " + currentUserLastName);
//                        userNameProfile.setText(currentUserName);
//                        userEmailProfile.setText(currentUserEmail);
//                        userStudentNumberProfile.setText(currentUserStudentNumber);

                        //Set data visible for "Edit Profile" page
//                        userNameEditProfile.setText(currentUserName);
//                        userFirstNameEditProfile.setText(currentUserFirstName);
//                        userMiddleNameEditProfile.setText(currentUserMiddleName);
//                        userLastNameEditProfile.setText(currentUserLastName);
//                        userEmailEditProfile.setText(currentUserEmail);
//                        userStudentNumberEditProfile.setText(currentUserStudentNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveUserProfileChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).setValue(currentUser)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), userProfileUpdated,
                                            Toast.LENGTH_SHORT).show();

                                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                                        getFragmentManager().popBackStack();
                                        return;
                                    }
                                } else {
                                    Toast.makeText(getContext(), userProfileUpdateError,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
            }
        });

        return view;
    }
}
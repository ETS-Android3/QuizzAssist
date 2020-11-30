package com.example.quizza;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends BottomSheetDialogFragment {

    TextInputEditText userNameEditProfile;
    TextInputEditText userFirstNameEditProfile;
    TextInputEditText userMiddleNameEditProfile;
    TextInputEditText userLastNameEditProfile;
    TextInputEditText userEmailEditProfile;
    TextInputEditText userStudentNumberEditProfile;

    Button editProfileButton;
    Button logoutButton;
    Button saveUserProfileChangesButton;

    User currentUser;

    LinearLayout userAvatarSettingsMenu;
    BottomSheetBehavior sheetBehavior;

    FirebaseAuth myFirebaseAuthenticator;
    DatabaseReference myFirebaseReference;

    ImageView backToUserProfile;
    ImageView changeUserAvatar;

    FirebaseAuth fAuth;
    DatabaseReference currentDatabaseReference;

    RelativeLayout userProfileView;
    RelativeLayout userProfileEditView;

    TextView userNameProfile;
    TextView userFullNameProfile;
    TextView userEmailProfile;
    TextView userStudentNumberProfile;


    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        userNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userName);
        userFirstNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userFirstName);
        userMiddleNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userMiddleName);
        userLastNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userLastName);
        userEmailEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userEmail);
        userStudentNumberEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userStudentNumber);

        editProfileButton = (Button) view.findViewById(R.id.bt_editProfile);
        logoutButton = (Button) view.findViewById(R.id.bt_logoutButton);
        saveUserProfileChangesButton = (Button) view.findViewById(R.id.bt_saveUserProfileChanges);

        userProfileView = (RelativeLayout) view.findViewById(R.id.rv_userSettings);
        userProfileEditView = (RelativeLayout) view.findViewById(R.id.rv_editUserSettingsPage);

        userNameProfile = (TextView) view.findViewById(R.id.tv_userName);
        userFullNameProfile = (TextView) view.findViewById(R.id.tv_userFullName);
        userEmailProfile = (TextView) view.findViewById(R.id.tv_userEmail);
        userStudentNumberProfile = (TextView) view.findViewById(R.id.tv_userStudentNumber);

        backToUserProfile = (ImageView) view.findViewById(R.id.iv_backToUserProfile);
        changeUserAvatar = (ImageView) view.findViewById(R.id.iv_changeUserAvatar);

        userAvatarSettingsMenu = (LinearLayout) view.findViewById(R.id.linLayout_userAvatarSettingsMenu);
        sheetBehavior = BottomSheetBehavior.from(userAvatarSettingsMenu);

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

        currentDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        currentDatabaseReference.addValueEventListener(new ValueEventListener() {
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
                        if (!currentUserMiddleName.isEmpty())
                            userFullNameProfile.setText(currentUserFirstName + " " + currentUserMiddleName
                                    + " " + currentUserLastName);
                        else
                            userFullNameProfile.setText(currentUserFirstName + " " + currentUserLastName);
                        userNameProfile.setText(currentUserName);
                        userEmailProfile.setText(currentUserEmail);
                        userStudentNumberProfile.setText(currentUserStudentNumber);

                        //Set data visible for "Edit Profile" page
                        userNameEditProfile.setText(currentUserName);
                        userFirstNameEditProfile.setText(currentUserFirstName);
                        userMiddleNameEditProfile.setText(currentUserMiddleName);
                        userLastNameEditProfile.setText(currentUserLastName);
                        userEmailEditProfile.setText(currentUserEmail);
                        userStudentNumberEditProfile.setText(currentUserStudentNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileView.setVisibility(View.INVISIBLE);
                userProfileEditView.setVisibility(View.VISIBLE);
            }
        });

        saveUserProfileChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {

                    String userNameString = userNameEditProfile.getText().toString();
                    String userFirstNameString = userFirstNameEditProfile.getText().toString();
                    String userMiddleNameString = userMiddleNameEditProfile.getText().toString();
                    String userLastNameString = userLastNameEditProfile.getText().toString();
                    String userEmailString = userEmailEditProfile.getText().toString();
                    String userStudentNumberString = userStudentNumberEditProfile.getText().toString();

                    //need to check for valid email change
                    if (userNameString.isEmpty() || userFirstNameString.matches(".*\\d.*")
                            || userFirstNameString.isEmpty() || userMiddleNameString.matches(".*\\d.*")
                            || userLastNameString.matches(".*\\d.*") || userLastNameString.isEmpty()
                            || userStudentNumberString.isEmpty()) {

                        if (userNameString.isEmpty())
                            userNameEditProfile.setError(emptyUserNameError);

                        if (userFirstNameString.isEmpty())
                            userFirstNameEditProfile.setError(emptyUserFirstNameError);
                        else if (userFirstNameString.matches(".*\\d.*"))
                            userFirstNameEditProfile.setError((invalidUserFirstNameError));

                        if (userMiddleNameString.matches(".*\\d.*"))
                            userMiddleNameEditProfile.setError((invalidUserMiddleNameError));

                        if (userLastNameString.isEmpty())
                            userLastNameEditProfile.setError(emptyUserLastNameError);
                        else if (userLastNameString.matches(".*\\d.*"))
                            userLastNameEditProfile.setError((invalidUserLastNameError));

                        if (userStudentNumberString.isEmpty())
                            userStudentNumberEditProfile.setError(emptyStudentNumberError);
                        return;
                    }

                    currentUser.setUserName(userNameString);
                    currentUser.setUserFirstName(userFirstNameString);
                    currentUser.setUserMiddleName(userMiddleNameString);
                    currentUser.setUserLastName(userLastNameString);
                    currentUser.setUserEmail(userEmailString);
                    currentUser.setUserStudentNumber(userStudentNumberString);

                    userProfileEditView.setVisibility(View.INVISIBLE);
                    userProfileView.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).setValue(currentUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), userProfileUpdated,
                                                Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getActivity(), userProfileUpdateError,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
            }
        });

        fAuth = FirebaseAuth.getInstance();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getActivity(), LoginPage.class));
                getActivity().finish();
            }
        });

        backToUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileEditView.setVisibility(View.INVISIBLE);
                userProfileView.setVisibility(View.VISIBLE);
            }
        });

        changeUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

            }
        });
        return view;
    }
}
package com.example.quizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SettingsFragment extends Fragment {

    //TODO: if decided then add profile picture method here !

    Button logoutButton;
    Button editProfileButton;
    FirebaseAuth fAuth;
    DatabaseReference currentDatabaseReference;
    TextView userName;
    TextView userEmail;
    TextView userStudentNumber;
    User currentUser;
    RelativeLayout rv_userSettings;

    // TODO: update fields like user MiddleName and other added fields in the SIGNUP page !

    final String toImplement = "This feature will be implemented";

    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        userName = (TextView) view.findViewById(R.id.tv_userName);
        userEmail = (TextView) view.findViewById(R.id.tv_userEmail);
        userStudentNumber = (TextView) view.findViewById(R.id.tv_userStudentNumber);
        editProfileButton = (Button) view.findViewById(R.id.bt_editProfile);
        logoutButton = (Button) view.findViewById(R.id.bt_logoutButton);

        rv_userSettings = (RelativeLayout) view.findViewById(R.id.rv_userSettings);

        currentDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        currentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item_snapshot:snapshot.getChildren()) {
                    if (item_snapshot.getKey().equals
                            (FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        currentUser = item_snapshot.getValue(User.class);
                        userName.setText(currentUser.getUserName());
                        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, editProfileFragment).addToBackStack(null).commit();
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

        return view;
    }

}
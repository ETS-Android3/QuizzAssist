package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    DatabaseReference currentDatabaseReference;
    TextView userName;
    TextView userEmail;
    TextView userStudentNumber;
    User currentUser;

    public void profileFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = (TextView) view.findViewById(R.id.userFirstName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        userStudentNumber = (TextView) view.findViewById(R.id.userStudentNumber);

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
        return view;
    }
}
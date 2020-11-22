package com.example.quizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Button;


public class SettingsFragment extends Fragment {

    Button logoutButton;
    Button classRoomButton;
    FirebaseAuth fAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        classRoomButton = (Button) view.findViewById(R.id.classRoomButton);
        logoutButton = (Button)view.findViewById(R.id.logout_button);
        fAuth = FirebaseAuth.getInstance();

        classRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ClassRoom.class));
                getActivity().finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getActivity(), loginPage.class));
                getActivity().finish();
            }
        });

        return view;
    }

}
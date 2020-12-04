package com.example.quizza;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EditProfileFragment extends Fragment {

    // TODO: Link all the elements from the .xml layout file and write the edit profile logic,
    //  Already linked Back button for easier navigation
    //  Should be already on TempBranchBrandon on GIT.

    Button backButton;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        backButton = (Button) view.findViewById(R.id.bt_backEditProfile);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, settingsFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
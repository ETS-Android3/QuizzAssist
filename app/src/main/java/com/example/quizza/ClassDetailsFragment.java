package com.example.quizza;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClassDetailsFragment extends Fragment {

    TextView courseTitle;
    TextView courseInviteCode;

    RecyclerView classDetailsView;

    public ClassDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_details, container, false);
        courseTitle = (TextView) view.findViewById(R.id.tv_courseTitleDetailsPage);
        courseInviteCode = (TextView) view.findViewById(R.id.tv_inviteCodeDetailsPage);
        classDetailsView = (RecyclerView) view.findViewById(R.id.classDetailsRecyclerView);

        //!TODO: implement a list View of all the events in the class using eventList and also show Course invite code here
        //!TODO: implement a Recycler View adapter for the event list !


        return view;
    }
}
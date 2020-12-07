package com.example.quizza;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ClassDetailsFragment extends Fragment {

    TextView courseTitle;
    TextView courseInviteCode;
    Context mContext;
    FloatingActionButton createEvent;
    String courseName;
    Course currentCourse;

    LinkingInterface mInterface = new LinkingInterface() {
        @Override
        public void sendData(String value) {
            EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString("keyValue", value);
            mBundle.putString("courseName", courseName);
            eventDetailsFragment.setArguments(mBundle);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.flFragment, eventDetailsFragment).addToBackStack(null).commit();
        }
    };

    RecyclerView classDetailsView;

    List<String> eventList = new ArrayList<>();


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
        createEvent = (FloatingActionButton) view.findViewById(R.id.bt_createEvent);

        //!TODO: implement a list View of all the events in the class using eventList and also show Course invite code here
        //!TODO: implement a Recycler View adapter for the event list !
        Bundle bundle = this.getArguments();
        if(bundle!= null){
            courseName = bundle.getString("courseName");
        }

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Courses");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item_snap : snapshot.getChildren()){
                    if(item_snap.getValue(Course.class).getCourseName().equals(courseName)){
                        currentCourse = item_snap.getValue(Course.class);
                        courseTitle.setText(currentCourse.getCourseName());
                        courseInviteCode.setText(currentCourse.getInviteCode());
                        courseInviteCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CopyToClipBoard(currentCourse.getInviteCode());
                            }
                        });
                        eventList.addAll(currentCourse.getEventLinkID());
                        eventList.remove(0);
                        Log.d("key", eventList.toString());
                        RecyclerViewAdapter_EventList adapterEventList = new RecyclerViewAdapter_EventList(getActivity(), eventList, mInterface);
                        classDetailsView.setAdapter(adapterEventList);
                        classDetailsView.setHasFixedSize(true);
                        classDetailsView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventFragment createEventFragment = new CreateEventFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("courseName",currentCourse.getCourseName());
                createEventFragment.setArguments(bundle1);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, createEventFragment).addToBackStack(null).commit();
            }
        });


        return view;
    }


    public void CopyToClipBoard(String text){
        Context context = getContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ClassCode", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Copied To ClipBoard", Toast.LENGTH_SHORT).show();
    }
}
package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    Button addButton;
    EditText userInputClass;
    EditText userInputcourseID;
    User currentUser;
    TextView courseInfo;
    private FirebaseAuth fAuth;
    DatabaseReference currentDatabase;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add, container, false);
        addButton = (Button) view.findViewById(R.id.addCourse);
        userInputClass = (EditText) view.findViewById(R.id.classToAdd);
        userInputcourseID = (EditText) view.findViewById(R.id.courseID);
        courseInfo = (TextView) view.findViewById(R.id.classInfo);

        final String addClassFail = "Error occurred in adding class";


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = userInputClass.getText().toString();
                Integer courseID = 0;

                if (TextUtils.isEmpty(courseName)) {
                    userInputClass.setError("Email is Required");
                    return;
                }

                if(!(currentUser == null)){
                    Course course1 = new Course(courseName, currentUser, courseID);
                    currentDatabase = FirebaseDatabase.getInstance().getReference();
                    currentDatabase.child("Courses").push().setValue(course1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    courseInfo.setText(course1.getCourseName());
                                } else {
                                    Toast.makeText(getActivity(), addClassFail,
                                        Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }

            }
        });

        return view;
    }
}
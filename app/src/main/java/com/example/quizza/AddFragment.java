package com.example.quizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    CourseManager courseManager = new CourseManager();
    AtomicInteger courseID = new AtomicInteger(0);
    User courseOwner;
    DatabaseReference currentDatabase;
    FirebaseAuth fAuth;

    TextView joinSuccessView;
    TextView classJoinInfo;
    TextView classJoinCode;
    TextView courseInfo;

    ImageView backJoinView;
    ImageView backAddView;

    EditText userInputCourseID;
    EditText userInputClass;

    Button joinClassButton;
    Button joinViewButton;
    Button addViewButton;
    Button addButton;

    RelativeLayout joinCourseView;
    RelativeLayout initialAddView;
    RelativeLayout addCourseView;

    private final String emptyCourseNameError = "Course Name is Required";
    private final String addingClassError = "Error occurred in adding class";


    public AddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        joinSuccessView = (TextView) view.findViewById(R.id.joinSuccessView);
        classJoinInfo = (TextView) view.findViewById(R.id.classJoinCodeInfo);
        classJoinCode = (TextView) view.findViewById(R.id.classJoinCode);
        courseInfo = (TextView) view.findViewById(R.id.classInfo);

        backJoinView = (ImageView) view.findViewById(R.id.backJoinView);
        backAddView = (ImageView) view.findViewById(R.id.backAddView);

        userInputCourseID = (EditText) view.findViewById(R.id.courseID);
        userInputClass = (EditText) view.findViewById(R.id.classToAdd);

        joinCourseView = (RelativeLayout) view.findViewById(R.id.joinCourseView);
        initialAddView = (RelativeLayout) view.findViewById(R.id.addPage);
        addCourseView = (RelativeLayout) view.findViewById(R.id.courseAddView);

        joinClassButton = (Button) view.findViewById(R.id.joinClassButton);
        joinViewButton = (Button) view.findViewById(R.id.joinViewButton);
        addViewButton = (Button) view.findViewById(R.id.addViewButton);
        addButton = (Button) view.findViewById(R.id.addCourse);

        backAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialAddView.setVisibility(View.VISIBLE);
                addCourseView.setVisibility(View.INVISIBLE);
                joinCourseView.setVisibility(View.INVISIBLE);
            }
        });

        backJoinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialAddView.setVisibility(View.VISIBLE);
                addCourseView.setVisibility(View.INVISIBLE);
                joinCourseView.setVisibility(View.INVISIBLE);
            }
        });

        addViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialAddView.setVisibility(View.INVISIBLE);
                addCourseView.setVisibility(View.VISIBLE);
                joinCourseView.setVisibility(View.INVISIBLE);
            }
        });

        joinViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialAddView.setVisibility(View.INVISIBLE);
                addCourseView.setVisibility(View.INVISIBLE);
                joinCourseView.setVisibility(View.VISIBLE);
            }
        });

        joinClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinSuccessView.setVisibility(View.VISIBLE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = userInputClass.getText().toString();
                Log.d("courseName", courseName);
                currentDatabase = FirebaseDatabase.getInstance().getReference("Users");
                currentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item_snapshot : snapshot.getChildren()) {
                            if (item_snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                courseOwner = item_snapshot.getValue(User.class);
                            }
                        }
                    }

                    //To implement
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (TextUtils.isEmpty(courseName)) {
                    userInputClass.setError(emptyCourseNameError);
                    return;
                }

                if (courseOwner != null) {
                    Course newCourse = new Course(courseName, courseOwner, courseID.getAndIncrement());
                    while (courseManager.getCourseInviteCodes().containsValue(newCourse.getInviteCode())) {
                        newCourse.generateNewInviteCode();
                    }
                    courseManager.getCourseInviteCodes().put(newCourse, newCourse.getInviteCode());
                    courseManager.getCourseList().add(newCourse);

                    currentDatabase = FirebaseDatabase.getInstance().getReference();

                    currentDatabase.child("Courses").push().setValue(newCourse)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    courseInfo.setText(newCourse.getCourseName());
                                    classJoinInfo.setVisibility(View.VISIBLE);
                                    classJoinCode.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(getActivity(), addingClassError, Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }
            }
        });
        return view;
    }
}
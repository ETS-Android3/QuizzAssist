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

    TextView tv_joinCourseSuccessText;
    TextView tv_courseInviteCodeHelperText;
    TextView tv_courseInviteCode;
   //TextView courseInfo;

    ImageView iv_returnToUserOptionFromJoinCourse;
    ImageView iv_returnToUserOptionFromCreateCourse;

    EditText et_userInputCourseName;

    Button bt_userOptionJoinCourse; //user choice to join a course
    Button bt_joiningCourse; //join course after inputting invite code

    Button bt_userOptionCreateCourse; //user choice to create a course
    Button bt_creatingCourse; //create course after inputting course name

    RelativeLayout rv_joinCoursePage;
    RelativeLayout rv_userOptionPage;
    RelativeLayout rv_createCoursePage;

    private final String emptyCourseNameError = "Course Name is Required";
    private final String addingClassError = "Error occurred in adding class";


    public AddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        tv_joinCourseSuccessText = (TextView) view.findViewById(R.id.tv_joinCourseSuccess);
        tv_courseInviteCodeHelperText = (TextView) view.findViewById(R.id.tv_CourseInviteCodeHelperText);
        tv_courseInviteCode = (TextView) view.findViewById(R.id.tv_generatedCourseInviteCode);
        //courseInfo = (TextView) view.findViewById(R.id.classInfo);

        iv_returnToUserOptionFromJoinCourse = (ImageView) view.findViewById(R.id.iv_returnToUserOptionFromJoining);
        iv_returnToUserOptionFromCreateCourse = (ImageView) view.findViewById(R.id.iv_returnToUserOptionFromCreating);

        et_userInputCourseName = (EditText) view.findViewById(R.id.et_courseName);

        rv_userOptionPage = (RelativeLayout) view.findViewById(R.id.rv_userOptionPage);
        rv_joinCoursePage = (RelativeLayout) view.findViewById(R.id.rv_JoiningCoursePage);
        rv_createCoursePage = (RelativeLayout) view.findViewById(R.id.rv_creatingCoursePage);

        bt_userOptionJoinCourse = (Button) view.findViewById(R.id.bt_userOptionJoinCourse);
        bt_joiningCourse = (Button) view.findViewById(R.id.bt_joiningCourse);
        bt_userOptionCreateCourse = (Button) view.findViewById(R.id.bt_userOptionCreateCourse);
        bt_creatingCourse = (Button) view.findViewById(R.id.bt_creatingCourse);

        iv_returnToUserOptionFromCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_userOptionPage.setVisibility(View.VISIBLE);
                rv_createCoursePage.setVisibility(View.INVISIBLE);
                rv_joinCoursePage.setVisibility(View.INVISIBLE);
            }
        });

        iv_returnToUserOptionFromJoinCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_userOptionPage.setVisibility(View.VISIBLE);
                rv_createCoursePage.setVisibility(View.INVISIBLE);
                rv_joinCoursePage.setVisibility(View.INVISIBLE);
            }
        });

        bt_userOptionCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_userOptionPage.setVisibility(View.INVISIBLE);
                rv_createCoursePage.setVisibility(View.VISIBLE);
                rv_joinCoursePage.setVisibility(View.INVISIBLE);
            }
        });

        bt_userOptionJoinCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_userOptionPage.setVisibility(View.INVISIBLE);
                rv_createCoursePage.setVisibility(View.INVISIBLE);
                rv_joinCoursePage.setVisibility(View.VISIBLE);
            }
        });

        bt_joiningCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_joinCourseSuccessText.setVisibility(View.VISIBLE);
            }
        });

        bt_creatingCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = et_userInputCourseName.getText().toString();
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
                    et_userInputCourseName.setError(emptyCourseNameError);
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
                                    if (!task.isSuccessful()) {
                                        //courseInfo.setText(newCourse.getCourseName());
                                        tv_courseInviteCodeHelperText.setVisibility(View.VISIBLE);
                                        tv_courseInviteCode.setText(newCourse.getInviteCode());
                                        tv_courseInviteCode.setVisibility(View.VISIBLE);
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
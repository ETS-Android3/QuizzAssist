/***
 * AddFragment.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class implements the user choice page between creating a course or joining one.
 * Choice between the two will be displayed and will prompt user for necessary information
 * depending on their choice (class name for creation, invite code for joining).
 */

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AddFragment extends Fragment {
    AtomicInteger courseID = new AtomicInteger(0);
    User currentUser;
    DatabaseReference currentDatabase;
    FirebaseAuth fAuth;

    TextView tv_joinCourseSuccessText;
    TextView tv_courseInviteCodeHelperText;
    TextView tv_courseInviteCode;

    ImageView iv_returnToUserOptionFromJoinCourse;
    ImageView iv_returnToUserOptionFromCreateCourse;

    EditText et_userInputCourseName;
    EditText et_userInputInviteCode;

    Button bt_userOptionJoinCourse; //user choice to join a course
    Button bt_joiningCourse; //join course after inputting invite code

    Button bt_userOptionCreateCourse; //user choice to create a course
    Button bt_creatingCourse; //create course after inputting course name

    RelativeLayout rv_joinCoursePage;
    RelativeLayout rv_userOptionPage;
    RelativeLayout rv_createCoursePage;

    private final String emptyCourseNameError = "Course name is required";
    private final String courseNameExistsError = "Course with that name already exists";
    private final String invalidInviteCodeError = "Invite code is invalid. Try again";


    public AddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        tv_joinCourseSuccessText = (TextView) view.findViewById(R.id.tv_joinCourseSuccess);
        tv_courseInviteCodeHelperText = (TextView) view.findViewById(R.id.tv_CourseInviteCodeHelperText);
        tv_courseInviteCode = (TextView) view.findViewById(R.id.tv_generatedCourseInviteCode);

        iv_returnToUserOptionFromJoinCourse = (ImageView) view.findViewById(R.id.iv_returnToUserOptionFromJoining);
        iv_returnToUserOptionFromCreateCourse = (ImageView) view.findViewById(R.id.iv_returnToUserOptionFromCreating);

        et_userInputCourseName = (EditText) view.findViewById(R.id.et_courseName);
        et_userInputInviteCode = (EditText) view.findViewById(R.id.et_joinCourseInviteCode);

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
                String userInputInviteCode = et_userInputInviteCode.getText().toString();

                currentDatabase = FirebaseDatabase.getInstance().getReference("Users");
                currentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                            if (currentSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                currentUser = currentSnapshot.getValue(User.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //do later
                    }
                });

                currentDatabase = FirebaseDatabase.getInstance().getReference("Courses");
                if (currentUser != null) {
                    currentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                                Course currentCourse = currentSnapshot.getValue(Course.class);
                                if (currentCourse.getInviteCode().equals(userInputInviteCode)) {
                                    Set<String> myUsers = new HashSet<String>(currentCourse.getEnrolledUsers());
                                    myUsers.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    currentCourse.setEnrolledUsers(new ArrayList<String>(myUsers));
                                    FirebaseDatabase.getInstance().getReference("Courses/" + currentSnapshot.getKey()).setValue(currentCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Set<String> enrolledCourses = new HashSet<>();
                                                enrolledCourses.add(currentCourse.getCourseName());
                                                currentUser.setEnrolledCourses(new ArrayList<String>(enrolledCourses));
                                                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentUser);

                                                rv_userOptionPage.setVisibility(View.VISIBLE);
                                                rv_createCoursePage.setVisibility(View.INVISIBLE);
                                                rv_joinCoursePage.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getContext(), "Successfully Joined!",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                et_userInputInviteCode.setError(invalidInviteCodeError);
                                            }
                                        }
                                    });
//
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        bt_creatingCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputCourseName = et_userInputCourseName.getText().toString();

                currentDatabase = FirebaseDatabase.getInstance().getReference("Users");
                currentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                            if (currentSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                currentUser = currentSnapshot.getValue(User.class);
                                Set<String> createdCourses = new HashSet<>(currentUser.getCreatedCourses());
                                createdCourses.add(userInputCourseName);
                                currentUser.setCreatedCourses(new ArrayList<String>(createdCourses));
                                FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentUser);
                            }
                        }
                    }

                    //To implement
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (TextUtils.isEmpty(userInputCourseName)) {
                    et_userInputCourseName.setError(emptyCourseNameError);
                    return;
                }

                if (currentUser != null) {
                    Course newCourse = new Course(userInputCourseName, currentUser.getUserName(), courseID.getAndIncrement());


                    currentDatabase = FirebaseDatabase.getInstance().getReference();
                    currentDatabase.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                                if (currentSnapshot.getValue(Course.class).getCourseName().equals(userInputCourseName)) {
                                    et_userInputCourseName.setError(courseNameExistsError);
                                    return;
                                }
                                while (currentSnapshot.getValue(Course.class).getInviteCode().equals(newCourse.getInviteCode())) {
                                    newCourse.generateNewInviteCode();
                                }
                            }

                            currentDatabase.child("Courses").push().setValue(newCourse)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                tv_courseInviteCodeHelperText.setVisibility(View.VISIBLE);
                                                tv_courseInviteCode.setText(newCourse.getInviteCode());
                                                tv_courseInviteCode.setVisibility(View.VISIBLE);
                                            } else {
                                                Toast.makeText(getActivity(), courseNameExistsError, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        return view;
    }
}
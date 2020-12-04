package com.example.quizza;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CreateCourseFragment extends Fragment {

    User currentUser;
    AtomicInteger courseID = new AtomicInteger(0);
    TextView tv_courseInviteCode;
    Button returnToUserOptionFromCreateCourse;
    Button bt_creatingCourse;
    EditText et_userInputCourseName;

    TextView tv_courseInviteCodeHelperText;

    Context mContext;

    private final String emptyCourseNameError = "Course name is required";
    private final String courseNameExistsError = "Course with that name already exists";

    public CreateCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_course, container, false);
        tv_courseInviteCode = (TextView) view.findViewById(R.id.tv_generatedCourseInviteCode);
        returnToUserOptionFromCreateCourse = (Button) view.findViewById(R.id.bt_returnToUserOptionFromCreating);
        bt_creatingCourse = (Button) view.findViewById(R.id.bt_creatingCourse);
        et_userInputCourseName = (EditText) view.findViewById(R.id.et_courseName);
        tv_courseInviteCodeHelperText = (TextView) view.findViewById(R.id.tv_CourseInviteCodeHelperText);

        returnToUserOptionFromCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFragment addFragment = new AddFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, addFragment).addToBackStack(null).commit();
            }
        });

        bt_creatingCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputCourseName = et_userInputCourseName.getText().toString();
                if (TextUtils.isEmpty(userInputCourseName)) {
                    et_userInputCourseName.setError(emptyCourseNameError);
                    return;
                }
                DatabaseReference currentDatabase = FirebaseDatabase.getInstance().getReference("Users");
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

                if (currentUser != null) {
                    Course newCourse = new Course(userInputCourseName, currentUser.getUserName(), courseID.getAndIncrement());
                    newCourse.generateNewInviteCode();

                    DatabaseReference jReference = FirebaseDatabase.getInstance().getReference("Courses");
                    jReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot item_snap:snapshot.getChildren()){
                                if(item_snap.getValue(Course.class).getCourseName().equals(userInputCourseName)){
                                    et_userInputCourseName.setError(courseNameExistsError);
                                    return;
                                } else {
                                  jReference.push().setValue(newCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          tv_courseInviteCode.setText(newCourse.getInviteCode());
                                          tv_courseInviteCodeHelperText.setVisibility(View.VISIBLE);
                                          tv_courseInviteCode.setVisibility(View.VISIBLE);
                                          tv_courseInviteCode.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  CopyToClipBoard(tv_courseInviteCode.getText().toString());
                                              }
                                          });
                                      }
                                  });
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
        return view;
    }

    public void CopyToClipBoard(String text){
        Context context = getContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ClassCode", text);
        clipboard.setPrimaryClip(clip);
    }

}
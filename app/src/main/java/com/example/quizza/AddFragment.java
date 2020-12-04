/***
 * AddFragment.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class implements the user choice page between creating a course or joining one.
 * Choice between the two will be displayed and will prompt user for necessary information
 * depending on their choice (class name for creation, invite code for joining).
 */

package com.example.quizza;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AddFragment extends Fragment {

    Button bt_userOptionJoinCourse; //user choice to join a course
    Button bt_userOptionCreateCourse; //user choice to create a course
    RelativeLayout rv_userOptionPage;

    public AddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        rv_userOptionPage = (RelativeLayout) view.findViewById(R.id.rv_userOptionPage);
        bt_userOptionJoinCourse = (Button) view.findViewById(R.id.bt_userOptionJoinCourse);
        bt_userOptionCreateCourse = (Button) view.findViewById(R.id.bt_userOptionCreateCourse);

        bt_userOptionCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCourseFragment createCourseFragment = new CreateCourseFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, createCourseFragment).addToBackStack(null).commit();
            }
        });

        bt_userOptionJoinCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinCourseFragment joinCourseFragment = new JoinCourseFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, joinCourseFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

}
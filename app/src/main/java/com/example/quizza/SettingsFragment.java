/***
 * SettingsFragment.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Class that displays and handles user edit profile page where user is prompted
 * to enter new details about their account (first/middle/last name, username, etc.)
 */

package com.example.quizza;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


public class SettingsFragment extends Fragment {

    //TODO: if decided then add profile picture method here !

    Button logoutButton;
    Button editProfileButton;
    Button switchLanguage;
    FirebaseAuth fAuth;
    DatabaseReference currentDatabaseReference;
    TextView userName;
    TextView userEmail;
    TextView userStudentNumber;
    User currentUser;
    RelativeLayout rv_userSettings;
    private PopupWindow mPopupWindow;
    // TODO: update fields like user MiddleName and other added fields in the SIGNUP page !

    final String toImplement = "This feature will be implemented";

    public SettingsFragment() {}
    private void showSelectPop() {
        // contentView
        View contentView = LayoutInflater.from(getContext())
                .inflate(R.layout.pop_select_language, null);
        mPopupWindow = new PopupWindow(contentView);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // listener
        contentView.findViewById(R.id.tv_default_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                toSetLanguage(0);
            }
        });

        contentView.findViewById(R.id.tv_chinese).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                toSetLanguage(1);
            }
        });

        // 外部是否可以点击
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //设置动画
        mPopupWindow.setAnimationStyle(R.style.betSharePopAnim);
        PopUtils.setBackgroundAlpha(getActivity(), 0.5f);
        // 显示PopupWindow
        mPopupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                PopUtils.setBackgroundAlpha(getActivity(), 1.0f);
            }
        });
    }
    private void toSetLanguage(int type) {
        Locale locale;
        Context context =getContext();
        if (type == 0) {
            locale=Locale.US;
        }else if (type == 1) {
            locale = Locale.SIMPLIFIED_CHINESE;

        }
        else {
            return;
        }

        LanguageUtil.updateLanguage(context, locale);
        Intent intent = new Intent(getContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        userName = (TextView) view.findViewById(R.id.tv_userName);
        userEmail = (TextView) view.findViewById(R.id.tv_userEmail);
        userStudentNumber = (TextView) view.findViewById(R.id.tv_userStudentNumber);
        editProfileButton = (Button) view.findViewById(R.id.bt_editProfile);
        logoutButton = (Button) view.findViewById(R.id.bt_logoutButton);
        switchLanguage=(Button)view.findViewById(R.id.bt_switchlanguage);

        rv_userSettings = (RelativeLayout) view.findViewById(R.id.rv_userSettings);

        currentDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        currentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item_snapshot:snapshot.getChildren()) {
                    if (item_snapshot.getKey().equals
                            (FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        currentUser = item_snapshot.getValue(User.class);
                        userName.setText(currentUser.getUserName());
                        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.flFragment, editProfileFragment).addToBackStack(null).commit();
            }
        });

        fAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getActivity(), LoginPage.class));
                getActivity().finish();
            }
        });
        switchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectPop();
            }
        });
        return view;
    }

}
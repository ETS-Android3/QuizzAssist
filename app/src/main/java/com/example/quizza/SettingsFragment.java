/***
 * SettingsFragment.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Class that displays and handles user edit profile page where user is prompted
 * to enter new details about their account (first/middle/last name, username, etc.)
 */

package com.example.quizza;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class SettingsFragment extends BottomSheetDialogFragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextInputEditText userNameEditProfile;
    TextInputEditText userFirstNameEditProfile;
    TextInputEditText userMiddleNameEditProfile;
    TextInputEditText userLastNameEditProfile;
    TextInputEditText userEmailEditProfile;
    TextInputEditText userStudentNumberEditProfile;

    Button editProfileButton;
    Button logoutButton;
    Button switchLanguage;
    Button saveUserProfileChangesButton;

    User currentUser;

    LinearLayout userAvatarSettingsMenu;
    BottomSheetBehavior sheetBehavior;

    FirebaseAuth myFirebaseAuthenticator;
    DatabaseReference myFirebaseReference;

    ImageView backToUserProfile;
    ImageView changeUserAvatar;

    FirebaseAuth fAuth;
    DatabaseReference currentDatabaseReference;

    RelativeLayout userProfileView;
    RelativeLayout userProfileEditView;

    TextView userNameProfile;
    TextView userFullNameProfile;
    TextView userEmailProfile;
    TextView userStudentNumberProfile;

    TextView userAvatarTakePhoto;
    TextView userAvatarChooseFromLibrary;
    TextView userAvatarRemovePhoto;
    TextView userAvatarCancel;

    String currentPhotoPath;
    TextView userName;
    TextView userEmail;
    TextView userStudentNumber;
    RelativeLayout rv_userSettings;
    private PopupWindow mPopupWindow;
    // TODO: update fields like user MiddleName and other added fields in the SIGNUP page !

    Bitmap bitmap;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        userNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userName);
        userFirstNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userFirstName);
        userMiddleNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userMiddleName);
        userLastNameEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userLastName);
        userEmailEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userEmail);
        userStudentNumberEditProfile = (TextInputEditText) view.findViewById(R.id.ti_et_userStudentNumber);

        editProfileButton = (Button) view.findViewById(R.id.bt_editProfileProfile);
        logoutButton = (Button) view.findViewById(R.id.bt_logoutButtonProfile);
        saveUserProfileChangesButton = (Button) view.findViewById(R.id.bt_saveUserProfileChanges);

        userProfileView = (RelativeLayout) view.findViewById(R.id.rv_userSettings);
        userProfileEditView = (RelativeLayout) view.findViewById(R.id.rv_editUserSettingsPage);

        userNameProfile = (TextView) view.findViewById(R.id.tv_userName);
        userFullNameProfile = (TextView) view.findViewById(R.id.tv_userFullName);
        userEmailProfile = (TextView) view.findViewById(R.id.tv_userEmail);
        userStudentNumberProfile = (TextView) view.findViewById(R.id.tv_userStudentNumber);

        userAvatarTakePhoto = (TextView) view.findViewById(R.id.tv_userAvatarTakePhoto);
        //userAvatarChooseFromLibrary = (TextView) view.findViewById(R.id.tv_userAvatarLibrary);
        //userAvatarRemovePhoto = (TextView) view.findViewById(R.id.tv_userAvatarRemove);
        userAvatarCancel = (TextView) view.findViewById(R.id.tv_userAvatarCancel);

        backToUserProfile = (ImageView) view.findViewById(R.id.iv_backToUserProfile);
        changeUserAvatar = (ImageView) view.findViewById(R.id.iv_changeUserAvatar);

        userAvatarSettingsMenu = (LinearLayout) view.findViewById(R.id.linLayout_userAvatarSettingsMenu);
        sheetBehavior = BottomSheetBehavior.from(userAvatarSettingsMenu);
        switchLanguage=(Button)view.findViewById(R.id.bt_switchlanguage);

        String userProfileUpdated = "User Profile Updated";
        String userProfileUpdateError = "User Profile Updated";

        //String invalidUserNameError = "Invalid username";
        String emptyUserNameError = "Username cannot be empty";
        String invalidUserFirstNameError = "Invalid first name";
        String emptyUserFirstNameError = "First name cannot be empty";
        String invalidUserMiddleNameError = "Invalid middle name";
        String emptyUserMiddleNameError = "Middle name cannot be empty";
        String invalidUserLastNameError = "Invalid last name";
        String emptyUserLastNameError = "Last name cannot be empty";
        String invalidUserEmailError = "Invalid email"; //I think this is already done automatically
        String emptyUserEmailError = "Email cannot be empty";
        String emptyStudentNumberError = "Student number cannot be empty";

        currentDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        currentDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item_snapshot : snapshot.getChildren()) {
                    if (item_snapshot.getKey().equals(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid())) {
                        currentUser = item_snapshot.getValue(User.class);
                        String currentUserName = currentUser.getUserName();
                        String currentUserFirstName = currentUser.getUserFirstName();
                        String currentUserMiddleName = currentUser.getUserMiddleName();
                        String currentUserLastName = currentUser.getUserLastName();
                        String currentUserEmail = currentUser.getUserEmail();
                        String currentUserStudentNumber = currentUser.getUserStudentNumber();


                        //Set data visible for User Profile Page
                        if (!currentUserMiddleName.isEmpty())
                            userFullNameProfile.setText(currentUserFirstName + " " + currentUserMiddleName
                                    + " " + currentUserLastName);
                        else
                            userFullNameProfile.setText(currentUserFirstName + " " + currentUserLastName);
                        userNameProfile.setText(currentUserName);
                        userEmailProfile.setText(currentUserEmail);
                        userStudentNumberProfile.setText(currentUserStudentNumber);

                        //Set data visible for "Edit Profile" page
                        userNameEditProfile.setText(currentUserName);
                        userFirstNameEditProfile.setText(currentUserFirstName);
                        userMiddleNameEditProfile.setText(currentUserMiddleName);
                        userLastNameEditProfile.setText(currentUserLastName);
                        userEmailEditProfile.setText(currentUserEmail);
                        userStudentNumberEditProfile.setText(currentUserStudentNumber);
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
                userProfileView.setVisibility(View.INVISIBLE);
                userProfileEditView.setVisibility(View.VISIBLE);
            }
        });

        saveUserProfileChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {

                    String userNameString = userNameEditProfile.getText().toString();
                    String userFirstNameString = userFirstNameEditProfile.getText().toString();
                    String userMiddleNameString = userMiddleNameEditProfile.getText().toString();
                    String userLastNameString = userLastNameEditProfile.getText().toString();
                    String userEmailString = userEmailEditProfile.getText().toString();
                    String userStudentNumberString = userStudentNumberEditProfile.getText().toString();

                    //need to check for valid email change
                    if (userNameString.isEmpty() || userFirstNameString.matches(".*\\d.*")
                            || userFirstNameString.isEmpty() || userMiddleNameString.matches(".*\\d.*")
                            || userLastNameString.matches(".*\\d.*") || userLastNameString.isEmpty()
                            || userStudentNumberString.isEmpty()) {

                        if (userNameString.isEmpty())
                            userNameEditProfile.setError(emptyUserNameError);

                        if (userFirstNameString.isEmpty())
                            userFirstNameEditProfile.setError(emptyUserFirstNameError);
                        else if (userFirstNameString.matches(".*\\d.*"))
                            userFirstNameEditProfile.setError((invalidUserFirstNameError));

                        if (userMiddleNameString.matches(".*\\d.*"))
                            userMiddleNameEditProfile.setError((invalidUserMiddleNameError));

                        if (userLastNameString.isEmpty())
                            userLastNameEditProfile.setError(emptyUserLastNameError);
                        else if (userLastNameString.matches(".*\\d.*"))
                            userLastNameEditProfile.setError((invalidUserLastNameError));

                        if (userStudentNumberString.isEmpty())
                            userStudentNumberEditProfile.setError(emptyStudentNumberError);
                        return;
                    }

                    currentUser.setUserName(userNameString);
                    currentUser.setUserFirstName(userFirstNameString);
                    currentUser.setUserMiddleName(userMiddleNameString);
                    currentUser.setUserLastName(userLastNameString);
                    currentUser.setUserEmail(userEmailString);
                    currentUser.setUserStudentNumber(userStudentNumberString);

                    userProfileEditView.setVisibility(View.INVISIBLE);
                    userProfileView.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).setValue(currentUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), userProfileUpdated,
                                                Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getActivity(), userProfileUpdateError,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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

        backToUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileEditView.setVisibility(View.INVISIBLE);
                userProfileView.setVisibility(View.VISIBLE);
            }
        });

        changeUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        userAvatarTakePhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    dispatchTakePictureIntent();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        });

                        /*userAvatarChooseFromLibrary.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "choose photo", Toast.LENGTH_SHORT).show();
                            }
                        });*/

                        /*userAvatarRemovePhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "remove photo", Toast.LENGTH_SHORT).show();
                            }
                        });*/

                        userAvatarCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                        });
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
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

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        OutputStream fOut = null;
        Uri photoURI;

        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            try {
                File photoFile = new File(getActivity().getExternalFilesDir(null), "TEMP.png");
                photoFile.mkdirs();
                photoURI = FileProvider.getUriForFile(getContext(), "com.example.quizza.provider", photoFile);
                Log.d("finished", "end of dispatch");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                StorageReference riversRef = mStorageRef.child("Profile Pictures/" +
                        FirebaseAuth.getInstance().getCurrentUser().getUid() + ".png");
                riversRef.putFile(photoURI);
                if (!photoFile.exists())
                    photoFile.createNewFile();
                try{
                    fOut = new FileOutputStream(photoFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        //goes here
    }

    public File createPhotoFile() throws IOException {
        File storageDir = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        File image = File.createTempFile(FirebaseAuth.getInstance().getUid(), ".png", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
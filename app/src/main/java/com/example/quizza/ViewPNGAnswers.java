package com.example.quizza;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;

public class ViewPNGAnswers extends Activity {

    private ImageView studentAnswerPNG;

    private Button back;

    private StorageReference mStorageRef;

    private String courseName;
    private String eventName;
    private String questionTitle;
    private String studentUID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_png_answers);

        studentAnswerPNG = (ImageView) findViewById(R.id.iv_studentAnswerPNG);
        back = (Button) findViewById(R.id.bt_studentAnswerBack);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        courseName = getIntent().getStringExtra("courseName");
        eventName = getIntent().getStringExtra("eventName");
        questionTitle = getIntent().getStringExtra("questionTitle");
        studentUID = getIntent().getStringExtra("studentUID");

        OutputStream fOut = null;
        Log.d("cc", courseName);
        Log.d("cc", eventName);
        Log.d("cc", questionTitle);
        Log.d("cc", studentUID);
        StorageReference riversRef = mStorageRef.child(courseName + "/" + eventName + "/" + questionTitle + "/" + studentUID + ".png");
        Log.d("question", questionTitle);
        mStorageRef.child(courseName + "/" + eventName + "/" + questionTitle + "/" + studentUID + ".png").
                getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("success", "penbuis");
                Picasso.get().load(uri).into(studentAnswerPNG);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

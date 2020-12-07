package com.example.quizza;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private String questionUID;
    private String studentUID;

    private Question myQuestion;

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
        Log.d("cccourseName", courseName);
        Log.d("cceventName", eventName);
        Log.d("ccquestionTitle", questionTitle);
        Log.d("ccstudentUID", studentUID);

        FirebaseDatabase.getInstance().getReference("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    //Log.d("cc", snap.getValue(Question.class).getQuestionText());
                    if(snap.getValue(Question.class).getQuestionTitle().equals(questionTitle)){
                        myQuestion = snap.getValue(Question.class);
                        questionUID = snap.getKey();
                        mStorageRef.child(courseName + "/" + eventName + "/" + questionUID + "/" + studentUID + ".png").
                                getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("success", "penbuis");
                                Picasso.get().load(uri).into(studentAnswerPNG);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


       /*Log.d("question", questionTitle);
        Log.d("reference", courseName + "/" + eventName + "/" + questionUID + "/" + studentUID + ".png");
        mStorageRef.child(courseName + "/" + eventName + "/" + questionUID + "/" + studentUID + ".png").
                getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("success", "penbuis");
                Picasso.get().load(uri).into(studentAnswerPNG);

            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

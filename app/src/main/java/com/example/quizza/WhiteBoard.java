package com.example.quizza;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WhiteBoard extends AppCompatActivity {

    private View WhiteBoard;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteboard);
        WhiteBoard = findViewById(R.id.WhiteBoard);
        imageView = (ImageView) findViewById(R.id.imageView);
        Button bt_submit = (Button) findViewById(R.id.bt_submit);


        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b = ScreenShot.takescreenshotOfRootView(imageView);
                imageView.setImageBitmap(b);
                WhiteBoard.setBackgroundColor(Color.parseColor("#999999"));


                OutputStream fOut = null;
                Integer counter = 0;

                try {
                    File file = new File(getExternalFilesDir(null), "BigPP" + counter + ".jpg");
                    if (!file.exists())
                        file.createNewFile();
                    try {
                        fOut = new FileOutputStream(file);
                        b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch(Exception e){
                }


            }
        });
    }
}

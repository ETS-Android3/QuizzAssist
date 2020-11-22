package com.example.quizza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.Color;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClassRoom extends AppCompatActivity {
    Context context;
    GridLayout GridLayout;
    Button bt_addEvent;
    TextView textview;
    CardView cardview;
    LinearLayout linearlayout;
    LayoutParams Card_View_Params;
    LayoutParams Text_View_Params;

    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        bt_addEvent = (Button)findViewById(R.id.bt_addEvent);
        context = getApplicationContext();
        GridLayout = (GridLayout)findViewById(R.id.gridLayout_activity_classroom);

        bt_addEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v ){
                AddEvent();
            }
        });

    }

    public void AddEvent(){
        //Initialize the CardView
        cardview = new CardView(context);

        //Creating parameters for CardView
        Card_View_Params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                1.0f);
        Card_View_Params.setMargins(DpToPix(12), DpToPix(12), DpToPix(12)
                , DpToPix(12));
        cardview.setLayoutParams(Card_View_Params);
        cardview.setCardElevation(DpToPix(6));
        cardview.setRadius(DpToPix(12));
        cardview.setBackgroundColor(Color.WHITE);

        //Initialize the LinearLayout
        LinearLayout.LayoutParams Linear_Layout = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        linearlayout.setLayoutParams(Linear_Layout);
        linearlayout.setPadding(0,DpToPix(16),0,0);
        linearlayout.setGravity(Gravity.CENTER);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        //Initialize the TextView
        textview = new TextView(context);
        Text_View_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        Text_View_Params.setMargins(0,DpToPix(12),0,0);
        textview.setText("Event Number");
        textview.setTextColor(Color.BLACK);
        textview.setTextSize(18);

        linearlayout.addView(textview);
        cardview.addView(linearlayout);
        GridLayout.addView(cardview);


    }

    public int DpToPix(float sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        return dpAsPixels;
    }

}

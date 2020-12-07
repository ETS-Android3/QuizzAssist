package com.example.quizza.base;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }

        this.initView();
    }


    public abstract int getLayoutId();

    public abstract void initView();

}

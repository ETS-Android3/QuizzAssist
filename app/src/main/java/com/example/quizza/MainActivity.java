/***
 * MainActivity.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class contains our main home page layout where our fragment layout is implemented.
 * we also have our navigation bar implemented here.
 */

package com.example.quizza;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    ClassDetailsFragment classDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout flFragment = (FrameLayout) findViewById(R.id.flFragment);
        BottomNavigationView bottomNavBar = findViewById(R.id.navigationBar);
        bottomNavBar.setOnNavigationItemSelectedListener(navBarMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new HomeFragment()).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBarMethod =  new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.nav_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_add:
                    fragment = new AddFragment();
                    break;
                case R.id.nav_settings:
                    fragment = new SettingsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
            return true;
        }
    };

}
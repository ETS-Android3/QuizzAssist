/***
 * MainActivity.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class contains our main home page layout where our fragment layout is implemented.
 * we also have our navigation bar implemented here.
 */

package com.example.quizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    ClassDetailsFragment classDetailsFragment;
    Event myEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout flFragment = (FrameLayout) findViewById(R.id.flFragment);
        BottomNavigationView bottomNavBar = findViewById(R.id.navigationBar);
        bottomNavBar.setOnNavigationItemSelectedListener(navBarMethod);

        /*FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    myEvent = itemSnap.getValue(Event.class);

                    try {
                        Date startDate = setDateStyle(myEvent.getStartDate(), myEvent.getStartHour(), myEvent.getStartMns());
                        Date endDate = setDateStyle(myEvent.getEndDate(), myEvent.getEndHours(), myEvent.getEndMns());
                        Date currentDate = new Date();
                        Log.d("startDate", String.valueOf(startDate));
                        Log.d("startDate", String.valueOf(endDate));

                        if(currentDate.after(startDate)){
                            myEvent.setEventStarted(true);
                            Log.d("startStatus", String.valueOf(myEvent.getEventStarted()));
                        }
                        if (currentDate.after(endDate)){
                            myEvent.setEventEnded(true);
                            Log.d("endStatus", String.valueOf(myEvent.getEventEnded()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.d("error", e.getMessage());
                    }
                    FirebaseDatabase.getInstance().getReference("Events/"+itemSnap.getKey()).setValue(myEvent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/



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

    private Date setDateStyle(String dateInput, Integer startH, Integer startMn) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Integer monthInt = 0;
        String month = dateInput.substring(0, 3);
        Integer day = Integer.parseInt(dateInput.substring(5, 6));
        Integer year = Integer.parseInt(dateInput.substring(8, 12));
        monthInt = getMonthInt(month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthInt);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, startH);
        cal.set(Calendar.MINUTE, startMn);
        Date mDate = cal.getTime();
        return mDate;
    }

    private Integer getMonthInt(String month){
        int result = 0;
        if(month.equals("Dec")){
            result = 11;
            return result;
        } else if (month.equals("Nov")){
            result = 10;
            return result;
        }
        else if (month.equals("Oct")){
            result = 9;
            return result;
        }
        else if (month.equals("Sep")){
            result = 8;
            return result;
        }else if (month.equals("Aug")){
            result = 7;
            return result;
        } else if (month.equals("Jul")){
            result = 6;
            return result;
        }else if (month.equals("Jun")){
            result = 5;
            return result;
        } else if (month.equals("May")){
            result = 4;
            return result;
        } else if (month.equals("Apr")){
            result = 3;
            return result;
        } else if (month.equals("Mar")){
            result = 2;
            return result;
        } else if (month.equals("Feb")){
            result = 1;
            return result;
        } else {
            result = 0;
            return result;
        }
    }

}
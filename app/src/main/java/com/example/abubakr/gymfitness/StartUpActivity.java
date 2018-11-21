package com.example.abubakr.gymfitness;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import GettersAndSetters.ClassSchedule;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import ServerLink.ServerSchedule;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void customerlogin(View s){


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();


    }


    public void signUp(View s){

        ServerSchedule serverSchedule = new ServerSchedule();
        String result = serverSchedule.getAllRecords();

        if(result.equals("success")){
            SessionData.schedulesList = serverSchedule.getList();



            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            finish();

        } else if (result.equals("nodata")) {


        } else if (result.equals("failed")) {
            ShowDialog.showToast(getApplicationContext(), "Connection not Available");

        }

    }


}

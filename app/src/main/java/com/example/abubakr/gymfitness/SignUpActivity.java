package com.example.abubakr.gymfitness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Fragments.SignUpFragments.FragmentChooseFitness;
import Fragments.SignUpFragments.FragmentHeightWeight;
import Fragments.SignUpFragments.FragmentLoginInfo;
import Fragments.SignUpFragments.FragmentPersonalInfo;
import GettersAndSetters.ClassCustomers;
import GettersAndSetters.ClassSchedule;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import ServerLink.ServerCustomer;

@SuppressLint("ResourceType")
public class SignUpActivity extends AppCompatActivity {

    public ClassCustomers classCustomers;
    public ClassSchedule classSchedule;
    public FrameLayout frmChooseFitness, frmPersonalInfo, frmHeightWeight, frmEmailPass;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    public FragmentChooseFitness fragmentChooseFitness;
    public FragmentPersonalInfo fragmentPersonalInfo;
    public FragmentHeightWeight fragmentHeightWeight;
    public FragmentLoginInfo fragmentLoginInfo;
    public ServerCustomer serverCustomer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        serverCustomer = new ServerCustomer();

        frmChooseFitness = findViewById(R.id.frmChooseFitness);
        frmPersonalInfo = findViewById(R.id.frmPersonalInfo);
        frmHeightWeight = findViewById(R.id.frmHeightWeight);
        frmEmailPass = findViewById(R.id.frmEmailPass);

        classCustomers = new ClassCustomers();
        classSchedule = new ClassSchedule();

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new FragmentChooseFitness(fragmentManager, this), "fragment");
            ft.commit();
        }

    }

    public void CreateRecord() {
        System.out.println(classCustomers.getSchdId() + "\n" + classCustomers.getName() + "\n" + classCustomers.getDob() + "\n" + classCustomers.getPhone() + "\n" + classCustomers.getGender() + "\n" + classCustomers.getAddress() + "\n" + classCustomers.getHeight() + "\n" + classCustomers.getWeight() + "\n" + classCustomers.getEmail() + "\n" + classCustomers.getPassword() + "\n");

        InputStream inputstream;
        if (classCustomers.getGender().equals("Male"))
            inputstream = SignUpActivity.this.getResources().openRawResource(R.drawable.profile_male);
        else
            inputstream = SignUpActivity.this.getResources().openRawResource(R.drawable.profile_female);

        byte[] imgByte = new byte[0];

        try {
            imgByte = Utils.getBytes(inputstream);
        } catch (IOException e) { e.printStackTrace(); }

        classCustomers.setPicture(imgByte);
        serverCustomer.setClassCustomers(classCustomers);
        if (serverCustomer.Save() > 0) {
            ShowDialog.showToast(getApplicationContext(), "Account Created Successfully");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            finish();
        } else
            ShowDialog.showToast(getApplicationContext(), "Something went wrong while creating your account");
    }

    public void goBack() {
        Intent intent = new Intent(this, StartUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();
    }

    public void nextFrag(Fragment fragment, int currentFrag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        ft.replace(R.id.content_frame, fragment, "fragment");
        ft.commit();
        changeProgressColor(currentFrag);
    }

    public void previousFrag(Fragment fragment, int currentFrag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        ft.replace(R.id.content_frame, fragment, "fragment");
        ft.commit();
        changeProgressColor(currentFrag);
    }

    public void changeProgressColor(int currentFrag) {
        if (currentFrag == 1) {
            frmChooseFitness.setBackgroundResource(R.color.colorAccent);
            frmPersonalInfo.setBackgroundResource(R.color.colorMuted);
            frmHeightWeight.setBackgroundResource(R.color.colorMuted);
            frmEmailPass.setBackgroundResource(R.color.colorMuted);
        } else if (currentFrag == 2) {
            frmChooseFitness.setBackgroundResource(R.color.colorAccent);
            frmPersonalInfo.setBackgroundResource(R.color.colorAccent);
            frmHeightWeight.setBackgroundResource(R.color.colorMuted);
            frmEmailPass.setBackgroundResource(R.color.colorMuted);
        } else if (currentFrag == 3) {
            frmChooseFitness.setBackgroundResource(R.color.colorAccent);
            frmPersonalInfo.setBackgroundResource(R.color.colorAccent);
            frmHeightWeight.setBackgroundResource(R.color.colorAccent);
            frmEmailPass.setBackgroundResource(R.color.colorMuted);
        } else if (currentFrag == 4) {
            frmChooseFitness.setBackgroundResource(R.color.colorAccent);
            frmPersonalInfo.setBackgroundResource(R.color.colorAccent);
            frmHeightWeight.setBackgroundResource(R.color.colorAccent);
            frmEmailPass.setBackgroundResource(R.color.colorAccent);
        }
    }

}

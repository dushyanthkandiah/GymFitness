package com.example.abubakr.gymfitness;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import GettersAndSetters.ClassCustomers;
import GettersAndSetters.ClassSchedule;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerCustomer;
import ServerLink.ServerSchedule;
import ServerLink.ServerTrainers;
import pl.droidsonroids.gif.GifImageView;

@SuppressLint("ResourceType")
public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    public ImageView imgViewProfile;
    public TextView lblEmail, lblSelectedTrainer;
    public EditText txtName, txtDob, txtPhone, txtAddress, txtHeight, txtWeight;
    public RadioGroup rdGrpGender, radioGroupFitnessPlans;
    public byte[] imgByte;
    public GifImageView progressBar;
    public ServerCustomer serverCustomer = new ServerCustomer();
    private ServerSchedule serverSchedule;
    private ServerTrainers serverTrainers;
    public ArrayList<ClassSchedule> scheduleArrayList;
    private SimpleDateFormat sdf;
    private Calendar myCalendar;
    private Validation vd;
    private int schdId = 0;
    private String selectedGender = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        imgViewProfile = findViewById(R.id.imgViewProfile);
        lblEmail = findViewById(R.id.lblEmail);
        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        lblSelectedTrainer = findViewById(R.id.lblSelectedTrainer);
        rdGrpGender = findViewById(R.id.rdGrpGender);
        radioGroupFitnessPlans = findViewById(R.id.radioGroupFitnessPlans);
        progressBar = findViewById(R.id.progressBar);

        vd = new Validation(getApplicationContext());

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                txtDob.setText(sdf.format(myCalendar.getTime()));
            }

        };

        txtDob.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ProfileActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        imgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        radioGroupFitnessPlans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = radioGroupFitnessPlans.getCheckedRadioButtonId();
                View radioButton = radioGroupFitnessPlans.findViewById(radioButtonID);

                try {
                    schdId = radioButton.getId();
                } catch (Exception e) {

                }
            }
        });

        rdGrpGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedGender = radioButton.getText().toString();
            }
        });

        new GetCustomerData().execute();

        InputStream inputstream = ProfileActivity.this.getResources().openRawResource(R.drawable.profile_male);

        try {
            imgByte = Utils.getBytes(inputstream);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void btnUpdate(View s) {
        String[] fieldName = {"Name", "Dob", "Phone", "Address", "Height", "Weight"};
        EditText[] field = {txtName, txtDob, txtPhone, txtAddress, txtHeight, txtWeight};

        if (vd.CheckEmptyText(fieldName, field)) {
            validate();
        }
    }

    private void validate() {
        if (vd.PhoneCheck(txtPhone.getText().toString().trim())) {
            new SaveDetails().execute();
        }
    }


    // asyn for update
    private class SaveDetails extends AsyncTask<Void, Void, Void> {

        boolean result = false;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String email = serverCustomer.getClassCustomers().getEmail();
            serverCustomer = new ServerCustomer();

            serverCustomer.getClassCustomers().setEmail(email);
            serverCustomer.getClassCustomers().setName(txtName.getText().toString().trim());
            serverCustomer.getClassCustomers().setDob(txtDob.getText().toString().trim());
            serverCustomer.getClassCustomers().setPhone(txtPhone.getText().toString().trim());
            serverCustomer.getClassCustomers().setGender(selectedGender);
            serverCustomer.getClassCustomers().setAddress(txtAddress.getText().toString().trim());
            serverCustomer.getClassCustomers().setHeight(Double.valueOf(txtHeight.getText().toString().trim()));
            serverCustomer.getClassCustomers().setWeight(Double.valueOf(txtWeight.getText().toString().trim()));
            serverCustomer.getClassCustomers().setSchdId(schdId);
            serverCustomer.getClassCustomers().setPicture(imgByte);

            if (serverCustomer.Update() > 0) {
                SessionData.sheduleId = String.valueOf(serverCustomer.getClassCustomers().getSchdId());
                result = true;
            }else
                result = false;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);

            if (result)
                ShowDialog.showToast(getApplicationContext(), "Record Updated Successfully");
            else
                ShowDialog.showToast(getApplicationContext(), "Error While Updating the Record");
        }

    }

    // asyn for get from database
    private class GetCustomerData extends AsyncTask<Void, Void, Void> {

        private String result = "", scheduleResult = "", trainerResult = "";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            serverCustomer.getClassCustomers().setEmail(SessionData.cusEmail);

            result = serverCustomer.Search();

            scheduleArrayList = new ArrayList<>();
            serverSchedule = new ServerSchedule();
            scheduleResult = serverSchedule.getAllRecords();

            serverTrainers = new ServerTrainers();

            int trainerId = 0;

            try {
                trainerId = serverCustomer.getClassCustomers().getTrainId();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            System.out.println(trainerId + " *********");

            serverTrainers.getClassTrainers().setId(trainerId);
            trainerResult = serverTrainers.Search();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            if (result.equals("success")) {

                lblEmail.setText(serverCustomer.getClassCustomers().getEmail());
                txtName.setText(serverCustomer.getClassCustomers().getName());
                txtDob.setText(serverCustomer.getClassCustomers().getDob());
                txtPhone.setText(serverCustomer.getClassCustomers().getPhone());
                txtAddress.setText(serverCustomer.getClassCustomers().getAddress());
                txtHeight.setText(serverCustomer.getClassCustomers().getHeight() + "");
                txtWeight.setText(serverCustomer.getClassCustomers().getWeight() + "");
                imgByte = serverCustomer.getClassCustomers().getPicture();

                imgViewProfile.setImageBitmap(Utils.getImage(imgByte));
                selectedGender = serverCustomer.getClassCustomers().getGender();

                try {
                    Date date = sdf.parse(serverCustomer.getClassCustomers().getDob());
                    myCalendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (selectedGender.equals("Male"))
                    rdGrpGender.check(R.id.rbtnMale);
                else
                    rdGrpGender.check(R.id.rbtnFemale);


                if (trainerResult.equals("success")){
                    lblSelectedTrainer.setText("Selected Trainer : " + serverTrainers.getClassTrainers().getName());
                }else if (trainerResult.equals("nodata")){
                    lblSelectedTrainer.setText("Selected Trainer : You haven't selected one");

                }else {
                    ShowDialog.showToast(getApplicationContext(), "Connection not Available");
                }

                if (scheduleResult.equals("success")) {
                    scheduleArrayList = serverSchedule.getList();
                    createChecks();
                } else {
                    ShowDialog.showToast(getApplicationContext(), "Connection not Available");
                }


            } else if (result.equals("nodata")) {

            } else if (result.equals("failed")) {
                ShowDialog.showToast(getApplicationContext(), "Connection not Available");

            }

        }

    }

    // Create dynamic checkboxes
    public void createChecks() {

        for (int i = 0; i < scheduleArrayList.size(); i++) {

            RadioButton rbtn = new RadioButton(this);
            rbtn.setId(scheduleArrayList.get(i).getSchdId());

            if (scheduleArrayList.get(i).getSchdId() == serverCustomer.getClassCustomers().getSchdId()) {
                rbtn.setChecked(true);
                schdId = serverCustomer.getClassCustomers().getSchdId();
            }
            rbtn.setText(scheduleArrayList.get(i).getType());
            rbtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            if (Build.VERSION.SDK_INT >= 21) {

                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_enabled}, //disabled
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[]{
                                Color.BLACK //disabled
                                , getResources().getColor(R.color.colorPrimaryDark) //enabled
                        }
                );

                rbtn.setButtonTintList(colorStateList);
                rbtn.invalidate();
            }

            radioGroupFitnessPlans.addView(rbtn);
        }

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1 && data != null) {
                Uri imageUri = data.getData();
                InputStream iStream;

                iStream = getContentResolver().openInputStream(imageUri);

                imgByte = Utils.getBytes(iStream, imgViewProfile);

            } else {
                ShowDialog.showToast(getApplicationContext(), "No Images Selected");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();
    }
}

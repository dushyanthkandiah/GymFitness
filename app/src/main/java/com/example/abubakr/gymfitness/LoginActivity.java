package com.example.abubakr.gymfitness;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Validation;
import ServerLink.ServerCustomer;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {

    public EditText txtEmail, txtPassword;
    public Validation vd;
    public ServerCustomer serverCustomer;

    public GifImageView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        progressBar = findViewById(R.id.progressBar);

        vd = new Validation(getApplicationContext());
        serverCustomer = new ServerCustomer();

    }

    public void btnLogin(View c) {

        String[] fieldName = {"Email", "Password"};
        EditText[] field = {txtEmail, txtPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            new LoadLogin().execute();
        }
    }

    private class LoadLogin extends AsyncTask<Void, Void, Void> {

        private String result = "", email = "", password = "";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            email = txtEmail.getText().toString().trim().toLowerCase();
            password = txtPassword.getText().toString();
            serverCustomer.getClassCustomers().setEmail(email);
            serverCustomer.getClassCustomers().setPassword(password);

            result = serverCustomer.Search();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);

            if (result.equals("success")) {

                if (serverCustomer.getClassCustomers().getPassword().equals(password)) {

                    if (!serverCustomer.getClassCustomers().getStatus().equals("inactive")) {

                        SessionData.cusEmail = serverCustomer.getClassCustomers().getEmail();
                        SessionData.sheduleId = String.valueOf(serverCustomer.getClassCustomers().getSchdId());
                        SessionData.cusId = serverCustomer.getClassCustomers().getId();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                        finish();
                    } else
                        ShowDialog.showToast(getApplicationContext(), "Sorry Your account has been blocked");
                } else
                    ShowDialog.showToast(getApplicationContext(), "Please check your Email/Password");

            } else if (result.equals("nodata")) {
                ShowDialog.showToast(getApplicationContext(), "Please check your Email/Password");

            } else if (result.equals("failed")) {
                ShowDialog.showToast(getApplicationContext(), "Connection not Available");

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StartUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();

    }

}

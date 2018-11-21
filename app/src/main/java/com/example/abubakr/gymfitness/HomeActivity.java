package com.example.abubakr.gymfitness;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Fragments.FragmentCustomerSupport;
import Fragments.FragmentHome;
import Fragments.FragmentStore;
import GettersAndSetters.ClassSchedule;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import ServerLink.ServerCustomer;
import ServerLink.ServerSchedule;
import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentManager fragmentManager = getSupportFragmentManager();
    private GifImageView progressBar;
    public NavigationView navigationView;
    public EditText txtSearch;
    private TextView lblFragmentTitle;
    public FragmentHome fragmentHome;
    Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        progressBar = findViewById(R.id.progressBar);
        txtSearch = findViewById(R.id.txtSearch);
        lblFragmentTitle = findViewById(R.id.lblFragmentTitle);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        fragmentHome = new FragmentHome(fragmentManager, this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            fragmentHome = new FragmentHome(fragmentManager, this);
            unCheck3Dots(true);
            lblFragmentTitle.setVisibility(View.GONE);
            txtSearch.setVisibility(View.VISIBLE);
            ft.replace(R.id.content_frame, fragmentHome, "fragment");
            ft.commit();
        }

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fragmentHome.searchRecords(txtSearch.getText().toString().trim().replace("'", "''"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        View rootView = navigationView.getHeaderView(0);
        populateNavProfile(rootView);

    }

    private void populateNavProfile(View rootView){
        ImageView userImageView;
        TextView lblUserName, lblUserEmail;
        LinearLayout layoutUserProfile;

        layoutUserProfile = rootView.findViewById(R.id.layoutUserProfile);
        userImageView = rootView.findViewById(R.id.userImageView);
        lblUserName = rootView.findViewById(R.id.lblUserName);
        lblUserEmail = rootView.findViewById(R.id.lblUserEmail);

        ServerCustomer serverCustomer = new ServerCustomer();
        serverCustomer.getClassCustomers().setEmail(SessionData.cusEmail);
        if(serverCustomer.Search().equals("success")) {

            lblUserEmail.setText(serverCustomer.getClassCustomers().getEmail());
            lblUserName.setText(serverCustomer.getClassCustomers().getName());
            userImageView.setImageBitmap(Utils.getImage(
                    serverCustomer.getClassCustomers().getPicture()
            ));

        }

        layoutUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                drawer.closeDrawer(GravityCompat.START);
                finish();

            }
        });

    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        ServerSchedule serverSchedule = new ServerSchedule();
        serverSchedule.getAllRecords();
        ArrayList<ClassSchedule> list = serverSchedule.getList();

        menu.add(0, 0, 0, "All").setShortcut('3', 'c');

        for (int i = 0; i < list.size(); i++) {
            menu.add(0, list.get(i).getSchdId(), 0, list.get(i).getType()).setShortcut('3', 'c');
        }

        return true;
    }

    void unCheck3Dots(boolean check){
        for (int i = 0; i < toolbar.getMenu().size(); i++) {

            toolbar.getMenu().getItem(i).setVisible(check);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        fragmentHome.searchRecords(txtSearch.getText().toString().trim().replace("'", "''"), id);
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_home:
                fragmentHome = new FragmentHome(fragmentManager, this);
                unCheck3Dots(true);
                lblFragmentTitle.setVisibility(View.GONE);
                txtSearch.setVisibility(View.VISIBLE);
                ft.replace(R.id.content_frame, fragmentHome, "fragment");
                ft.commit();
                break;

            case R.id.nav_store:
                FragmentStore fragmentStore = new FragmentStore(fragmentManager, this);
                unCheck3Dots(false);
                txtSearch.setVisibility(View.GONE);
                lblFragmentTitle.setVisibility(View.VISIBLE);
                lblFragmentTitle.setText("Store");
                ft.replace(R.id.content_frame, fragmentStore, "fragment");
                ft.commit();
                break;

            case R.id.nav_cus_support:
                FragmentCustomerSupport fragmentCustomerSupport = new FragmentCustomerSupport(fragmentManager, this);
                unCheck3Dots(false);
                txtSearch.setVisibility(View.GONE);
                lblFragmentTitle.setVisibility(View.VISIBLE);
                lblFragmentTitle.setText("Customer Support");
                ft.replace(R.id.content_frame, fragmentCustomerSupport, "fragment");
                ft.commit();
                break;

            case R.id.nav_logout:
                startActivity(new Intent(this, StartUpActivity.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                finish();
                break;

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

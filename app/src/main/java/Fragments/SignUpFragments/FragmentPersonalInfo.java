package Fragments.SignUpFragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.abubakr.gymfitness.R;
import com.example.abubakr.gymfitness.SignUpActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import OtherClasses.ShowDialog;
import OtherClasses.Validation;


@SuppressLint("ValidFragment")
public class FragmentPersonalInfo extends Fragment {

    private View iView;
    private FragmentManager fragmentManager;
    private SignUpActivity signUpActivity;
    private Button btnNext, btnGoBack;
    private EditText txtDob, txtName, txtPhone, txtAddress;
    private SimpleDateFormat sdf;
    private Calendar myCalendar;
    private Validation vd;
    private String selectedGender = "";
    private RadioGroup rdGrpGender;


    public FragmentPersonalInfo(FragmentManager fragmentManager, SignUpActivity signUpActivity) {
        this.fragmentManager = fragmentManager;
        this.signUpActivity = signUpActivity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_personal_info, container, false);
        vd = new Validation(getActivity());
        btnNext = iView.findViewById(R.id.btnNext);
        btnGoBack = iView.findViewById(R.id.btnGoBack);
        txtDob = iView.findViewById(R.id.txtDob);
        txtName = iView.findViewById(R.id.txtName);
        txtPhone = iView.findViewById(R.id.txtPhone);
        txtAddress = iView.findViewById(R.id.txtAddress);
        rdGrpGender = iView.findViewById(R.id.rdGrpGender);

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

                new DatePickerDialog(getActivity(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity.fragmentPersonalInfo = FragmentPersonalInfo.this;
                signUpActivity.previousFrag(signUpActivity.fragmentChooseFitness, 1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();


            }
        });

        rdGrpGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = iView.findViewById(checkedId);
                selectedGender = radioButton.getText().toString();
            }
        });

        return iView;
    }

    private void validate() {
        String[] fieldName = {"Name", "Date of Birth", "Phone Number", "Address"};
        EditText[] field = {txtName, txtDob, txtPhone, txtAddress};

        if (vd.CheckEmptyText(fieldName, field)) {
            if(vd.PhoneCheck(txtPhone.getText().toString().trim())) {
                if (!selectedGender.equals(""))
                    getFieldValues();
                else
                    ShowDialog.showToast(getActivity(), "Please Select a Gender");
            }

        }
    }

    private void getFieldValues(){

        signUpActivity.classCustomers.setName(txtName.getText().toString().trim());
        signUpActivity.classCustomers.setDob(txtDob.getText().toString().trim());
        signUpActivity.classCustomers.setPhone(txtPhone.getText().toString().trim());
        signUpActivity.classCustomers.setGender(selectedGender);
        signUpActivity.classCustomers.setAddress(txtAddress.getText().toString().trim());

        if (signUpActivity.fragmentHeightWeight == null) {
            signUpActivity.fragmentPersonalInfo = FragmentPersonalInfo.this;
            signUpActivity.nextFrag(new FragmentHeightWeight(fragmentManager, signUpActivity), 3);
        } else {
            signUpActivity.nextFrag(signUpActivity.fragmentHeightWeight, 3);
        }
    }

}

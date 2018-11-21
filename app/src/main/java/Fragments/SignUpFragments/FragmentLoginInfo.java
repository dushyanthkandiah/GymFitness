package Fragments.SignUpFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.abubakr.gymfitness.R;
import com.example.abubakr.gymfitness.SignUpActivity;

import OtherClasses.Validation;

@SuppressLint("ValidFragment")
public class FragmentLoginInfo extends Fragment {

    private View iView;
    private FragmentManager fragmentManager;
    private SignUpActivity signUpActivity;
    private Button btnNext, btnGoBack;
    private Validation vd;
    private EditText txtEmail, txtPassword, txtConfirmPassword;

    public FragmentLoginInfo(FragmentManager fragmentManager, SignUpActivity signUpActivity) {
        this.fragmentManager = fragmentManager;
        this.signUpActivity = signUpActivity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        iView = inflater.inflate(R.layout.fragment_login_info, container, false);
        vd = new Validation(getActivity());
        btnNext = iView.findViewById(R.id.btnNext);
        btnGoBack = iView.findViewById(R.id.btnGoBack);
        txtEmail = iView.findViewById(R.id.txtEmail);
        txtPassword = iView.findViewById(R.id.txtPassword);
        txtConfirmPassword = iView.findViewById(R.id.txtConfirmPassword);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity.fragmentLoginInfo = FragmentLoginInfo.this;
                signUpActivity.previousFrag(signUpActivity.fragmentHeightWeight, 3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        return iView;
    }

    private void validate(){

        String[] fieldName = {"Email", "Password", "Confirm Password"};
        EditText[] field = {txtEmail, txtPassword, txtConfirmPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            if (vd.EmailCheck(field[0].getText().toString().trim()))
            if (vd.PasswordCheck(field[1].getText().toString(), field[2].getText().toString())){
                if (vd.PasswordCheck(field[1].getText().toString())){
                    getFieldValues();
                }
            }
        }
    }

    private void getFieldValues(){
        signUpActivity.classCustomers.setEmail(txtEmail.getText().toString().trim().toLowerCase());
        signUpActivity.classCustomers.setPassword(txtPassword.getText().toString());
        signUpActivity.classCustomers.setStatus("active");

        signUpActivity.CreateRecord();

    }

}

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
public class FragmentHeightWeight extends Fragment {

    private View iView;
    private FragmentManager fragmentManager;
    private SignUpActivity signUpActivity;
    private Button btnNext, btnGoBack;
    private EditText txtHeight, txtWeight;
    private Validation vd;

    public FragmentHeightWeight(FragmentManager fragmentManager, SignUpActivity signUpActivity) {
        this.fragmentManager = fragmentManager;
        this.signUpActivity = signUpActivity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_height_weight, container, false);
        vd = new Validation(getActivity());
        btnNext = iView.findViewById(R.id.btnNext);
        btnGoBack = iView.findViewById(R.id.btnGoBack);
        txtHeight = iView.findViewById(R.id.txtHeight);
        txtWeight = iView.findViewById(R.id.txtWeight);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity.fragmentHeightWeight = FragmentHeightWeight.this;
                signUpActivity.previousFrag(signUpActivity.fragmentPersonalInfo, 2);
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

        String[] fieldName = {"Height", "Weight"};
        EditText[] field = {txtHeight, txtWeight};

        if (vd.CheckEmptyText(fieldName, field)) {
            getFieldValues();
        }
    }

    private void getFieldValues(){
        signUpActivity.classCustomers.setHeight(Double.parseDouble(txtHeight.getText().toString().trim()));
        signUpActivity.classCustomers.setWeight(Double.parseDouble(txtWeight.getText().toString().trim()));

        if (signUpActivity.fragmentLoginInfo == null) {
            signUpActivity.fragmentHeightWeight = FragmentHeightWeight.this;
            signUpActivity.nextFrag(new FragmentLoginInfo(fragmentManager, signUpActivity), 4);
        }else {
            signUpActivity.nextFrag(signUpActivity.fragmentLoginInfo, 4);
        }

    }

}

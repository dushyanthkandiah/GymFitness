package Fragments.SignUpFragments;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.abubakr.gymfitness.R;
import com.example.abubakr.gymfitness.SignUpActivity;

import java.util.ArrayList;

import Fragments.FragmentHome;
import GettersAndSetters.ClassSchedule;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;

@SuppressLint("ValidFragment")

public class FragmentChooseFitness extends Fragment {

    private View iView;
    private FragmentManager fragmentManager;
    private SignUpActivity signUpActivity;
    private RadioGroup radioGroupFitnessPlans;
    private Button btnNext, btnGoBack;

    public FragmentChooseFitness(FragmentManager fragmentManager, SignUpActivity signUpActivity) {
        this.fragmentManager = fragmentManager;
        this.signUpActivity = signUpActivity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_choose_fitness, container, false);

        radioGroupFitnessPlans = iView.findViewById(R.id.radioGroupFitnessPlans);
        btnNext = iView.findViewById(R.id.btnNext);
        btnGoBack = iView.findViewById(R.id.btnGoBack);

        createChecks();

        radioGroupFitnessPlans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = radioGroupFitnessPlans.getCheckedRadioButtonId();
                View radioButton = radioGroupFitnessPlans.findViewById(radioButtonID);
//                int idx = radioGroupFitnessPlans.indexOfChild(radioButton);

                signUpActivity.classCustomers.setSchdId(radioButton.getId());
//                ShowDialog.showToast(getActivity(),radioButton.getId()+ "");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity.goBack();
            }
        });

        return iView;
    }

    private void validate() {
        if (radioGroupFitnessPlans.getCheckedRadioButtonId() != -1) {
            if (signUpActivity.fragmentPersonalInfo == null) {
                signUpActivity.fragmentChooseFitness = FragmentChooseFitness.this;
                signUpActivity.nextFrag(new FragmentPersonalInfo(fragmentManager, signUpActivity), 2);
            } else {
                signUpActivity.nextFrag(signUpActivity.fragmentPersonalInfo, 2);
            }
        }else {
            ShowDialog.showToast(getActivity(), "Please select a Fitness Plan");
        }
    }


    // Create dynamic checkboxes
    private void createChecks() {
        ArrayList<ClassSchedule> list = SessionData.schedulesList;
        for (int i = 0; i < list.size(); i++) {

            RadioButton rbtn = new RadioButton(getActivity());
            rbtn.setId(list.get(i).getSchdId());

            rbtn.setText(list.get(i).getType());
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


}

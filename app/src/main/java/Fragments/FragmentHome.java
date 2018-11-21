package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abubakr.gymfitness.HomeActivity;
import com.example.abubakr.gymfitness.R;

import Adapters.PagerAdapter;
import OtherClasses.SessionData;

@SuppressLint("ValidFragment")
public class FragmentHome extends Fragment {

    private View iView;
    private FragmentManager fragmentManager;
    public HomeActivity homeActivity;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private FragmentTrainers fragmentTrainers;
    private FragmentNutritions fragmentNutritions;
    private FragmentExercises fragmentExercises;

    public FragmentHome(FragmentManager fragmentManager, HomeActivity homeActivity) {
        this.fragmentManager = fragmentManager;
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        iView = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = iView.findViewById(R.id.tabLayoutParent);
        viewPager = iView.findViewById(R.id.viewPagerParent);

        fragmentTrainers = new FragmentTrainers(this);
        fragmentNutritions = new FragmentNutritions(this);
        fragmentExercises = new FragmentExercises(this);

        pagerAdapter = new PagerAdapter(fragmentManager);
        pagerAdapter.addFragments(fragmentTrainers, "Trainers");
        pagerAdapter.addFragments(fragmentNutritions, "Nutrition");
        pagerAdapter.addFragments(fragmentExercises, "Exercises");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.trainer_select);
        tabLayout.getTabAt(1).setIcon(R.drawable.nutrition_unselect);
        tabLayout.getTabAt(2).setIcon(R.drawable.exercise_unselect);

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        if (tab.getPosition() == 0) {
                            tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.trainer_select);
                            SessionData.currentFragment = "trainer";
                        } else if (tab.getPosition() == 1) {
                            tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.nutrition_select);
                            SessionData.currentFragment = "nutrition";
                        } else if (tab.getPosition() == 2) {
                            tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.exercise_select);
                            SessionData.currentFragment = "exercise";
                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        if (tab.getPosition() == 0) {
                            tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.trainer_unselect);

                        } else if (tab.getPosition() == 1) {
                            tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.nutrition_unselect);

                        }else if (tab.getPosition() == 2) {
                            tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.exercise_unselect);

                        }

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        return iView;
    }

    public void searchRecords(String inputStr, int schdId){
//        System.out.println(schdId + "*************");

        if (SessionData.currentFragment.equals("trainer"))
            fragmentTrainers.serverTrainers.getClassTrainers().setSchdId(schdId);
        else if (SessionData.currentFragment.equals("nutrition"))
            fragmentNutritions.serverNutrition.getClassNutritions().setSchdId(schdId);
        else if (SessionData.currentFragment.equals("exercise"))
            fragmentExercises.serverExercise.getClassExercise().setSchdId(schdId);


        callSearchInFragment(inputStr);

    }

    public void searchRecords(String inputStr){
        callSearchInFragment(inputStr);

    }

    void callSearchInFragment(String inputStr){
        fragmentTrainers.inputStr = inputStr;
        fragmentNutritions.inputStr = inputStr;
        fragmentExercises.inputStr = inputStr;

        if (SessionData.currentFragment.equals("trainer")){
            fragmentTrainers.SearchData();
        }else if (SessionData.currentFragment.equals("nutrition")){
            fragmentNutritions.SearchData();
        }else if (SessionData.currentFragment.equals("exercise")){
            fragmentExercises.SearchData();
        }
    }

}

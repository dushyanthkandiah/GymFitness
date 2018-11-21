package Dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abubakr.gymfitness.R;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;

import Adapters.SupplementViewAdapter;
import Fragments.FragmentStore;
import Fragments.FragmentTrainers;
import GettersAndSetters.ClassSupplement;
import GettersAndSetters.ClassTrainers;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import ServerLink.ServerCustomer;
import pl.droidsonroids.gif.GifImageView;

import static android.content.ContentValues.TAG;


@SuppressLint({"ValidFragment", "SetTextI18n"})
public class DialogTrainer extends BaseDialogFragment<DialogTrainer.OnDialogFragmentClickListener> {

    private FragmentTrainers fragmentTrainers;
    private View iView;
    ClassTrainers classTrainers;
    private ImageView imgViewTrainer;
    private TextView lblTrainerName, lblTrainerGender, lblTrainerDob, lblTrainerPhone, lblTrainerExperience, lblTrainerAddress;
    private Button btnSelectTrainer;


    public static DialogTrainer newInstance(FragmentTrainers fragmentTrainers, ClassTrainers classTrainers) {
        DialogTrainer dialogSelectSupplement = new DialogTrainer(fragmentTrainers, classTrainers);
        return dialogSelectSupplement;
    }

    public DialogTrainer(FragmentTrainers fragmentTrainers, ClassTrainers classTrainers) {
        this.fragmentTrainers = fragmentTrainers;
        this.classTrainers = classTrainers;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_trainer, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRetainInstance(true);

        imgViewTrainer = iView.findViewById(R.id.imgViewTrainer);
        lblTrainerName = iView.findViewById(R.id.lblTrainerName);
        lblTrainerGender = iView.findViewById(R.id.lblTrainerGender);
        lblTrainerDob = iView.findViewById(R.id.lblTrainerDob);
        lblTrainerPhone = iView.findViewById(R.id.lblTrainerPhone);
        lblTrainerExperience = iView.findViewById(R.id.lblTrainerExperience);
        lblTrainerAddress = iView.findViewById(R.id.lblTrainerAddress);
        btnSelectTrainer = iView.findViewById(R.id.btnSelectTrainer);

        populateTrainerData();

        btnSelectTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCustomer serverCustomer = new ServerCustomer();
                serverCustomer.getClassCustomers().setTrainId(classTrainers.getId());

                if (serverCustomer.SelectTrainer() > 0){
                    ShowDialog.showToast(getActivity(), "You have selected this trainer");
                }else
                    ShowDialog.showToast(getActivity(), "Something went wrong");
            }
        });

        return iView;
    }


    private void populateTrainerData(){
        Period age = new Period(LocalDate.parse(classTrainers.getWorkExperience()), LocalDate.now());
        imgViewTrainer.setImageBitmap(Utils.getImage(classTrainers.getPicture()));
        lblTrainerName.setText("" + classTrainers.getName());
        lblTrainerGender.setText("Gender : " + classTrainers.getGender());
        lblTrainerDob.setText("Date of Birth : " + classTrainers.getDob());
        lblTrainerPhone.setText("Phone : " + classTrainers.getPhone());
        lblTrainerAddress.setText("Address : " + classTrainers.getAddress());
        lblTrainerExperience.setText("Experience : " + age.getYears() + " Years & " + age.getMonths() + " Months");

    }


    public interface OnDialogFragmentClickListener {

    }
}

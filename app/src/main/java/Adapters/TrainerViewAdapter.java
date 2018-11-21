package Adapters;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abubakr.gymfitness.R;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;

import Dialogs.DialogSelectSupplement;
import Dialogs.DialogTrainer;
import Fragments.FragmentStore;
import Fragments.FragmentTrainers;
import GettersAndSetters.ClassTrainers;
import OtherClasses.StringChange;
import OtherClasses.Utils;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class TrainerViewAdapter extends RecyclerView.Adapter<TrainerViewAdapter.VHolder> {
    private ArrayList<ClassTrainers> data;
    private FragmentTrainers fragmentTrainers;

    public TrainerViewAdapter(ArrayList<ClassTrainers> data, FragmentTrainers fragmentTrainers) {
        this.data = data;
        this.fragmentTrainers = fragmentTrainers;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentTrainers.getActivity());
        View view = inflater.inflate(R.layout.trainer_recycler_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        Period age = new Period(LocalDate.parse(data.get(position).getWorkExperience()), LocalDate.now());

        StringChange.MakeColoredText(fragmentTrainers.getActivity(), R.color.colorPrimaryDark, holder.lblTrainerName, "Name : ", data.get(position).getName());
        holder.lblTrainerGender.setText("Gender : " + data.get(position).getGender());
        holder.lblTrainerExperience.setText("Experience : " + age.getYears() + " Years & " + age.getMonths() + " Months");
        holder.imgViewTrainer.setImageBitmap(Utils.getImage(data.get(position).getPicture()));

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTrainer dialogTrainer = DialogTrainer.newInstance(fragmentTrainers, data.get(position));
                dialogTrainer.show(fragmentTrainers.getFragmentManager(), "dialog");
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblTrainerName, lblTrainerGender, lblTrainerExperience;
        CardView cardClick;
        ImageView imgViewTrainer;

        public VHolder(View itemView) {
            super(itemView);
            lblTrainerName = itemView.findViewById(R.id.lblTrainerName);
            lblTrainerGender = itemView.findViewById(R.id.lblTrainerGender);
            lblTrainerExperience = itemView.findViewById(R.id.lblTrainerExperience);
            imgViewTrainer = itemView.findViewById(R.id.imgViewTrainer);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }

}

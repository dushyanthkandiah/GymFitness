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
import android.widget.TextView;

import com.example.abubakr.gymfitness.R;
import com.mysql.jdbc.Util;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;

import Fragments.FragmentNutritions;
import Fragments.FragmentTrainers;
import GettersAndSetters.ClassNutritions;
import GettersAndSetters.ClassTrainers;
import OtherClasses.StringChange;
import OtherClasses.Utils;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class NutritionViewAdapter extends RecyclerView.Adapter<NutritionViewAdapter.VHolder> {
    private ArrayList<ClassNutritions> data;
    private FragmentNutritions fragmentNutritions;

    public NutritionViewAdapter(ArrayList<ClassNutritions> data, FragmentNutritions fragmentNutritions) {
        this.data = data;
        this.fragmentNutritions = fragmentNutritions;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentNutritions.getActivity());
        View view = inflater.inflate(R.layout.nutrition_recycler_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {

        StringChange.MakeColoredText(fragmentNutritions.getActivity(), R.color.colorPrimaryDark, holder.lblFood, "Food : ", data.get(position).getFood());
        holder.lblFoodType.setText("Type : " + data.get(position).getType());
        holder.imgViewNutrition.setImageBitmap(Utils.getImage(data.get(position).getPicture()));

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblFood, lblFoodType;
        CardView cardClick;
        ImageView imgViewNutrition;

        public VHolder(View itemView) {
            super(itemView);
            lblFood = itemView.findViewById(R.id.lblFood);
            lblFoodType = itemView.findViewById(R.id.lblFoodType);
            cardClick = itemView.findViewById(R.id.cardClick);
            imgViewNutrition = itemView.findViewById(R.id.imgViewNutrition);
        }
    }

}

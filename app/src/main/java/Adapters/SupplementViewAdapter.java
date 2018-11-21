package Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abubakr.gymfitness.R;
import java.util.ArrayList;

import Dialogs.DialogSelectSupplement;
import GettersAndSetters.ClassSupplement;
import OtherClasses.StringChange;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class SupplementViewAdapter extends RecyclerView.Adapter<SupplementViewAdapter.VHolder>{

    private ArrayList<ClassSupplement> data;
    private DialogSelectSupplement dialogSelectSupplement;

    public SupplementViewAdapter(ArrayList<ClassSupplement> data, DialogSelectSupplement dialogSelectSupplement) {
        this.data = data;
        this.dialogSelectSupplement = dialogSelectSupplement;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(dialogSelectSupplement.getActivity());
        View view = inflater.inflate(R.layout.supplement_recycler_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        StringChange.MakeColoredText(dialogSelectSupplement.getActivity(), R.color.colorPrimary, holder.lblSupplementName, data.get(position).getType() + " Name : ", data.get(position).getName());
        StringChange.MakeColoredText(dialogSelectSupplement.getActivity(), R.color.colorPrimary, holder.lblSupplementStock, "Stock : ", data.get(position).getStock() + "");
        StringChange.MakeColoredText(dialogSelectSupplement.getActivity(), R.color.colorPrimary, holder.lblSupplementPrice, "Price : ", data.get(position).getPrice() + " MYR");

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectSupplement.fragmentStore.setSupplementData(data.get(position));
                dialogSelectSupplement.dismiss();
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView lblSupplementName, lblSupplementStock, lblSupplementPrice;
        CardView cardClick;

        public VHolder(View itemView) {
            super(itemView);
            lblSupplementName = itemView.findViewById(R.id.lblSupplementName);
            lblSupplementStock = itemView.findViewById(R.id.lblSupplementStock);
            lblSupplementPrice = itemView.findViewById(R.id.lblSupplementPrice);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }
}

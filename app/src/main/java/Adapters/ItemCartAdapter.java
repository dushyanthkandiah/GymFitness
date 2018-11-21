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

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;

import Fragments.FragmentHome;
import Fragments.FragmentStore;
import Fragments.FragmentTrainers;
import GettersAndSetters.ClassItemCart;
import GettersAndSetters.ClassTrainers;
import OtherClasses.StringChange;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.VHolder> {
    private ArrayList<ClassItemCart> data;
    private FragmentStore fragmentStore;

    public ItemCartAdapter(ArrayList<ClassItemCart> data, FragmentStore fragmentStore) {
        this.data = data;
        this.fragmentStore = fragmentStore;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentStore.getActivity());
        View view = inflater.inflate(R.layout.recyclerview_design_itemcart, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.lblItemId.setText((position+1)+"");
        holder.lblItemName.setText(data.get(position).getItemName()+"");
        holder.lblItemQty.setText(data.get(position).getQuantity()+"");
        holder.lblItemSubTotal.setText(data.get(position).getSubTotal()+"");
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblItemId, lblItemName, lblItemQty, lblItemSubTotal;

        public VHolder(View itemView) {
            super(itemView);
            lblItemId = itemView.findViewById(R.id.lblItemId);
            lblItemName = itemView.findViewById(R.id.lblItemName);
            lblItemQty = itemView.findViewById(R.id.lblItemQty);
            lblItemSubTotal = itemView.findViewById(R.id.lblItemSubTotal);
        }
    }

}

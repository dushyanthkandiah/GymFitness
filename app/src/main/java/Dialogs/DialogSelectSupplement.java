package Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.abubakr.gymfitness.R;

import java.util.ArrayList;

import Adapters.NutritionViewAdapter;
import Adapters.SupplementViewAdapter;
import Fragments.FragmentStore;
import GettersAndSetters.ClassNutritions;
import GettersAndSetters.ClassSupplement;
import OtherClasses.ShowDialog;
import ServerLink.ServerSupplements;
import pl.droidsonroids.gif.GifImageView;


@SuppressLint({"ValidFragment", "NewApi"})
public class DialogSelectSupplement extends BaseDialogFragment<DialogSelectSupplement.OnDialogFragmentClickListener> {

    public FragmentStore fragmentStore;
    private View iView;
    private TextView txtItemId;
    private GifImageView progressBar;
    private RecyclerView rcyViewSupplement;
    private SupplementViewAdapter supplementViewAdapter;
    private ArrayList<ClassSupplement> list;
    private LinearLayoutManager manager;
    private ServerSupplements serverSupplements;
    public String inputStr = "";

    public static DialogSelectSupplement newInstance(FragmentStore fragmentStore) {
        DialogSelectSupplement dialogSelectSupplement = new DialogSelectSupplement(fragmentStore);
        return dialogSelectSupplement;
    }

    public DialogSelectSupplement(FragmentStore fragmentStore) {
        this.fragmentStore = fragmentStore;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_select_supplement, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRetainInstance(true);

        progressBar = iView.findViewById(R.id.progressBar);
        rcyViewSupplement = iView.findViewById(R.id.rcyViewSupplement);
        txtItemId = iView.findViewById(R.id.txtItemId);

        list = new ArrayList<>();
        serverSupplements = new ServerSupplements();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        supplementViewAdapter = new SupplementViewAdapter(list, this);
        rcyViewSupplement.setAdapter(supplementViewAdapter);
        rcyViewSupplement.setLayoutManager(manager);

        txtItemId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputStr = txtItemId.getText().toString().trim().replace("'", "''");
                fetchData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return iView;
    }

    private void fetchData() {

        list.clear();
        supplementViewAdapter.notifyDataSetChanged();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                for (int i = 0; i < 10; i++) {
//                    ClassSupplement classSupplement = new ClassSupplement();
//                    classSupplement.setName("Protein Powder");
//                    classSupplement.setStock(Double.parseDouble("152"));
//                    classSupplement.setPrice(Double.parseDouble("5684.00"));
//                    list.add(classSupplement);
//                }
//
//                supplementViewAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
//        }, 2000);


                new PopulateList().execute();

    }

    private class PopulateList extends AsyncTask<Void, Void, Void> {

        private String result = "";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverSupplements.getAllRecords(inputStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverSupplements.getList().size(); i++) {
                    ClassSupplement classSupplement = new ClassSupplement(
                            serverSupplements.getList().get(i).getId(),
                            serverSupplements.getList().get(i).getName(),
                            serverSupplements.getList().get(i).getType(),
                            serverSupplements.getList().get(i).getStock(),
                            serverSupplements.getList().get(i).getPrice()
                    );

                    list.add(classSupplement);
                }


            } else if (result.equals("nodata")) {
                ShowDialog.showToast(getActivity(), "No Supplements or Products Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            supplementViewAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onCancelled() {
            cancel(true);
            progressBar.setVisibility(View.INVISIBLE);
            super.onCancelled();
        }

    }

    public interface OnDialogFragmentClickListener {

    }
}

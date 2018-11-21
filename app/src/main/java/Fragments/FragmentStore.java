package Fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abubakr.gymfitness.HomeActivity;
import com.example.abubakr.gymfitness.R;

import java.util.ArrayList;
import java.util.Random;

import Adapters.ItemCartAdapter;
import Dialogs.DialogSelectSupplement;
import GettersAndSetters.ClassItemCart;
import GettersAndSetters.ClassMessages;
import GettersAndSetters.ClassSupplement;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.StringChange;
import ServerLink.ServerBilling;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentStore extends Fragment {

    private View iView;
    private FragmentManager fragmentManager;
    public HomeActivity homeActivity;
    private RecyclerView recyclerViewCartItems;
    private ItemCartAdapter itemCartAdapter;
    private ArrayList<ClassItemCart> list;
    private LinearLayoutManager manager;
    private TextView lblItemName, lblItemSubTotal, lblTotal;
    private EditText txtQuantity;
    private Button btnAddToList, btnPlaceOrder, btnCancel;
    private String supplementName = "";
    private Double price = 0.0, stock = 0.0, total = 0.0;
    private Double quantity = 0.0;
    private int supId = 0;
    ServerBilling serverBilling;

    public FragmentStore(FragmentManager fragmentManager, HomeActivity homeActivity) {
        this.fragmentManager = fragmentManager;
        this.homeActivity = homeActivity;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerViewCartItems = iView.findViewById(R.id.recyclerViewCartItems);
        lblItemName = iView.findViewById(R.id.lblItemName);
        btnAddToList = iView.findViewById(R.id.btnAddToList);
        txtQuantity = iView.findViewById(R.id.txtQuantity);
        lblItemSubTotal = iView.findViewById(R.id.lblItemSubTotal);
        lblTotal = iView.findViewById(R.id.lblTotal);
        btnPlaceOrder = iView.findViewById(R.id.btnPlaceOrder);
        btnCancel = iView.findViewById(R.id.btnCancel);

        list = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemCartAdapter = new ItemCartAdapter(list, this);
        recyclerViewCartItems.setAdapter(itemCartAdapter);
        recyclerViewCartItems.setLayoutManager(manager);
        serverBilling = new ServerBilling();

        lblItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectSupplement dialogSelectSupplement = DialogSelectSupplement.newInstance(FragmentStore.this);
                dialogSelectSupplement.show(getFragmentManager(), "dialog");
            }
        });

        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateFields();
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {

                    new AlertDialog.Builder(getActivity()).setTitle("Confirm Placing Order")
                            .setMessage("Are you sure you want to place this over?")
                            .setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            new PlaceOrder().execute();
                                            dialog.dismiss();

                                        }
                                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            }).create().show();


                } else
                    ShowDialog.showToast(getActivity(), "Please add some items to the list");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Cancelling the Bill")
                        .setMessage("Are you sure you want to cancel this bill?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        CancelBill();
                                        dialog.dismiss();

                                    }
                                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });

        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txtQuantity.getText().toString().equals("0")) {
                    txtQuantity.setText("1");
                }

                try {
                    quantity = Double.valueOf(txtQuantity.getText().toString());
                } catch (NumberFormatException e) {
                    quantity = 0.0;
                }

                lblItemSubTotal.setText((quantity * price) + "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return iView;
    }

    private void ValidateFields() {

        if (supId == 0) {
            ShowDialog.showToastLong(getActivity(), "Please select a Supplement or Product to add to the cart");
        } else if (txtQuantity.getText().toString().equals("")) {
            ShowDialog.showToast(getActivity(), "Please Enter some quantity");
        } else {

            if (stock >= quantity) {
                addToCart();
            } else {
                ShowDialog.showToastLong(getActivity(), "Quantity should be less than or equal to stock");
            }
        }

    }

    private void addToCart() {
        boolean flagStockCheck = true, flagItemCheck = true;

        /******* checking if product already exists ******/

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getSupId() == supId) {
                flagItemCheck = false;
                Double tmpQty = quantity + list.get(i).getQuantity();
                Double tmpSbTtl = tmpQty * price;

                if (stock < tmpQty) {
                    flagStockCheck = false;
                    break;
                } else {
                    list.set(i, new ClassItemCart(
                            list.get(i).getSupId(),
                            tmpQty,
                            tmpSbTtl,
                            list.get(i).getItemName()
                    ));
                }


            }

        }

        /**************/


        if (flagStockCheck) {
            if (flagItemCheck) {
                ClassItemCart classItemCart = new ClassItemCart(supId, quantity, price * quantity, supplementName);
                list.add(classItemCart);

            }
            itemCartAdapter.notifyDataSetChanged();
            clearCart();
        } else {
            ShowDialog.showToastLong(getActivity(), "Quantity should be less than or equal to stock");
        }


        /******* getting the final total ****************/

        total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getSubTotal();
        }

        lblTotal.setText("Total = MYR. " + total);

        /**************/


    }

    private class PlaceOrder extends AsyncTask<Void, Void, Void> {

        private String result = "failed";

        @Override
        protected void onPreExecute() {
            homeActivity.showProgress();

            serverBilling.getClassBill().setCusId(SessionData.cusId);
            serverBilling.getClassBill().setTotal(total);
            serverBilling.setItemList(list);
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (serverBilling.Save() > 0) {
                result = "success";
                serverBilling.SaveOrder();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (result.equals("success")) {
                ShowDialog.showToast(getActivity(), "Your Order was Placed Successfully");
                CancelBill();
            } else
                ShowDialog.showToast(getActivity(), "Something went wrong");

            homeActivity.hideProgress();

        }

        @Override
        protected void onCancelled() {
            cancel(true);
            homeActivity.hideProgress();
            super.onCancelled();
        }

    }

    private void CancelBill() {
        clearCart();
        list.clear();
        itemCartAdapter.notifyDataSetChanged();
        lblTotal.setText("Total = MYR. 0.00");
        this.total = 0.0;
    }

    private void clearCart() {
        this.supId = 0;
        this.supplementName = "";
        this.price = 0.0;
        this.stock = 0.0;
        txtQuantity.setText("");
        txtQuantity.setHint("Quantity");
        lblItemName.setText("");
        lblItemName.setHint("Select a Supplement/Product");
        lblItemSubTotal.setText("0.00");
    }

    public void setSupplementData(ClassSupplement classSupplement) {
        this.supId = classSupplement.getId();
        this.supplementName = classSupplement.getName();
        this.price = classSupplement.getPrice();
        this.stock = classSupplement.getStock();
        lblItemName.setText(supplementName + "");
        txtQuantity.requestFocus();

    }
}











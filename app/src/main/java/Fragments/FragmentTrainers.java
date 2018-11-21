package Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.abubakr.gymfitness.R;

import java.util.ArrayList;

import Adapters.TrainerViewAdapter;
import GettersAndSetters.ClassTrainers;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.StringChange;
import ServerLink.ServerTrainers;

@SuppressLint({"NewApi", "ValidFragment"})
public class FragmentTrainers extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View iView;
    private FragmentHome fragmentHome;
    private RecyclerView rcyViewTrainer;
    private TrainerViewAdapter trainerViewAdapter;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassTrainers> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0;
    public ServerTrainers serverTrainers;
    public String inputStr = "";

    public FragmentTrainers(FragmentHome fragmentHome) {
        this.fragmentHome = fragmentHome;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        iView = inflater.inflate(R.layout.fragment_trainers, container, false);

        rcyViewTrainer = iView.findViewById(R.id.rcyViewTrainer);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        list = new ArrayList<>();
        serverTrainers = new ServerTrainers();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        trainerViewAdapter = new TrainerViewAdapter(list, this);
        rcyViewTrainer.setAdapter(trainerViewAdapter);
        rcyViewTrainer.setLayoutManager(manager);

        rcyViewTrainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;

                    fetchData();

                }

            }
        });

        swp2Rfsh.setOnRefreshListener(this);
        swp2Rfsh.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_orange_dark);
        serverTrainers.getClassTrainers().setSchdId(Integer.parseInt(SessionData.sheduleId));
        fetchData();

        return iView;

    }

    @Override
    public void onRefresh() {
        isScrolling = false;
        page = 0;
        list.clear();
        fetchData();
    }

    public void SearchData(){
        page = 0;
        list.clear();
        fetchData();
    }

    public void fetchData() {

        fragmentHome.homeActivity.showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PopulateList populateList = new PopulateList();
                populateList.execute();

            }
        }, 1000);
    }

    private class PopulateList extends AsyncTask<Void, Void, Void> {

        private String result = "";

        @Override
        protected void onPreExecute() {
            fragmentHome.homeActivity.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverTrainers.getAllRecords(inputStr, page);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverTrainers.getList().size(); i++) {
                    ClassTrainers classTrainers = new ClassTrainers(
                            serverTrainers.getList().get(i).getId(),
                            serverTrainers.getList().get(i).getName(),
                            serverTrainers.getList().get(i).getDob(),
                            serverTrainers.getList().get(i).getAddress(),
                            serverTrainers.getList().get(i).getGender(),
                            serverTrainers.getList().get(i).getPhone(),
                            serverTrainers.getList().get(i).getWorkExperience(),
                            serverTrainers.getList().get(i).getPicture(),
                            serverTrainers.getList().get(i).getStatus(),
                            serverTrainers.getList().get(i).getSchdId()

                    );

                    list.add(classTrainers);
                }


            } else if (result.equals("nodata")) {
                ShowDialog.showToast(getActivity(), "No more Trainers Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            trainerViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            fragmentHome.homeActivity.hideProgress();
            page += 1;

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentHome.homeActivity.hideProgress(); /*hide the progressbar dialog here...*/
            super.onCancelled();
        }

    }

}

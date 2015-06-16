package com.rideshare.rideshare.view.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.RankAdapter;
import com.rideshare.rideshare.entity.app.Rank;
import com.rideshare.rideshare.present.RankPresent;
import com.rideshare.rideshare.view.activity.NavigationActivity;

import java.util.ArrayList;

public class RankFragment extends ListFragment implements View.OnClickListener{

    private RankPresent present;
    private ArrayList<Rank> ranks;
    private RankAdapter adapter;

    public static RankFragment newInstance(Bundle args) {
        RankFragment fragment = new RankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ranks = new ArrayList<>();
        present = new RankPresent(this, ((NavigationActivity) getActivity()).getUserID(), ranks);
        adapter = new RankAdapter(getActivity(), R.layout.rank_item, ranks);
        ListView listView = getListView();
        listView.setDivider(null);
        setListAdapter(adapter);
        Bundle args = getArguments();
        if(args.getString("RIDE_ID", null) != null){
            present.getDriver(args.getString("RIDE_ID"));
        } else {
            present.getPassengers(args.getString("REQUEST_ID"));
        }
    }

    public void initList(ArrayList<Rank> rankUsers) {
        ranks.addAll(rankUsers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rank_btn){
            present.rankUsers();
        }
    }
}

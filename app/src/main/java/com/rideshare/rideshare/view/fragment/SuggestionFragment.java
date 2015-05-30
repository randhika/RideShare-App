package com.rideshare.rideshare.view.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.SuggestionAdapter;
import com.rideshare.rideshare.entity.app.Suggestion;
import com.rideshare.rideshare.present.SuggestionPresent;

import java.util.ArrayList;

public class SuggestionFragment extends ListFragment {

    private SuggestionPresent present;
    private ArrayList<Suggestion> suggestions;
    private SuggestionAdapter adapter;

    public static SuggestionFragment newInstance(Bundle args) {
        SuggestionFragment fragment = new SuggestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SuggestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String requestID = args.getString("REQUEST_ID");
        present = new SuggestionPresent(this, requestID);
        suggestions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setDivider(null);
        adapter = new SuggestionAdapter(getActivity(), R.layout.suggestion_item, suggestions,
                present);
        this.setListAdapter(adapter);
        present.getSuggestion();
    }

    public void addSuggestions(ArrayList<Suggestion> suggestions) {
        this.suggestions.addAll(suggestions);
        adapter.notifyDataSetChanged();
    }
}

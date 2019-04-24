package com.ssproduction.shashank.newproject.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssproduction.shashank.newproject.Adapters.ListAdapter;
import com.ssproduction.shashank.newproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFeedFrag extends Fragment {


    public PostsFeedFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts_feed, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profile_posts_on_tabs);
        ListAdapter listAdapter = new ListAdapter(getContext());
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

}

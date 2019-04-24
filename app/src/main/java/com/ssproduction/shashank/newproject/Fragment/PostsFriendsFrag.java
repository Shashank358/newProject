package com.ssproduction.shashank.newproject.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssproduction.shashank.newproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFriendsFrag extends Fragment {


    public PostsFriendsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts_friends, container, false);
    }

}

package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ssproduction.shashank.newproject.Fragment.PostsFeedFrag;
import com.ssproduction.shashank.newproject.Fragment.PostsFriendsFrag;
import com.ssproduction.shashank.newproject.Fragment.PostsGreedFrag;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public ProfilePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0){
            return new PostsFeedFrag();
        }else if (i == 1){
            return new PostsGreedFrag();
        }else{
            return new PostsFriendsFrag();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
            case 1:
            case 2:
                default:
                    return null;
        }
    }
}

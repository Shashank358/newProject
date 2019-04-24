package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ssproduction.shashank.newproject.Fragment.FollowersFrag;
import com.ssproduction.shashank.newproject.Fragment.FollowingFrag;

public class FriendsPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public FriendsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int i) {

        if (i == 0){
            return new FollowingFrag();
        }else {
            return new FollowersFrag();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
            case 1:
                default:
                    return null;
        }
    }
}

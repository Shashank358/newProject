package com.ssproduction.shashank.newproject.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.ssproduction.shashank.newproject.Fragment.Fragment1;
import com.ssproduction.shashank.newproject.Fragment.Fragment2;
import com.ssproduction.shashank.newproject.Fragment.Fragment3;
import com.ssproduction.shashank.newproject.Fragment.Fragment4;

public class pagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public pagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new Fragment4();
        }else if (position == 1){
            return new Fragment1();
        }else if (position == 2){
            return new Fragment2();
        }else {
            return new Fragment3();
        }
    }


    @Override
    public int getCount() {
        return 4;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0:
            case 1:
            case 2:
            case 3:
                default:
                    return null;
        }

    }
}

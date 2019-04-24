package com.ssproduction.shashank.newproject;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ssproduction.shashank.newproject.Adapters.FriendsPagerAdapter;

public class FriendsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager pager;
    private TabLayout tabLayout;

    private String name[] = {"Following", "Followers"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mToolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Following");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager = (ViewPager) findViewById(R.id.friends_pager);
        pager.setOffscreenPageLimit(2);
        FriendsPagerAdapter adapter = new FriendsPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.friends_tabs);
        tabLayout.setupWithViewPager(pager);
        tabName();
    }

    private void tabName() {
        tabLayout.getTabAt(0).setText(name[0]);
        tabLayout.getTabAt(1).setText(name[1]);

    }
}

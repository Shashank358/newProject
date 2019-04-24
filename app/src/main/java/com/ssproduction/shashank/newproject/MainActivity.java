package com.ssproduction.shashank.newproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssproduction.shashank.newproject.Fragment.Fragment1;
import com.ssproduction.shashank.newproject.Fragment.Fragment2;
import com.ssproduction.shashank.newproject.Fragment.Fragment3;
import com.ssproduction.shashank.newproject.Fragment.Fragment4;
import com.ssproduction.shashank.newproject.utils.pagerAdapter;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private int[] tabIcons = {R.drawable.ic_event_note_black_24dp, R.drawable.ic_chat_black_24dp
            , R.drawable.ic_people_black_24dp, R.drawable.ic_person_black_24dp};

    private TabLayout tabLayout;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String mCurrentUser;
    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();

        navigationView = (BottomNavigationView) findViewById(R.id.main_navigation);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_container, new Fragment4()).commit();


        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("FunCHat");

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.main_timeline_item:
                        transaction.replace(R.id.main_container, new Fragment4()).commit();
                        return true;

                    case R.id.main_chat_item:
                        transaction.replace(R.id.main_container, new Fragment1()).commit();
                        return true;

                    case R.id.main_all_users_item:
                        transaction.replace(R.id.main_container, new Fragment2()).commit();
                        return true;

                    case R.id.main_profile_item:
                        transaction.replace(R.id.main_container, new Fragment3()).commit();
                        return true;
                }

                return true;

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.app_bar_search){

            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(searchIntent);

        }

      if (item.getItemId() == R.id.logout_item){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null){
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            }

      }
      if (item.getItemId() == R.id.about_item){


      }
      if (item.getItemId() == R.id.account_settings_item){
          Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
          accountIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(accountIntent);

      }
      if (item.getItemId() == R.id.chat_talk_swap_item){

          getSupportActionBar().setTitle("ChatTalk");
          switch (item.getItemId()){
              case R.id.chat_talk_swap_item:
                  if (item.getTitle().equals("Swap to ChatTalk")){
                      item.setTitle("Swap to FunCHat");
                  }
                  else {
                      item.setTitle("Swap to ChatTalk");
                      getSupportActionBar().setTitle("FunCHat");
                  }
          }
      }
      return true;
    }

    private void online(String online){

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", online);


        mUserDatabase.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        online("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        online("offline");
    }
}

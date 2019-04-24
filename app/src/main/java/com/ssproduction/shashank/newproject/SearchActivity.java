package com.ssproduction.shashank.newproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssproduction.shashank.newproject.Adapters.UsersAdapterFrag2;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView AlluserList;
    private String mCurrentUser;

    private DatabaseReference mUserDatabase;


    private UsersAdapterFrag2 usersAdapter;
    private List<Users> mUsers;

    private EditText search_user;
    private android.support.v7.widget.Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser().getUid();

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlluserList = (RecyclerView) findViewById(R.id.all_user_list_activity);
        AlluserList.setHasFixedSize(true);
        AlluserList.setLayoutManager(new LinearLayoutManager(this));

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);

        search_user = (EditText)findViewById(R.id.search_user_activity);
        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });

        mUsers = new ArrayList<>();

        readUsers();

    }

    public void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users users = snapshot.getValue(Users.class);

                    assert users != null;
                    assert fuser != null;
                    if (!users.getId().equals(fuser.getUid())) {
                        mUsers.add(users);
                    }
                }

                usersAdapter= new UsersAdapterFrag2(getApplicationContext(), mUsers, true);
                AlluserList.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void readUsers() {

        final FirebaseUser firebaseUser = mAuth.getCurrentUser();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (search_user.getText().toString().equals("")) {
                    mUsers.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users users = snapshot.getValue(Users.class);

                        assert users != null;
                        assert firebaseUser != null;

                        if (!users.getId().equals(mCurrentUser)) {
                            mUsers.add(users);

                        }
                    }
                }

                usersAdapter = new UsersAdapterFrag2(getApplicationContext(), mUsers, true);
                AlluserList.setAdapter(usersAdapter);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

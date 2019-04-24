package com.ssproduction.shashank.newproject.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.Adapters.UsersAdapterFrag2;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.UserProfileActivity;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView AlluserList;
    private String mCurrentUser, mCurrent_state;

    private DatabaseReference mUserDatabase;


    private UsersAdapterFrag2 usersAdapter;
    private List<Users> mUsers;


    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

       mAuth = FirebaseAuth.getInstance();

       mCurrentUser = mAuth.getCurrentUser().getUid();

       AlluserList = (RecyclerView) view.findViewById(R.id.all_user_list);
       AlluserList.setHasFixedSize(true);
       AlluserList.setLayoutManager(new LinearLayoutManager(getActivity()));

       mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
       mUserDatabase.keepSynced(true);



        mUsers = new ArrayList<>();
        readUsers();


       return view;
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

                    usersAdapter= new UsersAdapterFrag2(getContext(), mUsers, false);
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

                    mUsers.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users users = snapshot.getValue(Users.class);

                        assert users != null;
                        assert firebaseUser != null;

                        if (!users.getId().equals(mCurrentUser)) {
                            mUsers.add(users);

                        }
                    }
                usersAdapter = new UsersAdapterFrag2(getContext(), mUsers, false);
                usersAdapter.notifyDataSetChanged();
                AlluserList.setAdapter(usersAdapter);


                usersAdapter.setOnItemClickListener(new UsersAdapterFrag2.OnItemClickListener() {
                    @Override
                    public void onItemCick(int position) {

                        Users users = mUsers.get(position);
                        Intent chatIntent = new Intent(getActivity(), UserProfileActivity.class);
                        chatIntent.putExtra("user_id", users.getId());
                        getActivity().startActivity(chatIntent);

                    }

                    @Override
                    public void onFollowClick(int position) {

                        Users users = mUsers.get(position);

                        String user_id = users.getId();
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

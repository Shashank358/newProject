package com.ssproduction.shashank.newproject.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssproduction.shashank.newproject.Adapters.FriendsAdapter;
import com.ssproduction.shashank.newproject.Adapters.UsersAdapter;
import com.ssproduction.shashank.newproject.Adapters.UsersAdapterFrag2;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Follow;
import com.ssproduction.shashank.newproject.utils.Friends;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFrag extends Fragment {

    private RecyclerView followerList;

    private DatabaseReference mFollowDatabase;
    private FirebaseAuth mAuth;
    private String mCurrentUser;

    private List<Users> mUsers;
    private UsersAdapterFrag2 usersAdapter;

    private List<Follow> mFollow;


    public FollowingFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();

        followerList = (RecyclerView) view.findViewById(R.id.friends_list);
        followerList.setHasFixedSize(true);
        followerList.setLayoutManager(new LinearLayoutManager(getActivity()));


        mFollow = new ArrayList<>();

        mFollowDatabase = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(mCurrentUser);

        mFollowDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mFollow.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Follow follow = snapshot.getValue(Follow.class);
                    mFollow.add(follow);
                }
                followerPeople();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    private void followerPeople() {

        mUsers = new ArrayList<>();

        mFollowDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mFollowDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    for (Follow follow : mFollow){
                        if (users.getId().equals(follow.getFollowingId())){
                            mUsers.add(users);
                        }

                        usersAdapter = new UsersAdapterFrag2(getContext(), mUsers, true);
                        followerList.setAdapter(usersAdapter);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

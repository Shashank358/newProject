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
import com.google.firebase.iid.FirebaseInstanceId;
import com.ssproduction.shashank.newproject.Adapters.UsersAdapter;
import com.ssproduction.shashank.newproject.Notifications.Token;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Chatlist;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    private RecyclerView chatList;
    private DatabaseReference mChatDatabase,friendsDatabase;

    private List<Users> mUsers;
    private UsersAdapter usersAdapter;

    private List<Chatlist> usersList;

    private FirebaseUser fuser;


    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        chatList = (RecyclerView) view.findViewById(R.id.list_users);
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        mChatDatabase = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        mChatDatabase.keepSynced(true);
        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Chats").child(fuser.getUid());
        friendsDatabase.keepSynced(true);

        mChatDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                ChatList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;

    }

    private void updateToken(String token) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }


    private void ChatList() {
        mUsers = new ArrayList<>();

        mChatDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mChatDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    for (Chatlist chatList : usersList){
                        if (users.getId().equals(chatList.getId())){
                            mUsers.add(users);
                        }
                    }
                }

                usersAdapter = new UsersAdapter(getContext(), mUsers, true);
                chatList.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

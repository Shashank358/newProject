package com.ssproduction.shashank.newproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.utils.Follow;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private String user_id;
    private Toolbar mToolbar;

    private CircleImageView otherUserImage;
    private TextView userNameFirst, userNameLast, userStatus;
    private TextView friends, messege, followView, friendsNumbers;

    private String current_state, mCurrent_state;

    private String mCurrentUser;

    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef;
    private DatabaseReference mUserDatabase, FollowersRef, FollowCountRef;
    private DatabaseReference myDatabase, otherDatabase;
    private DatabaseReference mainDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        user_id = getIntent().getStringExtra("user_id");

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.keepSynced(true);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user_id);
        mUserDatabase.keepSynced(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myDatabase =  FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser);
        otherDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user_id);

        FollowersRef = FirebaseDatabase.getInstance().getReference().child("Follow");
        FollowCountRef = FirebaseDatabase.getInstance().getReference().child("FollowCount");

        mToolbar = (Toolbar) findViewById(R.id.profile_toolbar_main);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        otherUserImage = (CircleImageView) findViewById(R.id.avatar_circle_image);
        userNameFirst = (TextView) findViewById(R.id.user_name);
        userStatus = (TextView) findViewById(R.id.user_college_name);
        friendsNumbers = (TextView) findViewById(R.id.friends_count);
        friends = (TextView) findViewById(R.id.profile_followers_text);
        followView = (TextView) findViewById(R.id.profile_follow_btn);
        messege = (TextView) findViewById(R.id.profile_message_text);

        messege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(UserProfileActivity.this, ChatActivity.class);
                chatIntent.putExtra("user_id", user_id);
                startActivity(chatIntent);
            }
        });
        mCurrent_state = "unFollowing";

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String user_first_name = dataSnapshot.child("search").getValue().toString();
                String user_status = dataSnapshot.child("status").getValue().toString();
                final String user_image = dataSnapshot.child("avatar_thumbImage").getValue().toString();

                userNameFirst.setText(user_first_name);
                userStatus.setText(user_status);

                Picasso.get().load(user_image).placeholder(R.drawable.user_profile).into(otherUserImage);

                //----------------Follow/Following Features---------------//
                FollowersRef.child(mCurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)){
                            String followerId = dataSnapshot.child(user_id).child("followerId").getValue().toString();
                            String followingId = dataSnapshot.child(user_id).child("followingId").getValue().toString();

                            if (followingId.equals(user_id) && followerId.equals("default")) {
                                mCurrent_state = "Followed_User";
                                followView.setText("Unfollow");
                                followView.setBackground(getResources().getDrawable(R.drawable.edit_profile_background));
                                followView.setTextColor(Color.BLACK);

                            }else if (followerId.equals(user_id) && followingId.equals("default")){
                                mCurrent_state = "Get_Followed";
                                followView.setText("Follow back");
                                followView.setTextColor(Color.WHITE);
                                followView.setBackground(getResources().getDrawable(R.drawable.user_single_follow_background));

                            }else if (followingId.equals(user_id) && followerId.equals(user_id)){
                                mCurrent_state = "both_following";
                                followView.setText("Unfollow");
                                followView.setBackground(getResources().getDrawable(R.drawable.edit_profile_background));
                                followView.setTextColor(Color.BLACK);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        followView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-----------not following state-------//
                if (mCurrent_state.equals("unFollowing")) {

                    followView.setText("Unfollow");
                    followView.setBackground(getResources().getDrawable(R.drawable.edit_profile_background));
                    followView.setTextColor(Color.BLACK);

                    Map getMap = new HashMap();
                    getMap.put("followingId", user_id);
                    getMap.put("followerId", "default");

                    Map myMap = new HashMap();
                    myMap.put("followerId", mCurrentUser);
                    myMap.put("followingId", "default");


                    Map newMap = new HashMap();
                    newMap.put(mCurrentUser + "/" + user_id + "/", getMap);
                    newMap.put(user_id + "/" + mCurrentUser + "/", myMap);

                    Map countMap = new HashMap();
                    countMap.put(user_id + "/" + "followers" + "/" + mCurrentUser, "default");
                    countMap.put(mCurrentUser + "/" + "following" + "/" + user_id, "default");

                    FollowCountRef.updateChildren(countMap);
                    FollowersRef.updateChildren(newMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Toast.makeText(UserProfileActivity.this, "Error..check your internet", Toast.LENGTH_SHORT).show();
                            }

                            mCurrent_state = "Followed_User";
                            followView.setText("Unfollow");
                            followView.setBackground(getResources().getDrawable(R.drawable.edit_profile_background));
                            followView.setTextColor(Color.BLACK);
                        }
                    });


                }

                if (mCurrent_state.equals("Followed_User")) {

                    followView.setText("Follow");
                    followView.setBackground(getResources().getDrawable(R.drawable.user_single_follow_background));
                    followView.setTextColor(Color.WHITE);

                    Map followedMap = new HashMap();
                    followedMap.put(mCurrentUser + "/" + user_id, null);
                    followedMap.put(user_id + "/" + mCurrentUser, null);

                    Map countMap = new HashMap();
                    countMap.put(user_id + "/" + "followers" + "/" + mCurrentUser, null);
                    countMap.put(mCurrentUser + "/" + "following" + "/" + user_id, null);


                    FollowCountRef.updateChildren(countMap);

                    FollowersRef.updateChildren(followedMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            mCurrent_state = "unFollowing";
                            followView.setText("Follow");
                            followView.setBackground(getResources().getDrawable(R.drawable.user_single_follow_background));
                            followView.setTextColor(Color.WHITE);

                        }
                    });

                }
                if (mCurrent_state.equals("Get_Followed")){
                    followView.setText("Unfollow");
                    followView.setBackground(getResources().getDrawable(R.drawable.edit_profile_background));
                    followView.setTextColor(Color.BLACK);

                    Map getMap = new HashMap();
                    getMap.put("followingId", mCurrentUser);
                    getMap.put("followerId", mCurrentUser);

                    Map myMap = new HashMap();
                    myMap.put("followerId", user_id);
                    myMap.put("followingId", user_id);

                    Map followBackMap = new HashMap();
                    followBackMap.put( user_id + "/" + mCurrentUser + "/", getMap);
                    followBackMap.put(mCurrentUser + "/" + user_id + "/", myMap);

                    Map countMap = new HashMap();
                    countMap.put(user_id + "/" + "followers" + "/" + mCurrentUser, "default");
                    countMap.put(mCurrentUser + "/" + "following" + "/" + user_id, "default");

                    FollowCountRef.updateChildren(countMap);

                    FollowersRef.updateChildren(followBackMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            mCurrent_state = "both_following";
                            followView.setText("Unfollow");
                            followView.setBackground(getResources().getDrawable(R.drawable.edit_profile_background));
                            followView.setTextColor(Color.BLACK);
                        }
                    });

                }if (mCurrent_state.equals("both_following")){
                    followView.setText("Follow");
                    followView.setBackground(getResources().getDrawable(R.drawable.user_single_follow_background));
                    followView.setTextColor(Color.WHITE);

                    Map getMap = new HashMap();
                    getMap.put("followingId", mCurrentUser);
                    getMap.put("followerId", "default");

                    Map followBackMap = new HashMap();
                    followBackMap.put( user_id + "/" + mCurrentUser + "/", getMap);
                    followBackMap.put(mCurrentUser + "/" + user_id + "/", null);

                    Map countMap = new HashMap();
                    countMap.put(user_id + "/" + "followers" + "/" + mCurrentUser, null);
                    countMap.put(mCurrentUser + "/" + "following" + "/" + user_id, null);

                    FollowCountRef.updateChildren(countMap);

                    FollowersRef.updateChildren(followBackMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            mCurrent_state = "unFollowing";
                            followView.setText("Follow");
                            followView.setBackground(getResources().getDrawable(R.drawable.user_single_follow_background));
                            followView.setTextColor(Color.WHITE);

                        }
                    });

                }

            }
        });

    }

    private void online(String online){

        current_state = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(current_state);

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

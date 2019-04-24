package com.ssproduction.shashank.newproject.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.AccountActivity;
import com.ssproduction.shashank.newproject.Adapters.ProfilePagerAdapter;
import com.ssproduction.shashank.newproject.FriendsActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Follow;
import com.ssproduction.shashank.newproject.utils.Users;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment {


    private CircleImageView userImage;
    private TextView userName, collegeName, profileFollowing, profilePosts, postsCount, followingCount, profileFollowers, followersCount;
    private ImageView profileImageCamera, profileCoverCamera, userCoverImage;
    private static final int GALLERY_PIC = 1;

    private ProgressDialog dialog;

    private String mCurrentUser;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase, FollowDatabase, postDatabase, followCountRef;

    private LinearLayout friendsLinear;
    private LinearLayout editProfileLinear;
    private LinearLayout privacyLinear;
    private LinearLayout moreLinear;

    private TabLayout tabs;
    private ViewPager viewPager;
    private int[] tabIcons = { R.drawable.icons8_activity_feed
    ,R.drawable.icons8_posts ,R.drawable.icons8_friends};


    public Fragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        dialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser);

        userImage = (CircleImageView) view.findViewById(R.id.avatar_circle_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        collegeName = (TextView) view.findViewById(R.id.user_college_name);
        profileImageCamera = (ImageView) view.findViewById(R.id.profile_camera_image);
        userCoverImage = (ImageView) view.findViewById(R.id.user_cover_image);
        friendsLinear = (LinearLayout) view.findViewById(R.id.friends_linear);
        moreLinear = (LinearLayout) view.findViewById(R.id.more_linear);
        editProfileLinear = (LinearLayout) view.findViewById(R.id.edit_profile_linear);
        profilePosts = (TextView) view.findViewById(R.id.profile_posts_text);
        profileFollowers = (TextView) view.findViewById(R.id.profile_followers_text);
        postsCount = (TextView) view.findViewById(R.id.posts_count);
        followersCount = (TextView) view.findViewById(R.id.followers_count);
        followingCount = (TextView) view.findViewById(R.id.following_count);
        profileFollowing = (TextView) view.findViewById(R.id.profile_following_text);


        postDatabase = FirebaseDatabase.getInstance().getReference().child("PostCount").child(mCurrentUser);

        FollowDatabase = FirebaseDatabase.getInstance().getReference().child("Follow");

        followCountRef = FirebaseDatabase.getInstance().getReference().child("FollowCount");


        postDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    int postNo = (int) dataSnapshot.getChildrenCount();
                    String count = String.valueOf(postNo);
                    postsCount.setText(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        followCountRef.child(mCurrentUser).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int followingNo = (int) dataSnapshot.getChildrenCount();
                String count = String.valueOf(followingNo);
                followingCount.setText(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followCountRef.child(mCurrentUser).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int followersNo = (int) dataSnapshot.getChildrenCount();
                String count = String.valueOf(followersNo);
                followersCount.setText(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        editProfileLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(editIntent);
            }
        });

        friendsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendsIntent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(friendsIntent);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetForProfileImage bottomSheetDialogFragment = new BottomSheetForProfileImage();
                bottomSheetDialogFragment.show(getFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        userCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetForCoverImage bottomSheetForCoverImage = new BottomSheetForCoverImage();
                bottomSheetForCoverImage.show(getFragmentManager(), bottomSheetForCoverImage.getTag());


            }
        });



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("avatar_thumbImage")) {
                    String user_image = dataSnapshot.child("avatar_thumbImage").getValue().toString();
                    String user_name = dataSnapshot.child("search").getValue().toString();
                    String userCollege =  dataSnapshot.child("status").getValue().toString();

                    Picasso.get().load(user_image).placeholder(R.drawable.avatar).into(userImage);
                    collegeName.setText(userCollege);
                    userName.setText(user_name);

                    if (dataSnapshot.hasChild("avatar_cover_image")) {
                        String user_cover_image = dataSnapshot.child("avatar_cover_image").getValue().toString();
                        Picasso.get().load(user_cover_image)
                                .placeholder(R.drawable.bottomsheet_back).into(userCoverImage);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }



}

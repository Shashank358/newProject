package com.ssproduction.shashank.newproject.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.Adapters.ListAdapter;
import com.ssproduction.shashank.newproject.Adapters.TimelineImageAdapter;
import com.ssproduction.shashank.newproject.Adapters.UsersAdapter;
import com.ssproduction.shashank.newproject.Adapters.newAdapter;
import com.ssproduction.shashank.newproject.PrevePostActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Follow;
import com.ssproduction.shashank.newproject.utils.Posts;
import com.ssproduction.shashank.newproject.utils.Users;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment4 extends Fragment {


    private RecyclerView Postslist, statusPostList;
    private RecyclerView.LayoutManager layoutManager;
    private static ListAdapter adapter;
    private static newAdapter newadapter;

    private List<Posts> mPosts;
    private TimelineImageAdapter imageAdapter;
    private LinearLayoutManager manager;

    private List<Follow> mFollow;

    private ImageView addPost, sendPost;
    private CircleImageView userImage;
    private EditText addEditPost;
    private RelativeLayout postLoad, addPostRel;

    private static final int PICK = 1;
    private static final int VIDEO_PICK = 2;
    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private String mCurrentUser, pushId;
    private StorageReference mImageStorage;
    private DatabaseReference reference, FollowDatabase;

    private FloatingActionButton floatingActionButton;

    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private int currentPage = 0;



    public Fragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        dialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();

        Postslist = (RecyclerView) view.findViewById(R.id.user_posts_list);
        Postslist.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
        Postslist.setItemViewCacheSize(20);
        Postslist.setDrawingCacheEnabled(true);
        Postslist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Postslist.setLayoutManager(manager);

        //--------------------on scroll pagination feature--------------//

        addPost = (ImageView) view.findViewById(R.id.add_post_image);
        addPostRel = (RelativeLayout) view.findViewById(R.id.add_post_rel);

        statusPostList = (RecyclerView) view.findViewById(R.id.user_status_post_list);
        newadapter = new newAdapter(getContext());
        statusPostList.setAdapter(newadapter);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        statusPostList.setHasFixedSize(true);
        statusPostList.setLayoutManager(layoutManager);

        mFollow = new ArrayList<>();

        FollowDatabase = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(mCurrentUser);
        FollowDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mFollow.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Follow follow = snapshot.getValue(Follow.class);
                    mFollow.add(follow);
                }
                readPosts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), PICK);

            }
        });

        addPostRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preIntent = new Intent(getActivity(), PrevePostActivity.class);
                startActivity(preIntent);
            }
        });



        return view;
    }


    private void readPosts() {
        mPosts = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mPosts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Posts posts = snapshot.getValue(Posts.class);
                    if (mCurrentUser.equals(posts.getId())){
                        mPosts.add(posts);
                    }
                    for (Follow follow : mFollow){
                        if (follow.followingId.equals(posts.getId())){
                            mPosts.add(posts);
                        }
                    }
                    imageAdapter = new TimelineImageAdapter(getContext(), mPosts);
                    imageAdapter.notifyDataSetChanged();
                    Postslist.setAdapter(imageAdapter);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*

        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(
                        Posts.class,
                        R.layout.timeline_single_post_layout,
                        PostsViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(PostsViewHolder viewHolder, Posts model, int position) {

                        viewHolder.setUserThumbPost(model.getUserThumbPost());
                        viewHolder.setUser_image(model.getUser_image());
                        viewHolder.setPosting_time(model.getPosting_time());
                        viewHolder.setUser_name(model.getUser_name());

                    }
                };
        firebaseRecyclerAdapter.notifyItemChanged(0);

        Postslist.setAdapter(firebaseRecyclerAdapter);

        */

    }


/*
    public static class PostsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setPosting_time(String posting_time){

            TextView post_time = (TextView) itemView.findViewById(R.id.feed_user_timeAgo);
            post_time.setText(posting_time);
        }
        public void setUserThumbPost(String userThumbPost){
            ImageView post_image = (ImageView) itemView.findViewById(R.id.feed_user_post);
            Picasso picasso = Picasso.get();
            Picasso.get().load(userThumbPost).placeholder(R.drawable.bottomsheet_back).into(post_image);
            picasso.setIndicatorsEnabled(false);
        }
        public void setUser_name(String user_name){
            TextView userName = (TextView) itemView.findViewById(R.id.feed_user_name);
            userName.setText(user_name);
        }
        public void setUser_image(String user_image){
            CircleImageView userImage = (CircleImageView) itemView.findViewById(R.id.feed_user_image);
            Picasso picasso = Picasso.get();
            Picasso.get().load(user_image).into(userImage);
            picasso.setIndicatorsEnabled(false);
        }
    }
    */



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK && resultCode == Activity.RESULT_OK){

            String post_image = data.getData().toString();
            String currentUser = mCurrentUser;
            int result = resultCode;
            Intent mData = data;

            Intent preIntent = new Intent(getActivity(), PrevePostActivity.class);
            preIntent.putExtra("post_image", post_image);
            preIntent.putExtra("resultCode", result);
            preIntent.putExtra("data", mData);
            startActivity(preIntent);
        }
    }

}

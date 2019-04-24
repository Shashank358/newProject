package com.ssproduction.shashank.newproject;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.Adapters.FeedCommentsAdapter;
import com.ssproduction.shashank.newproject.utils.Comments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private TextView userName, timeAgo, userTextPost, seeMore;
    private CircleImageView userImage;
    private ImageView postImage, LikePost, UnlikePost, downloadPost, sharePost;
    private RecyclerView commentsList;
    private RelativeLayout postTextRel;

    private EditText mycomment;
    private ImageView sendComment;

    private Toolbar mToolbar;

    DatabaseReference postDatabase, mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;

    private List<Comments> mComments;
    private FeedCommentsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        String publisher_image = getIntent().getStringExtra("image");
        String publisher_name = getIntent().getStringExtra("name");
        String post_time = getIntent().getStringExtra("timeAgo");
        String post_view = getIntent().getStringExtra("postImage");
        String post_info = getIntent().getStringExtra("postInfo");
        String post_id = getIntent().getStringExtra("post_id");

        userName = (TextView) findViewById(R.id.comment_publisher_name);
        timeAgo = (TextView) findViewById(R.id.comment_publisher_time_ago);
        userTextPost = (TextView) findViewById(R.id.comment_publisher_image_info);
        seeMore = (TextView) findViewById(R.id.comment_see_more_text);
        userImage = (CircleImageView) findViewById(R.id.comment_publisher_image);
        postImage = (ImageView) findViewById(R.id.comment_publisher_post);
        LikePost = (ImageView) findViewById(R.id.comment_feed_like_color_view);
        UnlikePost = (ImageView) findViewById(R.id.comment_feed_like_view);
        downloadPost = (ImageView) findViewById(R.id.comment_post_image_download_view);
        sharePost = (ImageView) findViewById(R.id.comment_feed_share_view);
        postTextRel = (RelativeLayout) findViewById(R.id.comment_type_something_rel);
        commentsList = (RecyclerView) findViewById(R.id.comment_post_comment_list);
        mycomment = (EditText) findViewById(R.id.comment_user_comment_editext);
        sendComment = (ImageView) findViewById(R.id.comment_comment_send_view);

        mToolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentsList.setHasFixedSize(true);
        commentsList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        postDatabase = FirebaseDatabase.getInstance().getReference().child("postLikes");

        mComments = new ArrayList<>();

        Picasso picasso = Picasso.get();
        picasso.load(publisher_image).into(userImage);
        picasso.load(post_view).into(postImage);
        picasso.setIndicatorsEnabled(false);

        userName.setText(publisher_name);
        timeAgo.setText(post_time);
        userTextPost.setText(post_info);

        if (userTextPost.getText().equals("")){
            postTextRel.setVisibility(View.GONE);
        }

        //---------------comments list---------------//

        postDatabase.child(post_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mComments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comments comments = snapshot.getValue(Comments.class);
                    mComments.add(comments);
                }

                adapter = new FeedCommentsAdapter(getApplicationContext(), mComments);
                commentsList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //-------------comment send feature----------------//

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mComment = mycomment.getText().toString();
                mycomment.setText("");

                if (!mComment.equals("")){

                    postDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(currentUser);


                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("search").getValue().toString();
                                    String image = dataSnapshot.child("avatar_thumbImage").getValue().toString();

                                    Map commentMap = new HashMap();
                                    commentMap.put("mId", name);
                                    commentMap.put("postId", post_id);
                                    commentMap.put("profile_image", image);
                                    commentMap.put("comment", mComment);

                                    postDatabase.child(post_id).child("Comments").push().updateChildren(commentMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        }
                                    });
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

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }
}

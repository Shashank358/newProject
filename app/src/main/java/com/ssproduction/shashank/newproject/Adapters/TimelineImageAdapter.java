package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.CommentActivity;
import com.ssproduction.shashank.newproject.PostPreviewActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Comments;
import com.ssproduction.shashank.newproject.utils.Posts;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimelineImageAdapter extends RecyclerView.Adapter<TimelineImageAdapter.ViewHolder> {

    private Context mContext;
    private List<Posts> mPosts;
    private String currentUser;

    private List<Comments> mComments;
    private FeedCommentsAdapter adapter;

    DatabaseReference postDatabase, mDatabase;

    public TimelineImageAdapter(Context mContext, List<Posts> mPosts) {

        this.mContext = mContext;
        this.mPosts= mPosts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.timeline_single_post_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
    }



    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        postDatabase = FirebaseDatabase.getInstance().getReference().child("postLikes");

        final Posts posts = mPosts.get(position);

        holder.userName.setText(posts.getUser_name());
        holder.timeAgo.setText(posts.getPosting_time());
        holder.userTextPost.setText(posts.getUser_posted_text());
        if (currentUser.equals(posts.getId())) {
            if (posts.getLineCount() > 8){
                holder.seeMore.setVisibility(View.VISIBLE);

            }
        }

        if (holder.userTextPost.getText().equals("")){
            holder.postTextRel.setVisibility(View.GONE);
        }
        Picasso picasso = Picasso.get();

        if (posts.getPost_type().equals("photo")) {

            holder.postCard.setVisibility(View.VISIBLE);
            holder.postVideo.setVisibility(View.GONE);

            if (posts.getUserThumbPost().equals("default")) {
                holder.bar.setVisibility(View.VISIBLE);
                holder.postImage.setImageResource(R.drawable.bottomsheet_back);
                picasso.setIndicatorsEnabled(false);
            } else {
                picasso.setIndicatorsEnabled(false);

                Picasso.get().load(posts.getUserThumbPost()).fit().centerInside().networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.bottomsheet_back).into(holder.postImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                        Picasso.get().load(posts.getUserThumbPost()).fit().centerInside().placeholder(R.drawable.bottomsheet_back).into(holder.postImage);
                        picasso.setIndicatorsEnabled(false);
                    }
                });
            }
        }

        if (posts.getUser_image().equals("default")){
            holder.userImage.setImageResource(R.drawable.avatar);
            picasso.setIndicatorsEnabled(false);
        }
        else {
            picasso.setIndicatorsEnabled(false);
            Picasso.get().load(posts.getUser_image()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avatar).into(holder.userImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(posts.getUser_image()).placeholder(R.drawable.avatar).into(holder.userImage);
                    picasso.setIndicatorsEnabled(false);
                }
            });

            postDatabase.child(posts.getPushId()).child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(currentUser)){
                        holder.LikePost.setVisibility(View.VISIBLE);
                        holder.UnlikePost.setVisibility(View.GONE);
                    }else {
                        holder.LikePost.setVisibility(View.GONE);
                        holder.UnlikePost.setVisibility(View.VISIBLE);
                    }

                        int likeCount = (int) dataSnapshot.getChildrenCount();
                        String count = String.valueOf(likeCount);
                        holder.likesCount.setText(count + " Likes");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            postDatabase.child(posts.getPushId()).child("Comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int commentCount = (int) dataSnapshot.getChildrenCount();
                    String count = String.valueOf(commentCount);
                    holder.commentCount.setText(count + " Comments");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        //---------------Like/Unlike features----------------------//

        holder.UnlikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation expandIn = AnimationUtils.loadAnimation(mContext, R.anim.expand_in);
                holder.UnlikePost.setVisibility(View.GONE);
                holder.LikePost.setVisibility(View.VISIBLE);
                holder.LikePost.startAnimation(expandIn);


                postDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map newMap = new HashMap();
                        newMap.put(currentUser, "default");

                        postDatabase.child(posts.getPushId()).child("Likes").updateChildren(newMap);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //---------------like/unlike feature----------------//

        holder.LikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation expandIn = AnimationUtils.loadAnimation(mContext, R.anim.expand_in);
                holder.LikePost.setVisibility(View.GONE);
                holder.UnlikePost.setVisibility(View.VISIBLE);
                holder.UnlikePost.startAnimation(expandIn);


                postDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map newMap = new HashMap();
                        newMap.put(currentUser, null);

                        postDatabase.child(posts.getPushId()).child("Likes").updateChildren(newMap);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //------------------------comment feature-----------------------//
        holder.commentBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.commentBox.getText().toString().equals("")){
                    holder.sendComment.setVisibility(View.GONE);
                }else {
                    holder.sendComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = holder.commentBox.getText().toString();
                holder.commentBox.setText("");
                if (!comment.equals("")){
                    postDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(currentUser);

                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("search").getValue().toString();

                                    Map commentMap = new HashMap();
                                    commentMap.put("mId", name);
                                    commentMap.put("postId", posts.getPushId());
                                    commentMap.put("comment", comment);

                                    postDatabase.child(posts.getPushId()).child("Comments").push().updateChildren(commentMap, new DatabaseReference.CompletionListener() {
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
                }else {
                    Toast.makeText(mContext, "comment something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image = posts.getUserPost();
                String post_view = posts.getUserThumbPost();

                Intent previewIntent = new Intent(mContext, PostPreviewActivity.class);
                previewIntent.putExtra("postImage", image);
                previewIntent.putExtra("image", post_view);
                mContext.startActivity(previewIntent);
            }
        });

        holder.commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image = posts.getUser_image();
                String post_image = posts.getUserThumbPost();
                String post_id = posts.getPushId();
                String name = holder.userName.getText().toString();
                String timeAgo = holder.timeAgo.getText().toString();
                String postInfo = holder.userTextPost.getText().toString();

                Intent commentIntent = new Intent(mContext, CommentActivity.class);
                commentIntent.putExtra("image", image);
                commentIntent.putExtra("name", name);
                commentIntent.putExtra("timeAgo", timeAgo);
                commentIntent.putExtra("post_id", post_id);
                commentIntent.putExtra("postImage", post_image);
                commentIntent.putExtra("postInfo", postInfo);
                mContext.startActivity(commentIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userName, timeAgo, userTextPost, seeMore, likesCount, commentCount;
        private CircleImageView userImage;
        private ImageView postImage, LikePost, UnlikePost, sendComment, commentView;
        private RelativeLayout postTextRel;
        private EditText commentBox;
        private ProgressBar bar;
        private VideoView postVideo;
        private CardView postCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.feed_user_name);
            timeAgo = (TextView) itemView.findViewById(R.id.feed_user_timeAgo);
            userTextPost = (TextView) itemView.findViewById(R.id.feed_user_texted_post);
            userImage = (CircleImageView) itemView.findViewById(R.id.feed_user_image);
            postImage = (ImageView) itemView.findViewById(R.id.feed_user_post);
            seeMore = (TextView) itemView.findViewById(R.id.feed_post_see_more_text);
            UnlikePost = (ImageView) itemView.findViewById(R.id.feed_like_view);
            LikePost = (ImageView) itemView.findViewById(R.id.feed_like_color_view);
            postTextRel = (RelativeLayout) itemView.findViewById(R.id.feed_info_relative);
            likesCount = (TextView) itemView.findViewById(R.id.feed_posts_likes_count);
            commentBox = (EditText) itemView.findViewById(R.id.feed_user_editable_comments);
            sendComment = (ImageView) itemView.findViewById(R.id.feed_user_comment_send_image);
            commentCount = (TextView) itemView.findViewById(R.id.feed_user_comments_count);
            bar = (ProgressBar) itemView.findViewById(R.id.posting_progress);
            commentView = (ImageView) itemView.findViewById(R.id.feed_comment_view);
            postVideo = (VideoView) itemView.findViewById(R.id.feed_user_video_post);
            postCard = (CardView) itemView.findViewById(R.id.feed_user_post_cardview);
        }
    }


}


package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Comments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedCommentsAdapter extends RecyclerView.Adapter<FeedCommentsAdapter.ViewHolder> {

    private Context mContext;
    private List<Comments> mComments;
    private DatabaseReference commentDatabase;
    private String mCurrentUser;

    public FeedCommentsAdapter(Context context, List<Comments> mComments) {
        this.mContext = context;
        this.mComments = mComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_comment_single_layout, parent, false);

        return new FeedCommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comments comments = mComments.get(position);

        holder.comment.setText(comments.getComment());
        holder.commenterName.setText(comments.getmId());

        commentDatabase = FirebaseDatabase.getInstance().getReference().child("postLikes")
                .child(comments.getPostId()).child("Comments");


        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        Picasso.get().load(comments.getProfile_image()).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.avatar).into(holder.userImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(comments.getProfile_image()).placeholder(R.drawable.avatar).into(holder.userImage);
                picasso.setIndicatorsEnabled(false);
            }
        });

        //--------------------like/unlike comment feature---------------//



        holder.unlikeCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation expandIn = AnimationUtils.loadAnimation(mContext, R.anim.expand_in);
                holder.unlikeCmt.setVisibility(View.GONE);
                holder.likeCmt.setVisibility(View.VISIBLE);
                holder.likeCmt.startAnimation(expandIn);


            }
        });

        //---------------like/unlike feature----------------//

        holder.likeCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation expandIn = AnimationUtils.loadAnimation(mContext, R.anim.expand_in);
                holder.likeCmt.setVisibility(View.GONE);
                holder.unlikeCmt.setVisibility(View.VISIBLE);
                holder.unlikeCmt.startAnimation(expandIn);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView commenterName, comment;
        private CircleImageView userImage;
        private ImageView unlikeCmt, likeCmt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commenterName = (TextView) itemView.findViewById(R.id.post_single_commenter_name);
            comment = (TextView) itemView.findViewById(R.id.post_single_comment);
            userImage =  (CircleImageView) itemView.findViewById(R.id.post_single_commenter_image);
            unlikeCmt = (ImageView) itemView.findViewById(R.id.comment_unlike_view);
            likeCmt = (ImageView) itemView.findViewById(R.id.comment_like_color_view);
        }
    }
}

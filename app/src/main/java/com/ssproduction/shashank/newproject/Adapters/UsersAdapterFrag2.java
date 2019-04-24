package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.UserProfileActivity;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapterFrag2 extends RecyclerView.Adapter<UsersAdapterFrag2.ViewHolder> {

    private Context mContext;
    private List<Users> mUsers;
    private boolean ischat;
    private String mCurrent_state;
    private DatabaseReference FollowCountRef;
    private DatabaseReference FollowersRef;
    private String mCurrentUser;
    private OnItemClickListener mListener;

    String theLastMessage;

    public interface OnItemClickListener{
        void onItemCick(int position);
        void onFollowClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public UsersAdapterFrag2(Context mContext, List<Users> mUsers, boolean ischat) {

        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_single_layout_frag2, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Users users = mUsers.get(position);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mCurrent_state = "unFollowing";
        FollowersRef = FirebaseDatabase.getInstance().getReference().child("Follow");
        FollowersRef.keepSynced(true);

        FollowCountRef = FirebaseDatabase.getInstance().getReference().child("FollowCount");

        String user_id = users.getId();
        holder.userName.setText(users.getSearch());
        holder.user_status.setText(users.getStatus());

        if (users.getAvatar_thumbImage().equals("default")){
            holder.userImage.setImageResource(R.drawable.avatar);
        }
        else {
            Picasso.get().load(users.getAvatar_thumbImage()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avatar).into(holder.userImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(users.getAvatar_thumbImage()).placeholder(R.drawable.avatar).into(holder.userImage);
                }
            });
        }
        //online status
        if (ischat){
            if (users.getOnline().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_off.setVisibility(View.GONE);
            holder.img_on.setVisibility(View.GONE);
        }

        FollowersRef.child(mCurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)){
                    String followerId = dataSnapshot.child(user_id).child("followerId").getValue().toString();
                    String followingId = dataSnapshot.child(user_id).child("followingId").getValue().toString();

                    if (followingId.equals(user_id) && followerId.equals("default")) {
                        mCurrent_state = "Followed_User";
                        holder.followText.setText("Unfollow");
                        holder.followText.setBackground(mContext.getDrawable(R.drawable.edit_profile_background));
                        holder.followText.setTextColor(Color.BLACK);

                    }else if (followerId.equals(user_id) && followingId.equals("default")){
                        mCurrent_state = "Get_Followed";
                        holder.followText.setText("Follow back");
                        holder.followText.setTextColor(Color.WHITE);
                        holder.followText.setBackground(mContext.getDrawable(R.drawable.user_single_follow_background));

                    }else if (followingId.equals(user_id) && followerId.equals(user_id)){
                        mCurrent_state = "both_following";
                        holder.followText.setText("Unfollow");
                        holder.followText.setBackground(mContext.getDrawable(R.drawable.edit_profile_background));
                        holder.followText.setTextColor(Color.BLACK);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.followText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----------not following state-------//
                if (mCurrent_state.equals("unFollowing")) {

                    holder.followText.setText("Unfollow");
                    holder.followText.setBackground(mContext.getDrawable(R.drawable.edit_profile_background));
                    holder.followText.setTextColor(Color.BLACK);

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
                                Toast.makeText(mContext, "Error..check your internet", Toast.LENGTH_SHORT).show();
                            }

                            mCurrent_state = "Followed_User";
                            holder.followText.setText("Unfollow");
                            holder.followText.setBackground(mContext.getDrawable(R.drawable.edit_profile_background));
                            holder.followText.setTextColor(Color.BLACK);

                        }
                    });


                }

                if (mCurrent_state.equals("Followed_User")) {
                    holder.followText.setText("Follow");
                    holder.followText.setBackground(mContext.getDrawable(R.drawable.user_single_follow_background));
                    holder.followText.setTextColor(Color.WHITE);


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

                            holder.followText.setText("Follow");
                            holder.followText.setBackground(mContext.getDrawable(R.drawable.user_single_follow_background));
                            holder.followText.setTextColor(Color.WHITE);

                        }
                    });

                }
                if (mCurrent_state.equals("Get_Followed")){
                    holder.followText.setText("Unfollow");
                    holder.followText.setBackground(mContext.getDrawable(R.drawable.edit_profile_background));
                    holder.followText.setTextColor(Color.BLACK);

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
                            holder.followText.setText("Unfollow");
                            holder.followText.setBackground(mContext.getDrawable(R.drawable.edit_profile_background));
                            holder.followText.setTextColor(Color.BLACK);

                        }
                    });

                }if (mCurrent_state.equals("both_following")){
                    holder.followText.setText("Follow");
                    holder.followText.setBackground(mContext.getDrawable(R.drawable.user_single_follow_background));
                    holder.followText.setTextColor(Color.WHITE);

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
                            holder.followText.setText("Follow");
                            holder.followText.setBackground(mContext.getDrawable(R.drawable.user_single_follow_background));
                            holder.followText.setTextColor(Color.WHITE);
                        }
                    });

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, user_status;
        private CircleImageView userImage;
        private CircleImageView img_on;
        private CircleImageView img_off;
        private TextView followText;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_single_req_name);
            user_status = (TextView) itemView.findViewById(R.id.user_single_req_status);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_single_req_image);
            img_off = (CircleImageView) itemView.findViewById(R.id.img_off);
            img_on = (CircleImageView) itemView.findViewById(R.id.img_on);
            followText = (TextView) itemView.findViewById(R.id.frag2_user_single_follow_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemCick(position);
                        }
                    }
                }
            });

            followText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onFollowClick(position);
                        }
                    }
                }
            });
        }

    }

}


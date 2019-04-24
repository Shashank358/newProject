package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.ChatActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.UserProfileActivity;
import com.ssproduction.shashank.newproject.utils.follow_request_user;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendReqAdapter extends RecyclerView.Adapter<FriendReqAdapter.ViewHolder> {

    private Context mContext;
    private List<follow_request_user> follow_request_user;
    private boolean ischat;

    String theLastMessage;


    public FriendReqAdapter(Context mContext, List<follow_request_user> follow_request_user, boolean ischat) {

        this.mContext = mContext;
        this.follow_request_user = follow_request_user;

        this.ischat = ischat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_single_req_layout, parent, false);

        return new FriendReqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final follow_request_user followRequestUser = follow_request_user.get(position);

        holder.userName.setText(followRequestUser.getUserName());

        if (followRequestUser.getAvatar_thumbImage().equals("default")){
            holder.userImage.setImageResource(R.drawable.avatar);
        }
        else {
            Picasso.get().load(followRequestUser.getAvatar_thumbImage()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avatar).into(holder.userImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(followRequestUser.getAvatar_thumbImage()).placeholder(R.drawable.avatar).into(holder.userImage);
                }
            });
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(mContext, UserProfileActivity.class);
                profileIntent.putExtra("user_id", followRequestUser.getId());
                mContext.startActivity(profileIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return follow_request_user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userName, status;
        private CircleImageView userImage;
        private CircleImageView img_on;
        private CircleImageView img_off;
        private TextView followText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_single_req_name);
            status = (TextView) itemView.findViewById(R.id.user_single_req_status);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_single_req_image);
            img_off = (CircleImageView) itemView.findViewById(R.id.img_req_off);
            img_on = (CircleImageView) itemView.findViewById(R.id.img_req_on);
            followText = (TextView) itemView.findViewById(R.id.user_single_req_follow_text);


        }
    }


}


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
import com.ssproduction.shashank.newproject.utils.Friends;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context mContext;
    private List<Friends> friends;
    private boolean ischat;


    public FriendsAdapter(Context mContext, List<Friends> friends, boolean ischat) {
        this.mContext = mContext;
        this.friends = friends;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_single_req_layout, viewGroup, false);

        return new FriendsAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        final Friends friends1 = friends.get(i);

        holder.userName.setText(friends1.getName());
        holder.status.setText(friends1.getStatus());

        if (friends1.getImage().equals("default")){
            holder.userImage.setImageResource(R.drawable.avatar);
        }
        else {
            Picasso.get().load(friends1.getImage()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avatar).into(holder.userImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(friends1.getImage()).placeholder(R.drawable.avatar).into(holder.userImage);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(mContext, ChatActivity.class);
                chatIntent.putExtra("user_id", friends1.getId());
                mContext.startActivity(chatIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return friends.size();
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

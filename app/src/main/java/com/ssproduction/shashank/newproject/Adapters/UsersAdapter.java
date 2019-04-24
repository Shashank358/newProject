package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.ssproduction.shashank.newproject.ChatActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Chats;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mContext;
    private List<Users> mUsers;
    private boolean ischat;
    private FirebaseAuth mAuth;
    String currentUserId;

    String theLastMessage;

    public UsersAdapter(Context mContext, List<Users> mUsers, boolean ischat) {

        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_single_layout_frag1, parent, false);

        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Users users = mUsers.get(position);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        holder.firstName.setText(users.getFirstName());
        holder.lastName.setText(users.getLastName());

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

        if (ischat){
            lastMessage(users.getId(), holder.last_msg);

        }else {

            holder.last_msg.setVisibility(View.GONE);
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



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(mContext, ChatActivity.class);
                chatIntent.putExtra("user_id", users.getId());
                mContext.startActivity(chatIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView firstName, lastName, last_msg;
        private CircleImageView userImage;
        private CircleImageView img_on;
        private CircleImageView img_off;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = (TextView) itemView.findViewById(R.id.user_single_req_name);
            lastName = (TextView) itemView.findViewById(R.id.user_single_last_name);
            last_msg = (TextView) itemView.findViewById(R.id.user_single_last_message);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_single_req_image);
            img_off = (CircleImageView) itemView.findViewById(R.id.img_off);
            img_on = (CircleImageView) itemView.findViewById(R.id.img_on);


        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg){

        theLastMessage = "default";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chats chats = snapshot.getValue(Chats.class);

                        if (chats.getReceiver().equals(currentUserId) && chats.getSender().equals(userid) ||
                                chats.getReceiver().equals(userid) && chats.getSender().equals(currentUserId)) {
                            if (currentUserId != null){
                                theLastMessage = chats.getMessage();

                            }
                        }
                }

                switch (theLastMessage){
                        case "default":
                          last_msg.setText("No Message");
                           break;


                        default:
                            last_msg.setText(theLastMessage);
                            break;


                }

                theLastMessage = "default";

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}


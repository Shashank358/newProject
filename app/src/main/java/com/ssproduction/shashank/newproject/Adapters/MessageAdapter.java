package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.ImgMsgActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Chats;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chats> mChats;
    private String profileThumbDP;
    private String mCurrentUser;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(Context mContext, List<Chats> mChats, String profileThumbDP) {

        this.mChats = mChats;
        this.mContext = mContext;
        this.profileThumbDP = profileThumbDP;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_right_item, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_item, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, final int position) {


        final Chats chats = mChats.get(position);

        String message_type = chats.getMsg_type();
        String sent_time = chats.getSent_time();

        if (message_type.equals("text")){
            holder.chatSendImg.setVisibility(View.GONE);
            holder.bar.setVisibility(View.GONE);
            holder.show_message.setText(chats.getMessage());
            if (chats.getSender().equals(mCurrentUser)){
                holder.txt_seen.setText(sent_time);
                holder.txt_seen.setTextColor(Color.DKGRAY);
            }else {
                holder.txt_seen.setText(sent_time);
                holder.txt_seen.setTextColor(Color.DKGRAY);
            }

        }else {
            if (chats.getMessage().equals("default")){
                holder.bar.setVisibility(View.VISIBLE);
                holder.show_message.setVisibility(View.GONE);
                holder.chatSendImg.setVisibility(View.VISIBLE);
                Picasso picasso = Picasso.get();
                Picasso.get().load(chats.getMessage()).into(holder.chatSendImg);
                picasso.setIndicatorsEnabled(false);
            }else {
                holder.bar.setVisibility(View.GONE);
                holder.show_message.setVisibility(View.GONE);
                holder.chatSendImg.setVisibility(View.VISIBLE);
                Picasso picasso = Picasso.get();
                Picasso.get().load(chats.getMessage()).into(holder.chatSendImg);
                picasso.setIndicatorsEnabled(false);
            }

        }


        if (profileThumbDP.equals("default")){
            holder.message_user_image.setImageResource(R.drawable.avatar);
        }else {

            Picasso.get().load(profileThumbDP).placeholder(R.drawable.avatar).into(holder.message_user_image);
        }


        if (chats.isIsseen() == true){
                if (message_type.equals("text")){
                    holder.txt_seen_image.setVisibility(View.GONE);
                    if (holder.txt_seen.getVisibility() == View.VISIBLE){
                        if (chats.getSender().equals(mCurrentUser)){
                            holder.SeenImg.setVisibility(View.VISIBLE);
                            holder.SeenImg.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            holder.DeliveredImg.setVisibility(View.GONE);
                        }

                    }

                }
                else {
                    holder.txt_seen.setVisibility(View.GONE);
                    holder.txt_seen_image.setVisibility(View.VISIBLE);
                    holder.txt_seen_image.setText("Seen");
                    holder.txt_seen_image.setTextColor(Color.BLUE);
                    holder.SeenImg.setVisibility(View.GONE);
                    holder.DeliveredImg.setVisibility(View.GONE);
                }

            }else {
                if (message_type.equals("text")){
                    holder.txt_seen_image.setVisibility(View.GONE);
                    if (holder.txt_seen.getVisibility() == View.VISIBLE){
                        if (chats.getSender().equals(mCurrentUser)){
                            holder.DeliveredImg.setVisibility(View.VISIBLE);
                            holder.DeliveredImg.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            holder.SeenImg.setVisibility(View.GONE);
                        }

                    }
                }
                else {
                    holder.txt_seen.setVisibility(View.GONE);
                    holder.txt_seen_image.setVisibility(View.VISIBLE);
                    holder.txt_seen_image.setText("Delivered");
                    holder.txt_seen_image.setTextColor(Color.DKGRAY);
                    holder.SeenImg.setVisibility(View.GONE);
                    holder.DeliveredImg.setVisibility(View.GONE);

                }
            }

        holder.chatSendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image_id = chats.getMessage();

                Intent i = new Intent(v.getContext(), ImgMsgActivity.class);
                i.putExtra("image_id", image_id);
                mContext.startActivity(i);

            }
        });

        holder.chatSendImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.chatSendImg.setVisibility(View.GONE);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {

        return mChats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public CircleImageView message_user_image;
        public TextView txt_seen, txt_seen_image;
        public ImageView chatSendImg, DeliveredImg, SeenImg;
        public ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = (TextView) itemView.findViewById(R.id.show_message);
            message_user_image = (CircleImageView) itemView.findViewById(R.id.other_messege_image);
            txt_seen = (TextView) itemView.findViewById(R.id.text_seen);
            txt_seen_image = (TextView) itemView.findViewById(R.id.txt_seen_image);
            chatSendImg = (ImageView) itemView.findViewById(R.id.chat_send_image);
            DeliveredImg = (ImageView) itemView.findViewById(R.id.delivered_icon);
            SeenImg = (ImageView) itemView.findViewById(R.id.seen_icon);
            bar = (ProgressBar) itemView.findViewById(R.id.image_load_bar);
        }
    }

    @Override
    public int getItemViewType(int position) {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (mChats.get(position).getSender().equals(mCurrentUser)){

            return MSG_TYPE_RIGHT;
        }
        else {

            return MSG_TYPE_LEFT;
        }

    }

}

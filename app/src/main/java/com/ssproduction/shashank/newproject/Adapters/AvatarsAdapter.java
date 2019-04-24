package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.utils.Avatars;
import com.ssproduction.shashank.newproject.utils.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarsAdapter extends RecyclerView.Adapter<AvatarsAdapter.ViewHolder> {

    private Context mContext;
    private List<Avatars> mAvatars;

    public AvatarsAdapter(Context mContext, List<Avatars> mAvatars) {
        this.mAvatars = mAvatars;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AvatarsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.avatar_items, parent, false);

        return new AvatarsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarsAdapter.ViewHolder holder, int position) {

        Avatars avatars = mAvatars.get(position);

        Picasso.get().load(avatars.getAvatar_image()).into(holder.firstImage);
        Picasso.get().load(avatars.getAvatar_image()).into(holder.firstImage);
        Picasso.get().load(avatars.getAvatar_image()).into(holder.firstImage);

    }

    @Override
    public int getItemCount() {
        return mAvatars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView firstImage , secondImage, thirdImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            firstImage = (ImageView) itemView.findViewById(R.id.first_avatar);
            secondImage = (ImageView) itemView.findViewById(R.id.second_avatar);
            thirdImage = (ImageView) itemView.findViewById(R.id.third_avatar);


        }
    }
}

package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ssproduction.shashank.newproject.R;

public class ListAdapter extends RecyclerView.Adapter {

    private Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_single_post_layout, parent, false);
        return new ListViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ListViewHolder)holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return OurData.picturePath.length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImage;
        private ImageView likeImageOFF, likeImageON;

        Context context;

        public ListViewHolder(View itemView, Context context){

            super(itemView);
            this.context = context;
            mItemImage = (ImageView) itemView.findViewById(R.id.feed_user_post);
            likeImageOFF = (ImageView) itemView.findViewById(R.id.feed_like_view);
            likeImageON = (ImageView) itemView.findViewById(R.id.feed_like_color_view);

            likeImageOFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeImageOFF.setVisibility(View.GONE);
                    likeImageON.setVisibility(View.VISIBLE);
                }
            });

            likeImageON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeImageOFF.setVisibility(View.VISIBLE);
                    likeImageON.setVisibility(View.GONE);
                }
            });
        }

        public void bindView(int position){
            mItemImage.setImageResource(OurData.picturePath[position]);
        }
    }
}

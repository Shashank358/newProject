package com.ssproduction.shashank.newproject.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssproduction.shashank.newproject.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class newAdapter extends RecyclerView.Adapter {

    private Context context;

    public newAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_single_status_layout, parent, false);
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

        private CircleImageView mItemImage;
        private TextView userName;
        Context context;

        public ListViewHolder(View itemView, Context context){

            super(itemView);
            this.context = context;
            mItemImage = (CircleImageView) itemView.findViewById(R.id.user_single_status_profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_single_status_profile_name);
        }

        public void bindView(int position){
            mItemImage.setImageResource(OurData.picturePath[position]);
            userName.setText("userName");
        }
    }
}

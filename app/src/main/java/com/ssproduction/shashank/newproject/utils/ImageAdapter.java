package com.ssproduction.shashank.newproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssproduction.shashank.newproject.R;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Avatars");

            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {

            R.drawable.sample_01, R.drawable.sample_02,
            R.drawable.sample_03, R.drawable.sample_04,
            R.drawable.sample_05, R.drawable.sample_06,
            R.drawable.sample_07, R.drawable.sample_08,
            R.drawable.sample_09, R.drawable.sample_10,
            R.drawable.sample_11, R.drawable.sample_13,
            R.drawable.sample_12
    };


}

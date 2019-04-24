package com.ssproduction.shashank.newproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImgMsgActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private ImageView sentImageView, sendImgBtn, imgFilter, imgText, imgdraw, imgcrop, imgEmoji;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_msg);


        mToolbar = (Toolbar) findViewById(R.id.chat_image_prev_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String imageView = getIntent().getStringExtra("image_id");

        ImageView image_view = (ImageView) findViewById(R.id.user_chat_sent_image_view);


        Picasso picasso = Picasso.get();
        Picasso.get().load(imageView).into(image_view);
        picasso.setIndicatorsEnabled(false);


    }
}

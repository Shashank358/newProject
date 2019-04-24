package com.ssproduction.shashank.newproject;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PostPreviewActivity extends AppCompatActivity {

    PhotoViewAttacher pAttacher;
    PhotoView post;

    android.support.v7.widget.Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview);

        String post_image = getIntent().getStringExtra("postImage");
        String imagePost = getIntent().getStringExtra("image");

        post = (PhotoView) findViewById(R.id.post_show_image_view);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.post_show_image_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Picasso picasso = Picasso.get();
        picasso.load(imagePost).into(post);
        pAttacher = new PhotoViewAttacher(post);
        pAttacher.update();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }
}

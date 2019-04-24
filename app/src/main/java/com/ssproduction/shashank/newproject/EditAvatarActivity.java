package com.ssproduction.shashank.newproject;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssproduction.shashank.newproject.Adapters.AvatarsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAvatarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GridView gridView;

    private DatabaseReference mUserDatabase;
    private String mCurrentUser;
    private FirebaseAuth mAuth;

    private RecyclerView avatarList;

    private ArrayList<String> image;
    private AvatarsAdapter avatarsAdapter;

    private FirebaseUser fuser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);

        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.edit_avatar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Avatar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_circle_image);


        GridView Gallery = (GridView) findViewById(R.id.edit_avatar_grid_view);

        Gallery.setAdapter(new Adapter(this));
        Gallery.setBackgroundColor(Color.BLACK);

        Gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });



    }


    private void online(String online){

        mCurrentUser = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", online);


        mUserDatabase.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        online("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        online("offline");
    }




    private class Adapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public Adapter(Activity localContext) {
            context = localContext;
            image = getAllShownImagesPath(context);
        }

        public int getCount() {
            return image.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                picturesView.setLayoutParams(new GridView.LayoutParams(200, 200));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(image.get(position))
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }
}

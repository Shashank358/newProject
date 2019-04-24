package com.ssproduction.shashank.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.utils.Posts;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private static final int GALLERY_PIC = 1;
    private StorageReference mImageStorage;

    private DatabaseReference mDatabase, mUserDatabase;
    private FirebaseUser mCurrentUser;

    private String current_user;

    private ProgressDialog dialog;

    private FirebaseAuth mAuth;


    private CircleImageView profileCircleImage;
    private TextView profileUserFirstName;
    private TextView profileUserStatus;
    private TextView profileUserLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileCircleImage = (CircleImageView) findViewById(R.id.profile_circle_image);
        profileUserFirstName = (TextView) findViewById(R.id.profile_user_first_name);
        profileUserStatus = (TextView) findViewById(R.id.profile_status);

        dialog = new ProgressDialog(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDatabase.keepSynced(true);

        mImageStorage = FirebaseStorage.getInstance().getReference();
        VideoView videoView = (VideoView) findViewById(R.id.account_video);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Posts posts = dataSnapshot.getValue(Posts.class);

                MediaController mc = new MediaController(AccountActivity.this);
                videoView.setMediaController(mc);
                videoView.setVideoPath(posts.getUserPost());
                videoView.requestFocus();
                videoView.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        profileCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT PROFILE IMAGE"), GALLERY_PIC);

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String userFirstName = dataSnapshot.child("firstName").getValue().toString();
                String userLastName = dataSnapshot.child("lastName").getValue().toString();
                String userStatus =  dataSnapshot.child("status").getValue().toString();
                final String userImage = dataSnapshot.child("profileDP").getValue().toString();
                String userThumbImage = dataSnapshot.child("profileThumbDP").getValue().toString();


                if (!userImage.equals("default")){

                    Picasso.get().load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.avatar).into(profileCircleImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(userImage).placeholder(R.drawable.avatar)
                                    .into(profileCircleImage);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PIC && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                dialog.setTitle("Updating Profile");
                dialog.setMessage("please wait while profile is updating");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Uri resultUri = result.getUri();

                final File thumb_filePath = new File(resultUri.getPath());

                String user_uid = mCurrentUser.getUid();

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(50)
                        .compressToBitmap(thumb_filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("profile_images").child(user_uid + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumb")
                        .child(user_uid + "jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {

                                    String thumb_downloadUrl = thumbTask.getResult().getDownloadUrl().toString();

                                    if (thumbTask.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("profileDP", download_url);
                                        update_hashMap.put("profileThumbDP", thumb_downloadUrl);

                                        mDatabase.updateChildren(update_hashMap).addOnCompleteListener
                                                (new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {


                                                if (task.isSuccessful()){
                                                    dialog.dismiss();
                                                    Toast.makeText(AccountActivity.this, "success", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });
                                    }
                                    else {
                                        dialog.hide();
                                        Toast.makeText(AccountActivity.this, "error uploading", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }


                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception error = result.getError();

            }
        }
    }

    private void online(String online){

        current_user = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(current_user);

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
}

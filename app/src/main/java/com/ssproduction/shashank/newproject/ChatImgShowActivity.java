package com.ssproduction.shashank.newproject;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;

public class ChatImgShowActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private ImageView sentImageView, sendImgBtn, imgFilter, imgText, imgdraw, imgcrop, imgEmoji;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_img_show);

        final String sentImage = getIntent().getStringExtra("image_info");
        final String user_id = getIntent().getStringExtra("user_id");
        final String currentUser = getIntent().getStringExtra("current_user");
        final byte[] myData = getIntent().getByteArrayExtra("myData");


        sentImageView = (ImageView) findViewById(R.id.user_chat_sent_image);
        sendImgBtn = (ImageView) findViewById(R.id.send_image_to_chat);

        mToolbar = (Toolbar) findViewById(R.id.chat_image_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.getBackground().setAlpha(90);

        final DatabaseReference chatDatabase = FirebaseDatabase.getInstance().getReference();

        StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();

        StorageReference filePath = mImageStorage.child("chatImages").child(currentUser)
                .child(user_id).child(random() + ".jpg");
        final StorageReference thumb_filepath = mImageStorage.child("chatImages").child("thumb")
                .child(currentUser).child(user_id).child(random() + ".jpg");


        Picasso picasso = Picasso.get();
        Picasso.get().load(sentImage).into(sentImageView);
        picasso.setIndicatorsEnabled(false);


        sendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatImgShowActivity.super.onBackPressed();

                final HashMap<String, Object> newMap = new HashMap<>();
                newMap.put("sender", currentUser);
                newMap.put("receiver", user_id);
                newMap.put("message", "default");
                newMap.put("isseen", false);
                newMap.put("msg_type", "image");

                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                        .child(currentUser)
                        .child(user_id);

                final String pushId = chatDatabase.child("Chats").push().getKey();

                chatDatabase.child("Chats").child(pushId).setValue(newMap);

                UploadTask uploadTask = thumb_filepath.putBytes(myData);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {

                        String thumb_downloadUrl = thumbTask.getResult().getDownloadUrl().toString();

                        if (thumbTask.isSuccessful()){

                            final HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("sender", currentUser);
                            hashMap.put("receiver", user_id);
                            hashMap.put("message", thumb_downloadUrl);
                            hashMap.put("isseen", false);
                            hashMap.put("msg_type", "image");


                            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                                    .child(currentUser)
                                    .child(user_id);

                            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()){

                                        chatRef.child("id").setValue(user_id);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            if (thumbTask.isSuccessful()){

                                chatDatabase.child("Chats").child(pushId).setValue(hashMap);

                            }

                        }
                    }

                });


            }
        });



    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}

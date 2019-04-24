package com.ssproduction.shashank.newproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.Notifications.Data;
import com.ssproduction.shashank.newproject.models.ImagePicker;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class PrevePostActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private CircleImageView userProfile;
    private ImageView postImage;
    private EditText saySomething;
    private TextView userName;
    private TextView postDone;
    private Intent data;

    private DatabaseReference mDatabase;
    private StorageReference mImageStorage;
    private String pushId;
    private RelativeLayout postingShow;

    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private String current_user;

    private FloatingActionButton addFab;
    private LinearLayout postOptions, addPhoto, addVideo, addLocation, addCamera, addPolo, addGif, addQuestions, addGreeting;
    private ImageView postHide;
    private static final int VIDEO_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preve_post);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            data = (Intent) bundle.get("data");
        }

        String post_image = getIntent().getStringExtra("post_image");
        int resultCode = getIntent().getIntExtra("resultCode",1);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.post_preview_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Post Preview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);

        userProfile = (CircleImageView) findViewById(R.id.post_prev_user_profile);
        postImage = (ImageView) findViewById(R.id.post_prev_image);
        saySomething = (EditText) findViewById(R.id.post_prev_tell_something_edit_text);
        userName = (TextView) findViewById(R.id.post_prev_user_name);
        postDone = (TextView) findViewById(R.id.post_prev_post_done_text);
        addFab = (FloatingActionButton) findViewById(R.id.add_post_option_fab);
        postOptions = (LinearLayout) findViewById(R.id.add_post_options);
        postHide = (ImageView) findViewById(R.id.post_option_cancel_image);
        addPhoto = (LinearLayout) findViewById(R.id.post_photos_linear);
        addVideo = (LinearLayout) findViewById(R.id.post_videos_linear);
        addCamera = (LinearLayout) findViewById(R.id.post_camera_linear);
        addPolo = (LinearLayout) findViewById(R.id.post_polo_linear);
        addLocation = (LinearLayout) findViewById(R.id.post_location_linear);
        addGif = (LinearLayout) findViewById(R.id.post_gif_linear);
        addQuestions = (LinearLayout) findViewById(R.id.post_question_linear);
        addGreeting = (LinearLayout) findViewById(R.id.post_greeting_linear);

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(post_image).into(postImage);


        addFab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                addFab.setVisibility(View.GONE);
                postOptions.setVisibility(View.VISIBLE);

            }
        });

        postHide.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                addFab.setVisibility(View.VISIBLE);
                postOptions.setVisibility(View.GONE);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String user_image = dataSnapshot.child("avatar_thumbImage").getValue().toString();
                String user_name = dataSnapshot.child("search").getValue().toString();

                picasso.load(user_image).into(userProfile);
                userName.setText(user_name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saySomething.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //---------add post feature----------------------[//
        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("video/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), VIDEO_PICK);

            }
        });



        postDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PrevePostActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                String post_text = saySomething.getText().toString().trim();
                int count = saySomething.getLineCount();



                Uri resultUri = data.getData();

                final Bitmap bitmap = ImagePicker.getImageFromResult(getApplicationContext(), resultCode, data);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                final byte[] thumb_byte = baos.toByteArray();


                mImageStorage = FirebaseStorage.getInstance().getReference().child("Posts");

                StorageReference filepath = mImageStorage.child(current_user).child(random() + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child(current_user).child("thumb")
                        .child(random() + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {

                                    String thumb_downloadUrl = thumbTask.getResult().getDownloadUrl().toString();

                                    if (thumbTask.isSuccessful()) {

                                        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);

                                        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                String user_image = dataSnapshot.child("avatar_thumbImage").getValue().toString();
                                                String user_name = dataSnapshot.child("search").getValue().toString();
                                                String id = dataSnapshot.child("id").getValue().toString();

                                                Date date = new Date();
                                                String strDateFormat = "dd MMM' - 'hh:mm a";
                                                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                                                String formattedDate= dateFormat.format(date);

                                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
                                                pushId = mDatabase.push().getKey();


                                                Map userMap = new HashMap();
                                                userMap.put("userPost", download_url);
                                                userMap.put("userThumbPost", thumb_downloadUrl);
                                                userMap.put("Id", id);
                                                userMap.put("post_type", "photo");
                                                userMap.put("user_image", user_image);
                                                userMap.put("user_name", user_name);
                                                userMap.put("posting_time", formattedDate);
                                                userMap.put("lineCount", count);
                                                userMap.put("liked_by", "default");
                                                userMap.put("pushId", pushId);
                                                if (!post_text.equals("")) {
                                                    userMap.put("user_posted_text", post_text);
                                                }else {
                                                    userMap.put("user_posted_text", "");
                                                }
                                                Map postMap = new HashMap();
                                                postMap.put("postId", pushId);

                                                mDatabase.child(pushId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()){
                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PostCount")
                                                                    .child(current_user);
                                                            reference.child(pushId).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    Toast.makeText(PrevePostActivity.this, "success", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });


                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {


                                            }
                                        });
                                    }
                                }
                            });
                        }

                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_PICK && resultCode == RESULT_OK){

            String post_text = saySomething.getText().toString().trim();
            int count = saySomething.getLineCount();

            dialog.setTitle("Updating Profile");
            dialog.setMessage("please wait while profile is updating");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Uri resultUri = data.getData();

            StorageReference videoStorage = FirebaseStorage.getInstance().getReference().child("post_video");

            StorageReference filepath = videoStorage.child(current_user).child(random()+ ".mp4");

            filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        final String download_url = task.getResult().getDownloadUrl().toString();

                        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);

                        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String user_image = dataSnapshot.child("avatar_thumbImage").getValue().toString();
                                String user_name = dataSnapshot.child("search").getValue().toString();
                                String id = dataSnapshot.child("id").getValue().toString();

                                Date date = new Date();
                                String strDateFormat = "dd MMM' - 'hh:mm a";
                                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                                String formattedDate = dateFormat.format(date);

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
                                pushId = mDatabase.push().getKey();


                                Map userMap = new HashMap();
                                userMap.put("userPost", download_url);
                                userMap.put("userThumbPost", "default");
                                userMap.put("Id", id);
                                userMap.put("post_type", "video");
                                userMap.put("user_image", user_image);
                                userMap.put("user_name", user_name);
                                userMap.put("posting_time", formattedDate);
                                userMap.put("lineCount", "default");
                                userMap.put("liked_by", "default");
                                userMap.put("pushId", pushId);
                                if (!post_text.equals("")) {
                                    userMap.put("user_posted_text", post_text);
                                } else {
                                    userMap.put("user_posted_text", "");
                                }
                                Map postMap = new HashMap();
                                postMap.put("postId", pushId);

                                mDatabase.child(pushId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PostCount")
                                                    .child(current_user);
                                            reference.child(pushId).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    Toast.makeText(PrevePostActivity.this, "success", Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {


                            }
                        });
                    }
                }
            });
        }

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();

    }
}

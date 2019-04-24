package com.ssproduction.shashank.newproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ssproduction.shashank.newproject.Fragment.APIService;
import com.ssproduction.shashank.newproject.Fragment.DialogFragment;
import com.ssproduction.shashank.newproject.Notifications.Client;
import com.ssproduction.shashank.newproject.Notifications.Data;
import com.ssproduction.shashank.newproject.Notifications.MyResponse;
import com.ssproduction.shashank.newproject.Notifications.Sender;
import com.ssproduction.shashank.newproject.Notifications.Token;
import com.ssproduction.shashank.newproject.models.ImagePicker;
import com.ssproduction.shashank.newproject.utils.Chats;
import com.ssproduction.shashank.newproject.Adapters.MessageAdapter;
import com.ssproduction.shashank.newproject.utils.Users;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity{

    private TextView chatUserFirstName, chatUserLastName, chatUserLastSeen;
    private CircleImageView chatUserImage;
    private Toolbar mToolbar;
    private ImageView cameraAttach;
    private ImageView attachThings, sendAudio;
    private static final int GALLERY_PIC = 1;
    private static final int CAMERA_PIC_REQUEST = 2;
    private ImageView chatThemeImg;
    private ImageView emoji_image;
    private ImageView sendMsgView;
    private EditText textMsg;
    private View attachDialog;

    private String mFileName = null;

    private MediaRecorder mRecorder;
    private static final String LOG_TAG = "Record_log";

    private DatabaseReference mUserDatabase, mChatDatabase;

    private String mCurrentUser;

    private MessageAdapter messageAdapter;
    private List<Chats> mChats;

    private FirebaseAuth mAuth;
    private StorageReference mImageStorage;

    private ProgressDialog dialog;

    private RecyclerView messagesList;
    private RelativeLayout attachCameraRel;

    ValueEventListener seenListener;
    private String user_id;

    APIService apiService;
    boolean notify = false;
    String download_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        chatUserFirstName = (TextView) findViewById(R.id.chat_user_first_name);
        chatUserLastName = (TextView) findViewById(R.id.chat_user_last_name);
        chatUserLastSeen = (TextView) findViewById(R.id.chat_user_last_seen);
        chatUserImage = (CircleImageView) findViewById(R.id.chat_user_image);
        textMsg = (EditText) findViewById(R.id.send_msg_text);
        sendMsgView = (ImageView) findViewById(R.id.send_msg_imageview);
        cameraAttach = (ImageView) findViewById(R.id.chat_box_camera_imageview);
        attachThings = (ImageView) findViewById(R.id.chat_box_attach_imageview);
        chatThemeImg = (ImageView) findViewById(R.id.chat_theme_image);
        sendAudio = (ImageView) findViewById(R.id.send_audio_imgeview);
        emoji_image = (ImageView) findViewById(R.id.emoji_icon);
        attachDialog = (View) findViewById(R.id.attach_file_dialog_box);



        messagesList = (RecyclerView) findViewById(R.id.messages_list);
        messagesList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        messagesList.setLayoutManager(layoutManager);

        user_id = getIntent().getStringExtra("user_id");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mUserDatabase.keepSynced(true);

        mChatDatabase = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(mCurrentUser)
                .child(user_id);
        mChatDatabase.keepSynced(true);


        mChatDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("chatTheme")){

                    String chat_theme = dataSnapshot.child("chatTheme").getValue().toString();

                    Picasso picasso = Picasso.get();
                    Picasso.get().load(chat_theme).into(chatThemeImg);
                    picasso.setIndicatorsEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);

                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String userImage = dataSnapshot.child("profileThumbDP").getValue().toString();

                chatUserFirstName.setText(firstName);
                chatUserLastName.setText(lastName);

                if (users.getOnline().equals("online")){
                    chatUserLastSeen.setText("Online");
                }else {

                    chatUserLastSeen.setText("Offline");
                }


                if (users.getProfileThumbDP().equals("default")){
                    chatUserImage.setImageResource(R.drawable.avatar);
                }else {

                    Picasso.get().load(userImage).placeholder(R.drawable.avatar).into(chatUserImage);

                }

                readMessages(mCurrentUser, user_id, users.getProfileThumbDP());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        seenMessage(user_id);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.mp3";

        sendMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg_text = textMsg.getText().toString();

                if (!msg_text.equals("")){

                    sendMessage(mCurrentUser, user_id, msg_text);
                }
                else {

                    Toast.makeText(ChatActivity.this, "type something", Toast.LENGTH_SHORT).show();
                }

                textMsg.setText("");

            }
        });

        cameraAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "camera_clicked", Toast.LENGTH_SHORT).show();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });


        attachThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.rotation_instance);
                Animation animation1 = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.rotation_after);
                Animation animation2 = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.slide_down);
                Animation animation3 = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.slide_up);

                Bundle bundle = new Bundle();
                bundle.putString("current_user", mCurrentUser);
                bundle.putString("user_id", user_id);
                DialogFragment fragment = new DialogFragment();
                fragment.setArguments(bundle);

                if (attachDialog.getVisibility() == View.GONE){
                    attachThings.startAnimation(animation);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up, R.anim.no_animation);
                    transaction.replace(R.id.attach_file_dialog_box, fragment);
                    transaction.commit();
                    attachDialog.setVisibility(View.VISIBLE);
                    attachDialog.startAnimation(animation3);


                }else if (attachDialog.getVisibility() == View.VISIBLE){
                    attachThings.startAnimation(animation1);
                    attachDialog.startAnimation(animation2);
                    attachDialog.setVisibility(View.GONE);

                }
            }
        });


        textMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textMsg.getText().toString().equals("")){

                    cameraAttach.setVisibility(View.VISIBLE);
                    sendAudio.setVisibility(View.VISIBLE);
                    sendMsgView.setVisibility(View.GONE);
                    emoji_image.setVisibility(View.VISIBLE);
                    attachThings.setVisibility(View.VISIBLE);

                }else
                {
                    cameraAttach.setVisibility(View.GONE);
                    sendAudio.setVisibility(View.GONE);
                    sendMsgView.setVisibility(View.VISIBLE);
                    emoji_image.setVisibility(View.VISIBLE);
                    attachThings.setVisibility(View.GONE);
                    if (attachDialog.getVisibility() == View.VISIBLE){
                        Animation animation1 = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.rotation_after);
                        Animation animation2 = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.slide_down);
                        attachThings.startAnimation(animation1);
                        attachDialog.startAnimation(animation2);
                        attachDialog.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void seenMessage(final String userid){

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chats = snapshot.getValue(Chats.class);
                    if (chats.getReceiver().equals(mCurrentUser) && chats.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);

                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void sendMessage(String Sender, final String Receiver, String Message) {

        Date date = new Date();
        String strDateFormat = "hh:mm a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);

        mUserDatabase = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", Sender);
        hashMap.put("receiver", Receiver);
        hashMap.put("message", Message);
        hashMap.put("isseen", false);
        hashMap.put("msg_type", "text");
        hashMap.put("sent_time", formattedDate);

        mUserDatabase.child("Chats").push().setValue(hashMap);
        //add user to chat fragment

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(mCurrentUser)
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

        final String msg = Message;

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUser);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);
                if (notify) {
                    sendNotification(Receiver, users.getSearch(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void sendNotification(String receiver, final String username, final String message){
        final DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(mCurrentUser, R.mipmap.ic_launcher, username+": "+message, "New Message",
                            user_id);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void readMessages(final String myId, final String userId, final String imageUrl){
        mChats = new ArrayList<>();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Chats");
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mChats.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chats = snapshot.getValue(Chats.class);
                    if (chats.getReceiver().equals(myId) && chats.getSender().equals(userId) ||
                            chats.getReceiver().equals(userId) && chats.getSender().equals(myId)){

                        mChats.add(chats);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this, mChats, imageUrl);
                    messagesList.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String user_id){

        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", user_id);
        editor.apply();

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

        currentUser(user_id);
        seenMessage(user_id);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mUserDatabase.removeEventListener(seenListener);
        online("offline");

        currentUser("none");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.view_profile_item){
            Toast.makeText(this, "view profile", Toast.LENGTH_SHORT).show();
            Intent profileINtent = new Intent(ChatActivity.this, UserProfileActivity.class);
            profileINtent.putExtra("user_id", user_id);
            startActivity(profileINtent);

        }
        if (item.getItemId() == R.id.chat_theme_item){
            Toast.makeText(this, "chat theme", Toast.LENGTH_SHORT).show();

            CharSequence options[] = new CharSequence[]{"Open Gallery","Chat Theme", "Default", "None"};

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Select Options");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //click event for each item
                    if (which == 0){
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT PROFILE IMAGE"), GALLERY_PIC);
                    }
                    if (which == 1){
                        Toast.makeText(ChatActivity.this, "select chat theme", Toast.LENGTH_SHORT).show();
                        Intent chatthemeIntent = new Intent(ChatActivity.this, ChatThemeActivity.class);
                        startActivity(chatthemeIntent);

                    }
                    if (which == 2){
                        Toast.makeText(ChatActivity.this, "Default", Toast.LENGTH_SHORT).show();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chatlist")
                                .child(mCurrentUser).child(user_id).child("chatTheme");
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Chatlist")
                                .child(user_id).child(mCurrentUser).child("chatTheme");

                        reference.setValue("default");
                        reference1.setValue("default");

                        chatThemeImg.setBackground(getResources().getDrawable(R.drawable.main_chat_wal));


                    }
                    if (which == 3){
                        Toast.makeText(ChatActivity.this, "none", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            builder.show();

        }
        if (item.getItemId() == R.id.chat_privacy_item){
            Toast.makeText(this, "chat privacy", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.block_item){
            Toast.makeText(this, "block", Toast.LENGTH_SHORT).show();
        }

       return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK){

            String imageUri = data.getData().toString();

            final Bitmap bitmap = ImagePicker.getImageFromResult(ChatActivity.this, resultCode, data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            final byte[] myData = baos.toByteArray();

            byte[] myByte = myData;

            Intent imgPrev = new Intent(getApplicationContext(), ChatImgShowActivity.class);
            imgPrev.putExtra("image_info", imageUri);
            imgPrev.putExtra("myData", myByte);
            startActivity(imgPrev);

        }

        if (requestCode == GALLERY_PIC && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(2,3)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                dialog.setTitle("Updating Chat Theme");
                dialog.setMessage("please wait while chat theme is updating");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Uri resultUri = result.getUri();

                StorageReference filepath = mImageStorage.child("chatImages").child("ChatTheme").child(mCurrentUser).child(user_id).child(user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            download_url = task.getResult().getDownloadUrl().toString();

                            if (task.isSuccessful()){

                                final HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("chatTheme", download_url);
                                hashMap.put("id", user_id);

                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                                        .child("Chatlist").child(mCurrentUser).child(user_id);

                                mDatabase.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            mDatabase.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    String chat_theme = dataSnapshot.child("chatTheme").getValue().toString();
                                                    Picasso picasso = Picasso.get();
                                                    Picasso.get().load(chat_theme).into(chatThemeImg);
                                                    picasso.setIndicatorsEnabled(false);

                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }
                                });

                                final HashMap<String, Object> hashMap1 = new HashMap<>();
                                hashMap1.put("chatTheme", download_url);
                                hashMap1.put("id", mCurrentUser);

                                final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                                        .child("Chatlist").child(user_id).child(mCurrentUser);

                                database.updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            database.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    String chat_theme = dataSnapshot.child("chatTheme").getValue().toString();
                                                    Picasso picasso = Picasso.get();
                                                    Picasso.get().load(chat_theme).into(chatThemeImg);
                                                    picasso.setIndicatorsEnabled(false);

                                                    dialog.dismiss();
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
                        else {
                            dialog.hide();
                            Toast.makeText(ChatActivity.this, "error in updating chat theme", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception error = result.getError();

            }
        }

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

}

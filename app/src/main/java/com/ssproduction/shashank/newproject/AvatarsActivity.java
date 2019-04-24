package com.ssproduction.shashank.newproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class AvatarsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private StorageReference mAvatarStorage;
    private FirebaseAuth mAuth;
    private String mCurrentUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatars);

        mCurrentUSer = mAuth.getCurrentUser().getUid();


    }
}

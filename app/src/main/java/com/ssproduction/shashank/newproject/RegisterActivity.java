package com.ssproduction.shashank.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout userFirstName, userLastName, userEmailMob, userPassword;
    private TextView linkToLogin;
    private Button createAccountBtn;

    private Toolbar mToolbar;

    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mUserDatabase;
    String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userFirstName = (TextInputLayout) findViewById(R.id.reg_first_name);
        userLastName = (TextInputLayout) findViewById(R.id.reg_last_name);
        userEmailMob = (TextInputLayout) findViewById(R.id.reg_email_mobile);
        userPassword = (TextInputLayout) findViewById(R.id.reg_password);
        linkToLogin = (TextView) findViewById(R.id.register_link_to_login_text);
        createAccountBtn = (Button) findViewById(R.id.register_create_account_button);

        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(regIntent);

            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = userFirstName.getEditText().getText().toString();
                String lastName = userLastName.getEditText().getText().toString();
                String email = userEmailMob.getEditText().getText().toString();
                String password = userPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(firstName) || !TextUtils.isEmpty(lastName) || !TextUtils.isEmpty(email) ||
                        !TextUtils.isEmpty(password))
                {
                    dialog.setTitle("Creating Account");
                    dialog.setMessage("please wait while we are creating your account");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    AccountCreated(firstName, lastName, email, password);

                }else{
                    Toast.makeText(RegisterActivity.this, "please fill the following details..", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void AccountCreated(final String firstName, final String lastName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    mCurrentUser = mAuth.getCurrentUser().getUid();
                    String userId = mCurrentUser;
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);


                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("firstName", firstName);
                    userMap.put("lastName", lastName);
                    userMap.put("status", "Hey..i am using Funchat");
                    userMap.put("profileDP", "default");
                    userMap.put("profileThumbDP", "default");
                    userMap.put("avatar_image", "default");
                    userMap.put("avatar_thumbImage", "default");
                    userMap.put("avatar_name", "Avatar");
                    userMap.put("college", "default");
                    userMap.put("id", userId);
                    userMap.put("online", "offline");
                    userMap.put("search", (firstName + " " + lastName).toLowerCase());

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                dialog.dismiss();

                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }

                        }
                    });


                }
                else {
                    dialog.hide();
                    Toast.makeText(RegisterActivity.this, "Error..please  try again later", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}

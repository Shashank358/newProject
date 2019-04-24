package com.ssproduction.shashank.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userEmailMob, userPassword;
    private TextView linkToReg;
    private Button loginBtn;

    private android.support.v7.widget.Toolbar mToolbar;

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;
    private DatabaseReference mUserDatabase;
    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userEmailMob = (TextInputLayout) findViewById(R.id.login_email_mobile);
        userPassword = (TextInputLayout) findViewById(R.id.login_password);
        linkToReg = (TextView) findViewById(R.id.login_link_to_register);
        loginBtn = (Button) findViewById(R.id.login_button);

        linkToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(regIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = userEmailMob.getEditText().getText().toString();
                String password = userPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(password))
                {
                    dialog.setTitle("Logging Account");
                    dialog.setMessage("please wait while we are logging your account");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    LoginAccount( email, password);
                }
                if (TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Please fill the following details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginAccount(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        }
                        else {
                            dialog.hide();
                            Toast.makeText(LoginActivity.this, "Error..please  try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

}

package com.fyp.sarcasmania.app.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.sarcasmania.app.Classes.FirebaseHelper;
import com.fyp.sarcasmania.app.Classes.User;
import com.fyp.sarcasmania.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class signup extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    TextView textView;
    EditText username;
    EditText email;
    EditText password;
    EditText confirmpassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_signup);


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.actionBarColor)));
        textView = findViewById(R.id.pressLogin);
        username = findViewById(R.id.signupUsername);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        confirmpassword = findViewById(R.id.signupConfirmPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),login.class);
                startActivity(i);
            }
        });

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }
    }

    public void onSignupClick (View view) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Signing up...", "Please wait...", true);

        if(username.getText().toString().length() < 1) {
            progressDialog.dismiss();
            username.setError("Username cannot be Empty");
            return;
        }
        if(email.getText().toString().length() < 1) {
            progressDialog.dismiss();
            email.setError("Email cannot be Empty");
            return;
        }
        if(password.getText().toString().length() < 1) {
            progressDialog.dismiss();
            password.setError("Password cannot be Empty");
            return;
        }
        if(confirmpassword.getText().toString().length() < 1) {
            progressDialog.dismiss();
            confirmpassword.setError("Password cannot be Empty");
            return;
        }
        if(!password.getText().toString().equals(confirmpassword.getText().toString())) {
            progressDialog.dismiss();
            password.setError("Passwords not same");
            confirmpassword.setError("Passwords not same");
            return;
        }

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            User user;
            boolean check = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        user = dsp.getValue(User.class);
                        if(user != null) {
                            if(user.getUsername().equals(username.getText().toString())) {
                                progressDialog.dismiss();
                                username.setError("Account with this username already Exists");
                                check = true;
                                break;
                            }
                            if(user.getEmail().equals(email.getText().toString())) {
                                progressDialog.dismiss();
                                email.setError("Account with this email already Exists");
                                check = true;
                                break;
                            }
                        }
                    }
                }
                databaseReference.removeEventListener(this);
                if(check == false) {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    FirebaseHelper firebaseHelper = new FirebaseHelper();
                                    firebaseHelper.newUser(username.getText().toString(),"",email.getText().toString(),password.getText().toString(),"","user");
                                    Log.d("Success: ",task.getResult().toString());
                                    progressDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(),login.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Log.d("Error: ",task.getException().getMessage());
                                    progressDialog.dismiss();
                                    runOnUiThread(() -> Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show());
                                }
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
    public void onBackPressed() {
        super.onBackPressed();
//        finish();

    }
}

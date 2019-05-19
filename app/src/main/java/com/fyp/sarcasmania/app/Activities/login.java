package com.fyp.sarcasmania.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class login extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    EditText email;
    EditText password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_login);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.actionBarColor)));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        textView = findViewById(R.id.textView5);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),signup.class);
                startActivity(i);
            }
        });
    }

    public void onClick(View v) {
        if(email.getText().toString().length() < 1) {
            email.setError("Email cannot be Empty");
            return;
        }
        if(password.getText().toString().length() < 1) {
            password.setError("Password cannot be Empty");
            return;
        }
        final Context c = this;
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Logging in...", "Please wait...", true);
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                        User user;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                if(dsp != null) {
                                    user = dsp.getValue(User.class);
                                    if(user != null) {
                                        if(user.getEmail().equals(email.getText().toString())) {
                                            databaseReference.removeEventListener(this);
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("email", email.getText().toString());
                                            editor.putString("password", password.getText().toString());
                                            editor.putString("username", user.getUsername());
                                            editor.putString("type",user.getType());
                                            editor.commit();
                                            Intent i = new Intent(login.this,MainActivity.class);
                                            i.putExtra("usernamefromlogin",user.getUsername());
                                            i.putExtra("usertype",user.getType());
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    } else {
                    Log.d("Error: ",task.getException().getMessage());
                    progressDialog.dismiss();
                    runOnUiThread(() -> Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}

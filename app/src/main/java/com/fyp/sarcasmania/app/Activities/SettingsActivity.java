package com.fyp.sarcasmania.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.sarcasmania.app.Classes.Post;
import com.fyp.sarcasmania.app.Classes.User;
import com.fyp.sarcasmania.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    EditText username;
    TextView usernamedisplay;
    EditText fullname;
    EditText password;
    EditText email;
    DatabaseReference databaseReference;
    String userFromMain;
    TextView numberofposts;
    TextView average;
    FirebaseAuth firebaseAuth;
    String currpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_settings);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.actionBarColor)));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        username = findViewById(R.id.username);
        username.setEnabled(false);
        username.setInputType(InputType.TYPE_NULL);
        username.setFocusable(false);

        userFromMain = getIntent().getExtras().getString("usernamefrommain");

        usernamedisplay = findViewById(R.id.settingsusername);
        usernamedisplay.setText(userFromMain);

        username.setText(userFromMain);

        fullname = findViewById(R.id.name);

        password = findViewById(R.id.password);

        email = findViewById(R.id.email);
        email.setEnabled(false);
        email.setInputType(InputType.TYPE_NULL);
        email.setFocusable(false);

        average = findViewById(R.id.average);

        firebaseAuth = FirebaseAuth.getInstance();

        numberofposts = findViewById(R.id.postnumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        user = dsp.getValue(User.class);
                        if(user != null) {
                            if(user.getUsername().equals(userFromMain)) {
                                fullname.setText(user.getFullname());
                                password.setText(user.getPassword());
                                email.setText(user.getEmail());
                          }
                      }
                    }
                }
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            Post post;
            int number = 0;
            int averagee = 0;
            float temp = 0;
            float temp2 = 0;
            ArrayList<Float> sarcasmRating = new ArrayList<Float>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        post = dsp.getValue(Post.class);
                        if(post != null) {
                            if(post.getUsername().equals(userFromMain)) {
                                number++;
                                temp  = post.getSarcasm()/20;
                                temp2 = (float)(Math.round(temp*100.0)/100.0);
                                sarcasmRating.add(temp2);
                            }
                        }
                    }
                }
                if(sarcasmRating.size() > 0) {
                    for (int i = 0; i < sarcasmRating.size(); i++) {
                        averagee += sarcasmRating.get(i);
                    }
                    averagee = averagee / sarcasmRating.size();
                }
                numberofposts.setText(Integer.toString(number));
                average.setText(Integer.toString(averagee));
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        currpassword = password.getText().toString();
    }

    public void onUpdateClick (View view) {
        if(fullname.getText().toString().length() < 1) {
            fullname.setError("Fullname cannot be Empty");
            return;
        }
        if(password.getText().toString().length() < 1) {
            password.setError("Password cannot be Empty");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currpassword);
        final Context c = this;

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Updating...", "Please wait...", true);
        user.updatePassword(password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("Users").child(userFromMain).child("password").setValue(password.getText().toString());
                            databaseReference.child("Users").child(userFromMain).child("fullname").setValue(fullname.getText().toString());
                            progressDialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(c, "Something went wrong please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
//        user.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {

//                                    } else {
//                                        Toast.makeText(c,"Something went wrong please try again!",Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(c,"User Authentication failed!",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

    }
}

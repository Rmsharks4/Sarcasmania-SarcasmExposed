package com.fyp.sarcasmania.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.sarcasmania.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class splashSreen extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    ImageView imageView1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent i = new Intent(getApplicationContext(),signup.class);
        startActivity(i);
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_splash_sreen);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.hide();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

//        FirebaseHelper firebaseHelper = new FirebaseHelper();
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat mdformat = new SimpleDateFormat("EEEE h:mm a");
//        String dateAndTime = mdformat.format(calendar.getTime());
//        firebaseHelper.newPost(3,"This is the most sarcastic tweet ever.", "mahnoorkhan",100,0,0,dateAndTime);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        imageView1 = findViewById(R.id.imageView1);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        imageView1.startAnimation(animation1); //Set animation to your ImageView

        final Context c = this;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        Thread myThread = new Thread() {
            @Override
            public void run() {
            try {
                final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String email = (mSharedPreference.getString("email",null));
                String password = (mSharedPreference.getString("password",null));
                String username = (mSharedPreference.getString("username",null));
                String type = (mSharedPreference.getString("type",null));
                sleep(5000);
                if(email != null && password != null && username != null && type != null) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Intent i = new Intent(c,MainActivity.class);
                                    i.putExtra("usernamefromlogin",username);
                                    i.putExtra("usertype",type);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Log.d("Error: ",task.getException().getMessage());
                                    runOnUiThread(() -> Toast.makeText(c, task.getException().getMessage(), Toast.LENGTH_LONG).show());
                                }
                            }
                        });

//                        Intent i = new Intent(c,MainActivity.class);
//                        i.putExtra("usernamefromlogin",username);
//                        i.putExtra("usertype",type);
//                        startActivity(i);
//                        finish();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), signup.class);
                    startActivity(i);
                }
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            }
        };
        myThread.start();
    }
}



package com.fyp.sarcasmania.app.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fyp.sarcasmania.app.Adapter.CardAdapter;
import com.fyp.sarcasmania.app.Adapter.MainActivityPagerAdapter;
import com.fyp.sarcasmania.app.Adapter.profileAdapter;
import com.fyp.sarcasmania.app.Classes.FirebaseHelper;
import com.fyp.sarcasmania.app.Classes.Post;
import com.fyp.sarcasmania.app.Classes.humorFeedback;
import com.fyp.sarcasmania.app.Classes.insultFeedback;
import com.fyp.sarcasmania.app.Classes.report;
import com.fyp.sarcasmania.app.Fragment.NewfeedFragment;
import com.fyp.sarcasmania.app.Fragment.PostFragment;
import com.fyp.sarcasmania.app.Fragment.ProfileFragment;
import com.fyp.sarcasmania.app.Fragment.reportedtweets;
import com.fyp.sarcasmania.app.R;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huxq17.swipecardsview.SwipeCardsView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

public class MainActivity extends AppCompatActivity implements PostFragment.postInterface, NewfeedFragment.newsFeedInterface, ProfileFragment.OnFragmentInteractionListener, reportedtweets.OnFragmentInteractionListener, NewfeedFragment.OnFragmentInteractionListener, PostFragment.OnFragmentInteractionListener, ProfileFragment.profileInterface {

    private TabLayout tabLayout;
    private com.fyp.sarcasmania.app.Classes.swipeOffViewPager swipeOffViewPager;
    private ArrayList<TabLayout.Tab> tabIdHistory = new ArrayList<TabLayout.Tab>();
    private TabLayout.Tab home;
    private TabLayout.Tab upload;
    private TabLayout.Tab profile;
    private android.support.v7.app.ActionBar actionBar;
    private SwipeCardsView swipeCardsView;
    private List<Post> modelList;
    private ArrayList<Post> modelList2;
    private ArrayList<report> modelList3;
    private Boolean check;
    private String usernameFromLogin;
    private TextView postUsername;
    private TextView profileUsername;
    private DatabaseReference databaseReference;
    private FirebaseHelper firebaseHelper;
    private TextView sarcasmScale;
    private int num;
    private Button createPost;
    private EditText newPost;
    private ImageView humorous;
    private ImageView insulting;
    private TabLayout.Tab reportedtweets;
    private String usertype;

    Bitmap laugh;
    Bitmap insultb;
    Bitmap laughgs;
    Bitmap insultgs;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_main);
        setTitle("NewsFeed");

        MobileAds.initialize(this, "ca-app-pub-1500982172589230~9004985923");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1500982172589230/8916042931");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                int interval = 60000; // 300 Seconds = 5 minutes
                Handler handler = new Handler();
                Runnable runnable = new Runnable(){
                    public void run() {
                        mInterstitialAd.show();
                    }
                };
                handler.postAtTime(runnable, System.currentTimeMillis()+interval);
                handler.postDelayed(runnable, interval);
            }

            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }
        });

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.actionBarColor)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        home = tabLayout.newTab().setIcon(R.mipmap.home_purple);
        upload = tabLayout.newTab().setIcon(R.mipmap.newpost_grey);
        profile = tabLayout.newTab().setIcon(R.mipmap.profile_grey);
        reportedtweets = tabLayout.newTab().setText("RT");

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.hintcolor));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        usertype = getIntent().getExtras().getString("usertype");

        tabLayout.addTab(home);
        tabLayout.addTab(upload);
        tabLayout.addTab(profile);

        if(usertype.length() > 0 && usertype.equals("admin")) {
            tabLayout.addTab(reportedtweets);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabIdHistory.add(home);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        swipeOffViewPager = (com.fyp.sarcasmania.app.Classes.swipeOffViewPager) findViewById(R.id.viewPager);

        MainActivityPagerAdapter mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        swipeOffViewPager.setAdapter(mainActivityPagerAdapter);
        swipeOffViewPager.setPagingEnabled(false); // here i am **

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        swipeOffViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                swipeOffViewPager.setCurrentItem(tab.getPosition());
                switch(tab.getPosition())
                {
                    case 0:
                        home.setIcon(R.mipmap.home_purple);
                        upload.setIcon(R.mipmap.newpost_grey);
                        profile.setIcon(R.mipmap.profile_grey);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("NewsFeed");
                        num = 1;
                        break;
                    case 1:
                        home.setIcon(R.mipmap.home_grey);
                        upload.setIcon(R.mipmap.newpost_purple);
                        profile.setIcon(R.mipmap.profile_grey);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("New Post");
                        break;
                    case 2:
                        home.setIcon(R.mipmap.home_grey);
                        upload.setIcon(R.mipmap.newpost_grey);
                        profile.setIcon(R.mipmap.profile_purple);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("Profile");
                        break;
                    case 3:
                        home.setIcon(R.mipmap.home_grey);
                        upload.setIcon(R.mipmap.newpost_grey);
                        profile.setIcon(R.mipmap.profile_grey);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("Reported Tweets");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseHelper = new FirebaseHelper();
        usernameFromLogin = getIntent().getExtras().getString("usernamefromlogin");

    }

    @Override
    public void onBackPressed() {
        if (tabIdHistory.size() > 0)
        {
            tabIdHistory.remove(tabIdHistory.size() - 1);
            if(tabIdHistory.size() > 0)
            {
                tabIdHistory.get(tabIdHistory.size()-1).select();
            }
            else
            {
                FragmentManager mgr = getSupportFragmentManager();
                if (mgr.getBackStackEntryCount() == 0) {
                    super.onBackPressed();
                } else {
                    mgr.popBackStack();
                }
            }
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void setRT() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        modelList3 = new ArrayList<report>();
        databaseReference.child("ReportedTweets").addValueEventListener(new ValueEventListener() {
            report report;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        report = dsp.getValue(report.class);
                        if(report != null) {
                            modelList3.add(0, report);
                        }
                    }
                }
                profileAdapter profileAdapter = new profileAdapter(MainActivity.this,2,modelList3);
                recyclerView.setAdapter(profileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setNewsFeed() {
        final Context c = this;
        //------------------------------- get swipe cards view----------------------------------------
        swipeCardsView = (SwipeCardsView)findViewById(R.id.swipeCardsView);
        swipeCardsView.retainLastCard(true);
        swipeCardsView.enableSwipe(true);
        check = false;
        modelList = new ArrayList<Post>();
        if(isNetworkAvaliable(this)) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading Newsfeed...", "Please wait...", true);
            databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
                Post post;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp != null) {
                            post = dsp.getValue(Post.class);
                            if (post != null) {
                                if(!post.getUsername().equals(usernameFromLogin)) {
                                    modelList.add(0, post);
                                }
                            }
                        }
                    }
                    Collections.shuffle(modelList);
                    progressDialog.dismiss();
                    CardAdapter cardAdapter = new CardAdapter(modelList, getBaseContext(), getIntent(), swipeCardsView, getLayoutInflater(), MainActivity.this);
                    swipeCardsView.setAdapter(cardAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Network not available. Turn on WIFI/4G/3G", Toast.LENGTH_LONG).show());
        }
    }

    //------------------------options menu for settings bruh-------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent;
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("usernamefrommain",usernameFromLogin);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.logout) {
            Toast.makeText(this,"Logout",Toast.LENGTH_LONG).show();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", null);
            editor.putString("password", null);
            editor.putString("username", null);
            editor.putString("type",null);
            editor.commit();
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(),signup.class);
            startActivity(i);
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setProfile() {
        profileUsername = (TextView) findViewById(R.id.profileUsername);
        profileUsername.setText(usernameFromLogin);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        modelList2 = new ArrayList<Post>();
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            Post post;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        post = dsp.getValue(Post.class);
                        if(post != null) {
                            if(post.getUsername().equals(usernameFromLogin)) {
                                modelList2.add(0, post);
                            }
                        }
                    }
                }
                profileAdapter profileAdapter = new profileAdapter(MainActivity.this,modelList2,1);
                recyclerView.setAdapter(profileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setPost() {
        postUsername = (TextView) findViewById(R.id.postUsername);
        createPost = (Button) findViewById(R.id.post_submit);
        newPost = (EditText) findViewById(R.id.post_text);
        postUsername.setText(usernameFromLogin);

        final Context c = this;

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(c, "Calculating Scores...", "Please wait...", true);
                String text = newPost.getText().toString();
                if(text.length() <= 0 || text.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(c,"You don't seem to be Sarcastic AT ALL! Type Something",Toast.LENGTH_LONG).show();
                }
                if(text.split("\\s+").length < 4) {
                    progressDialog.dismiss();
                    Toast.makeText(c,"Sentence should have atleast 4 words! :)",Toast.LENGTH_LONG).show();
                }
                else {
                    RequestQueue sarcasm_insult = Volley.newRequestQueue(c);
                    String url1 = "https://sarcasmania-api.herokuapp.com/api/sarcasmania?text=" + text;
                    JsonObjectRequest sarcasmAndInsult = new JsonObjectRequest(url1, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    int sarcasm = jsonObject.optInt("Sarcasm");
                                    if (sarcasm > 50) {
                                        int insult = jsonObject.optInt("Insult");

                                        if (insult >= 51 && insult <= 100) {
                                            insult = 1;
                                        } else if (insult >= 0 && insult <= 50) {
                                            insult = 0;
                                        }
                                        final int finalInsult = insult;

                                        Random random = new Random();
                                        int humor = random.nextInt(2);
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat mdformat = new SimpleDateFormat("EEEE h:mm a");
                                        String dateAndTime = mdformat.format(calendar.getTime());
                                        databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                                            Post post;
                                            int count = 0;

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                                    if (dsp != null) {
                                                        post = dsp.getValue(Post.class);
                                                        if (post != null) {
                                                            count = post.getTweetID();
                                                        }
                                                    }
                                                }
                                                firebaseHelper.newPost(count + 1, text, usernameFromLogin, sarcasm, humor, finalInsult, dateAndTime);
                                                progressDialog.dismiss();

                                                final Dialog dialog = new Dialog(c);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.setContentView(R.layout.profilepostinfo);
                                                TextView timee = (TextView) dialog.findViewById(R.id.timesss);
                                                TextView posts = (TextView) dialog.findViewById(R.id.textt);
                                                RatingBar sarcasmRating = (RatingBar) dialog.findViewById(R.id.ratingBar2);

                                                timee.setText(dateAndTime);
                                                posts.setText(text);
                                                float postSarcasmRating = sarcasm / 20;
                                                sarcasmRating.setRating((float) (Math.round(postSarcasmRating * 100.0) / 100.0));

                                                int humorValue = humor;
                                                int insultValue = finalInsult;

                                                ImageView humor = (ImageView) dialog.findViewById(R.id.imageView4);
                                                ImageView insult = (ImageView) dialog.findViewById(R.id.imageView3);

                                                Bitmap cross = BitmapFactory.decodeResource(getResources(), R.drawable.redcross);
                                                Bitmap tick = BitmapFactory.decodeResource(getResources(), R.drawable.yellowcheck);

                                                Bitmap heartGrey = cross;
                                                Bitmap heartPurple = tick;

                                                Bitmap unheartGrey = cross;
                                                Bitmap unheartPurple = tick;

                                                if (humorValue == 0) {
                                                    humor.setImageBitmap(heartGrey);
                                                }
                                                if (humorValue == 1) {
                                                    humor.setImageBitmap(heartPurple);
                                                }
                                                if (insultValue == 0) {
                                                    insult.setImageBitmap(unheartGrey);
                                                }
                                                if (insultValue == 1) {
                                                    insult.setImageBitmap(unheartPurple);
                                                }

                                                dialog.setCancelable(true);
                                                dialog.show();


                                                Toast.makeText(c, "Posted Successfully!", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(c, "Sorry something went wrong! Please try again some other time.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            Toast.makeText(c,"Sorry something went wrong! Please try again some other time.",Toast.LENGTH_LONG).show();
                        }});
                    sarcasm_insult.add(sarcasmAndInsult);
                }
            }
        });

    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}

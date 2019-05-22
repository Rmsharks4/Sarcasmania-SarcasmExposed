package com.fyp.sarcasmania.app.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.sarcasmania.app.Activities.MainActivity;
import com.fyp.sarcasmania.app.Classes.FirebaseHelper;
import com.fyp.sarcasmania.app.Classes.Post;
import com.fyp.sarcasmania.app.Classes.humorFeedback;
import com.fyp.sarcasmania.app.Classes.insultFeedback;
import com.fyp.sarcasmania.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.List;

public class CardAdapter extends BaseCardAdapter {

    private List<Post> modelList;
    private Context context;
    private ImageView humorous;
    private ImageView insulting;
    private DatabaseReference databaseReference;
    private int num;
    private String usernameFromLogin;
    private FirebaseHelper firebaseHelper;
    private Intent intent;
    private SwipeCardsView swipecardsview;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public CardAdapter(List<Post> modelList, Context context, Intent intent, SwipeCardsView swipeCardsView, LayoutInflater layoutInflater, Activity activity) {
        this.modelList = modelList;
        this.context = context;
        num = 1;
        this.intent = intent;
        this.swipecardsview = swipeCardsView;
        this.layoutInflater = layoutInflater;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.card_item;
    }

    private void showFullyCustomToast()
    {
        Toast toast = new Toast(activity);
        View toastView = layoutInflater.inflate(R.layout.custom_toast, null);
        toast.setView(toastView);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,22);
        toast.show();
        toast.setDuration(Toast.LENGTH_SHORT);
    }


    @Override
    public void onBindData(int position, View cardview) {
        if (modelList == null || modelList.size() == 0) {
            return;
        }
        //-------------------- username or tweet set ho rahi hai bruh-------------------
        cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.newReport(modelList.get(position).getTweetID(),modelList.get(position).getTweet(),modelList.get(position).getUsername());
                Toast.makeText(context,"Tweet Reported",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        Post post = modelList.get(position);

        Bitmap cross = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcross);
        Bitmap tick = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowcheck);

        TextView tweet = (TextView) cardview.findViewById(R.id.theTweet);
        TextView userName = (TextView) cardview.findViewById(R.id.theUsername);
        TextView tweetid = (TextView) cardview.findViewById(R.id.tweetid);
        RatingBar sarcasmRating = (RatingBar) cardview.findViewById(R.id.ratingBar);
        ImageView humor = (ImageView) cardview.findViewById(R.id.imageView4);
        ImageView insult = (ImageView) cardview.findViewById(R.id.imageView3);
        TextView timeStamp = (TextView) cardview.findViewById(R.id.time);

        TextView t1 = (TextView) cardview.findViewById(R.id.textView3);
        TextView t2 = (TextView) cardview.findViewById(R.id.textView2);

        ImageView humorous = (ImageView) cardview.findViewById(R.id.imageView7);
        ImageView insulting = (ImageView) cardview.findViewById(R.id.imageView6);

        Bitmap laugh = BitmapFactory.decodeResource(context.getResources(), R.drawable.laugh);
        Bitmap insultb = BitmapFactory.decodeResource(context.getResources(), R.drawable.angry);
        Bitmap laughgs = BitmapFactory.decodeResource(context.getResources(), R.drawable.laughgs);
        Bitmap insultgs = BitmapFactory.decodeResource(context.getResources(), R.drawable.angrygs);


        int humorValue = post.getHumor();
        int insultValue = post.getInsult();

        //Glide.with(context).load(R.mipmap.insult).into(insult);
        //Glide.with(context).load(R.mipmap.haha).into(humor);

        if(humorValue == 0) {
//            humor.setImageBitmap(heartGrey);
            humor.setImageBitmap(cross);
        }
        if(humorValue == 1) {
//            humor.setImageBitmap(heartPurple);
            humor.setImageBitmap(tick);
        }
        if(insultValue == 0) {
//            insult.setImageBitmap(unheartGrey);
            insult.setImageBitmap(cross);
        }
        if(insultValue == 1) {
//            insult.setImageBitmap(unheartPurple);
            insult.setImageBitmap(tick);
        }

        tweetid.setText(Integer.toString(post.getTweetID()));
        tweet.setText(post.getTweet());
        userName.setText(post.getUsername());
        timeStamp.setText(post.getTime());
        float postSarcasmRating = post.getSarcasm()/20;
        sarcasmRating.setRating((float)(Math.round(postSarcasmRating*100.0)/100.0));

        if (num == 1) {
            humorous.setImageBitmap(laughgs);
            humorous.setClickable(true);

            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseHelper = new FirebaseHelper();
            usernameFromLogin = intent.getExtras().getString("usernamefromlogin");

            databaseReference.child("HumorFeedback").addValueEventListener(new ValueEventListener() {
                com.fyp.sarcasmania.app.Classes.humorFeedback humorFeedback;
                int humor;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp != null) {
                            humorFeedback = dsp.getValue(humorFeedback.class);
                            if(humorFeedback != null) {
                                if(humorFeedback.getTweetid() == modelList.get(0).getTweetID() && humorFeedback.getUsername().equals(usernameFromLogin)) {
                                    humor = humorFeedback.getHumor();
                                    Bitmap heartGrey = laughgs;
                                    Bitmap heartPurple = laugh;
                                    if (humor == 0) {
                                        humorous.setImageBitmap(heartGrey);
                                    }
                                    else if (humor == 1) {
                                        humorous.setImageBitmap(heartPurple);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            humorous.setOnClickListener(v -> {

                final Bitmap bitmap = ((BitmapDrawable) humorous.getDrawable()).getBitmap();
                Bitmap heartGrey = laughgs;
                Bitmap heartPurple = laugh;

                if (bitmap.sameAs(heartPurple)) {
                    humorous.setImageBitmap(heartGrey);
                    firebaseHelper.humorFeed(0, usernameFromLogin, modelList.get(0).getTweetID());
                    showFullyCustomToast();
                }
                if (bitmap.sameAs(heartGrey)) {
                    humorous.setImageBitmap(heartPurple);
                    firebaseHelper.humorFeed(1, usernameFromLogin, modelList.get(0).getTweetID());
                    showFullyCustomToast();
                }
            });

            //------------------------ marking insulting ------------------------------------------

            insulting.setImageBitmap(insultgs);
            insulting.setClickable(true);

            databaseReference.child("InsultFeedback").addValueEventListener(new ValueEventListener() {
                com.fyp.sarcasmania.app.Classes.insultFeedback insultFeedback;
                int insult;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp != null) {
                            insultFeedback = dsp.getValue(insultFeedback.class);
                            if(insultFeedback != null) {
                                if(insultFeedback.getTweetid() == modelList.get(0).getTweetID() && insultFeedback.getUsername().equals(usernameFromLogin)) {
                                    insult = insultFeedback.getInsult();
                                    Bitmap unheartGrey = insultgs;
                                    Bitmap unheartPurple = insultb;
                                    if (insult == 0) {
                                        insulting.setImageBitmap(unheartGrey);
                                    }
                                    else if (insult == 1) {
                                        insulting.setImageBitmap(unheartPurple);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            insulting.setOnClickListener(v -> {

                final Bitmap bitmap2 = ((BitmapDrawable) insulting.getDrawable()).getBitmap();
                Bitmap unheartGrey = insultgs;
                Bitmap unheartPurple = insultb;

                if (bitmap2.sameAs(unheartPurple)) {
                    insulting.setImageBitmap(unheartGrey);
                    firebaseHelper.insultFeed(0, usernameFromLogin, modelList.get(0).getTweetID());
                    showFullyCustomToast();
                }
                if (bitmap2.sameAs(unheartGrey)) {
                    insulting.setImageBitmap(unheartPurple);
                    firebaseHelper.insultFeed(1, usernameFromLogin, modelList.get(0).getTweetID());
                    showFullyCustomToast();
                }
            });
        }

        swipecardsview.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {

                num++;

                //------------------------ marking humorous ------------------------------------------
                humorous.setImageBitmap(laughgs);
                humorous.setClickable(true);

                databaseReference.child("HumorFeedback").addValueEventListener(new ValueEventListener() {
                    humorFeedback humorFeedback;
                    int humor;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            if (dsp != null) {
                                humorFeedback = dsp.getValue(humorFeedback.class);
                                if(humorFeedback != null) {
                                    if(humorFeedback.getTweetid() == modelList.get(index).getTweetID() && humorFeedback.getUsername().equals(usernameFromLogin)) {
                                        humor = humorFeedback.getHumor();
                                        Bitmap heartGrey = laughgs;
                                        Bitmap heartPurple = laugh;
                                        if (humor == 0) {
                                            humorous.setImageBitmap(heartGrey);
                                        }
                                        else if (humor == 1) {
                                            humorous.setImageBitmap(heartPurple);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                humorous.setOnClickListener(v -> {

                    final Bitmap bitmap = ((BitmapDrawable) humorous.getDrawable()).getBitmap();
                    Bitmap heartGrey = laughgs;
                    Bitmap heartPurple = laugh;

                    if (bitmap.sameAs(heartPurple)) {
                        humorous.setImageBitmap(heartGrey);
                        firebaseHelper.humorFeed(0, usernameFromLogin, modelList.get(index).getTweetID());
                        showFullyCustomToast();
                    }
                    if (bitmap.sameAs(heartGrey)) {
                        humorous.setImageBitmap(heartPurple);
                        firebaseHelper.humorFeed(1, usernameFromLogin, modelList.get(index).getTweetID());
                        showFullyCustomToast();
                    }
                });

                //------------------------ marking insulting ------------------------------------------
                insulting.setImageBitmap(insultgs);
                insulting.setClickable(true);

                databaseReference.child("InsultFeedback").addValueEventListener(new ValueEventListener() {
                    insultFeedback insultFeedback;
                    int insult;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            if (dsp != null) {
                                insultFeedback = dsp.getValue(insultFeedback.class);
                                if(insultFeedback != null) {
                                    if(insultFeedback.getTweetid() == modelList.get(index).getTweetID() && insultFeedback.getUsername().equals(usernameFromLogin)) {
                                        insult = insultFeedback.getInsult();
                                        Bitmap unheartGrey = insultgs;
                                        Bitmap unheartPurple = insultb;
                                        if (insult == 0) {
                                            insulting.setImageBitmap(unheartGrey);
                                        }
                                        else if (insult == 1) {
                                            insulting.setImageBitmap(unheartPurple);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                insulting.setOnClickListener(v -> {

                    final Bitmap bitmap2 = ((BitmapDrawable) insulting.getDrawable()).getBitmap();
                    Bitmap unheartGrey = insultgs;
                    Bitmap unheartPurple = insultb;

                    if (bitmap2.sameAs(unheartPurple)) {
                        insulting.setImageBitmap(unheartGrey);
                        firebaseHelper.insultFeed(0, usernameFromLogin, modelList.get(index).getTweetID());
                        showFullyCustomToast();
                    }
                    if (bitmap2.sameAs(unheartGrey)) {
                        insulting.setImageBitmap(unheartPurple);
                        firebaseHelper.insultFeed(1, usernameFromLogin, modelList.get(index).getTweetID());
                        showFullyCustomToast();
                    }
                });

            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
            }

            @Override
            public void onItemClick(View cardImageView, int index) {

            }
        });
    }

}

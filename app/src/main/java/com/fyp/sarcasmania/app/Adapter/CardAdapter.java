package com.fyp.sarcasmania.app.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fyp.sarcasmania.app.Classes.FirebaseHelper;
import com.fyp.sarcasmania.app.Classes.Post;
import com.fyp.sarcasmania.app.R;
import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.List;

public class CardAdapter extends BaseCardAdapter {

    private List<Post> modelList;
    private Context context;

    public CardAdapter(List<Post> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.card_item;
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

        TextView tweet = (TextView) cardview.findViewById(R.id.theTweet);
        TextView userName = (TextView) cardview.findViewById(R.id.theUsername);
        TextView tweetid = (TextView) cardview.findViewById(R.id.tweetid);
        RatingBar sarcasmRating = (RatingBar) cardview.findViewById(R.id.ratingBar);
        ImageView humor = (ImageView) cardview.findViewById(R.id.imageView4);
        ImageView insult = (ImageView) cardview.findViewById(R.id.imageView3);
        TextView timeStamp = (TextView) cardview.findViewById(R.id.time);

        TextView t1 = (TextView) cardview.findViewById(R.id.textView3);
        TextView t2 = (TextView) cardview.findViewById(R.id.textView2);



        Drawable drawable = context.getDrawable(R.mipmap.heart_grey);
        Bitmap heartGrey = ((BitmapDrawable) drawable).getBitmap();
        Drawable drawable2 = context.getDrawable(R.mipmap.heart_purple);
        Bitmap heartPurple = ((BitmapDrawable) drawable2).getBitmap();

        Drawable drawable1 = context.getDrawable(R.mipmap.unheart_grey);
        Bitmap unheartGrey = ((BitmapDrawable) drawable1).getBitmap();
        Drawable drawable3 = context.getDrawable(R.mipmap.unheart_purple);
        Bitmap unheartPurple = ((BitmapDrawable) drawable3).getBitmap();

        int humorValue = post.getHumor();
        int insultValue = post.getInsult();

        Glide.with(context).load(R.mipmap.insult).into(insult);
        Glide.with(context).load(R.mipmap.haha).into(humor);

        if(humorValue == 0) {
//            humor.setImageBitmap(heartGrey);
            t1.setText("Humor: 0");
        }
        if(humorValue == 1) {
//            humor.setImageBitmap(heartPurple);
            t1.setText("Humor: 1");
        }
        if(insultValue == 0) {
//            insult.setImageBitmap(unheartGrey);
            t2.setText("Insult: 0");
        }
        if(insultValue == 1) {
//            insult.setImageBitmap(unheartPurple);
            t2.setText("Insult: 1");
        }

        tweetid.setText(Integer.toString(post.getTweetID()));
        tweet.setText(post.getTweet());
        userName.setText(post.getUsername());
        timeStamp.setText(post.getTime());
        float postSarcasmRating = post.getSarcasm()/20;
        sarcasmRating.setRating((float)(Math.round(postSarcasmRating*100.0)/100.0));
    }
}

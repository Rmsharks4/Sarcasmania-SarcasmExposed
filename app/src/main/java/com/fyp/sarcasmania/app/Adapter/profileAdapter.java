package com.fyp.sarcasmania.app.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fyp.sarcasmania.app.Classes.Post;
import com.fyp.sarcasmania.app.Classes.report;
import com.fyp.sarcasmania.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> List;
    private ArrayList<report> List2;
    private int check;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        ImageView haha;
        ImageView insult;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.timess);
        }
    }

    public profileAdapter(Context context,  int check, ArrayList<report> list) {
        this.context = context;
        List2 = list;
        this.check = check;
    }

    public profileAdapter(Context context, ArrayList<Post> list, int check) {
        this.context = context;
        this.List = list;
        this.check = check;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profileitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Posts").child(Integer.toString(List2.get(i).getTweetid())).removeValue();
                databaseReference.child("ReportedTweets").child(Integer.toString(List2.get(i).getTweetid())).removeValue();
                notifyDataSetChanged();
                int currPosition = List2.indexOf(List2.get(i));
                List2.remove(currPosition);
                notifyItemRemoved(currPosition);
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Message");
                alertDialog.setMessage("Tweet Deleted Successfully");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView tweet = viewHolder.textView;
        TextView times = viewHolder.textView2;

        if(check == 2) {
            report report = List2.get(i);
            times.setText(Integer.toString(report.getTweetid()));
            tweet.setText(report.getUsername() + ": " + report.getTweet());

        }

        if(check == 1) {
            Post post = List.get(i);
            tweet.setText(post.getTweet());
            times.setText(post.getTime());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            tweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
                        Post postt;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                if (dsp != null) {
                                    postt = dsp.getValue(Post.class);
                                    if (postt != null && postt.getTweet().equals(post.getTweet())) {
                                        final Dialog dialog = new Dialog(context);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.profilepostinfo);
                                        TextView timee = dialog.findViewById(R.id.timesss);
                                        TextView posts = dialog.findViewById(R.id.textt);
                                        RatingBar sarcasmRating = dialog.findViewById(R.id.ratingBar2);

                                        timee.setText(post.getTime());
                                        posts.setText(post.getTweet());
                                        float postSarcasmRating = postt.getSarcasm() / 20;
                                        sarcasmRating.setRating((float) (Math.round(postSarcasmRating * 100.0) / 100.0));

                                        int humorValue = postt.getHumor();
                                        int insultValue = postt.getInsult();

                                        ImageView humor = dialog.findViewById(R.id.imageView8);
                                        ImageView insult = dialog.findViewById(R.id.imageView5);

                                        Drawable drawable = context.getDrawable(R.mipmap.heart_grey);
                                        Bitmap heartGrey = ((BitmapDrawable) drawable).getBitmap();
                                        Drawable drawable2 = context.getDrawable(R.mipmap.heart_purple);
                                        Bitmap heartPurple = ((BitmapDrawable) drawable2).getBitmap();

                                        Drawable drawable1 = context.getDrawable(R.mipmap.unheart_grey);
                                        Bitmap unheartGrey = ((BitmapDrawable) drawable1).getBitmap();
                                        Drawable drawable3 = context.getDrawable(R.mipmap.unheart_purple);
                                        Bitmap unheartPurple = ((BitmapDrawable) drawable3).getBitmap();

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
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(check == 1) {
            return List.size();
        }
        if(check == 2){
            return List2.size();
        }
        else
            return 0;
    }

}

//            haha = (ImageView) itemView.findViewById(R.id.imageView3);
//            insult = (ImageView) itemView.findViewById(R.id.imageView4);

//        ImageView insult = viewHolder.insult;
//        ImageView haha = viewHolder.haha;

//        Glide.with(context).load(R.mipmap.insult).into(insult);
//        Glide.with(context).load(R.mipmap.haha).into(haha);
//        haha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "HUMOROUS", Toast.LENGTH_LONG).show();
//
//            }
//        });
//        insult.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "INSULTING", Toast.LENGTH_LONG).show();
//
//            }
//        });

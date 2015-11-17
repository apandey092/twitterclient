package com.codepath.apps.twitterclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activity.ComposeActivity;
import com.codepath.apps.twitterclient.activity.ProfileActivity;
import com.codepath.apps.twitterclient.activity.RetweetActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.util.RelativeTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apandey on 11/3/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder{
        TextView body;
        TextView userProfile;
        TextView createdAt;
        ImageView ivProfileView;
        ImageView ivReplyView;
        ImageView ivRetweet;
        ImageView ivFavorite;
        TextView tvFavorite;
        TextView tvRetweet;
    }
    public TweetsArrayAdapter(Context context, ArrayList<Tweet> objects) {
        super(context, R.layout.item_tweet, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweetResult = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.body = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.userProfile = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.ivProfileView = (ImageView)convertView.findViewById(R.id.ivProfileView);
            viewHolder.createdAt = (TextView)convertView.findViewById(R.id.tvTime);
            viewHolder.ivReplyView = (ImageView)convertView.findViewById(R.id.ivReply);
            viewHolder.ivRetweet = (ImageView)convertView.findViewById(R.id.ivRetweet);
            viewHolder.ivFavorite = (ImageView)convertView.findViewById(R.id.ivFav);
            viewHolder.tvFavorite = (TextView)convertView.findViewById(R.id.tvFav);
            viewHolder.tvRetweet = (TextView)convertView.findViewById(R.id.tvRetweet);
            viewHolder.ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), RetweetActivity.class);
                    i.putExtra("tweet", tweetResult);
                    v.getContext().startActivity(i);
                }
            });
            viewHolder.ivProfileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ProfileActivity.class);
                    i.putExtra("user", tweetResult.getUser());
                    v.getContext().startActivity(i);
                }
            });

            viewHolder.ivReplyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ComposeActivity.class);
                    i.putExtra("tweet", tweetResult);
                    v.getContext().startActivity(i);
                }
            });

            convertView.setTag(viewHolder);

        }{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.body.setText(Html.fromHtml(tweetResult.getBody()));
        viewHolder.userProfile.setText("@" + tweetResult.getUser().getScreenName());
        viewHolder.createdAt.setText(RelativeTime.getRelativeTimeAgo(tweetResult.getCreatedAt()));
        viewHolder.ivProfileView.setImageResource(0);
        viewHolder.ivReplyView.setImageResource(R.drawable.reply);
        if(tweetResult.getIsFavorite() != null && tweetResult.getIsFavorite()){
            viewHolder.ivFavorite.setImageResource(R.drawable.fav_red);
        }
        viewHolder.tvFavorite.setText(String.valueOf(tweetResult.getFavoriteCount()));
        viewHolder.tvRetweet.setText(String.valueOf(tweetResult.getRetweetCount()));

        Picasso.with(getContext()).load(tweetResult.getUser().getProfileImageUri()).into(viewHolder.ivProfileView);
//        Picasso.with(getContext()).load(tweetResult.getUser().getProfileImageUri()).into(viewHolder.ivReplyView);
        return convertView;
    }


}

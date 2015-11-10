package com.codepath.apps.twitterclient.adapter;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by apandey on 11/3/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder{
        TextView body;
        TextView userProfile;
        TextView createdAt;
        ImageView ivProfileView;
    }
    public TweetsArrayAdapter(Context context, ArrayList<Tweet> objects) {
        super(context,R.layout.item_tweet, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        View Holder Pattern

        Tweet tweetResult = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.body = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.userProfile = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.ivProfileView = (ImageView)convertView.findViewById(R.id.ivProfileView);
            viewHolder.createdAt = (TextView)convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);

        }{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.body.setText(Html.fromHtml(tweetResult.getBody()));
        viewHolder.userProfile.setText("@"+tweetResult.getUser().getScreenName());
        viewHolder.createdAt.setText(getRelativeTimeAgo(tweetResult.getCreatedAt()));
        viewHolder.ivProfileView.setImageResource(0);
        Picasso.with(getContext()).load(tweetResult.getUser().getProfileImageUri()).into(viewHolder.ivProfileView);
        return convertView;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


}

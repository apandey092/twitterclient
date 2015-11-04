package com.codepath.apps.twitterclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apandey on 11/3/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder{
        TextView body;
        TextView userProfile;
        ImageView ivProfileView;
    }
    public TweetsArrayAdapter(Context context, ArrayList<Tweet> objects) {
        super(context,android.R.layout.simple_list_item_1, objects);
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
            convertView.setTag(viewHolder);

        }{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.body.setText(Html.fromHtml(tweetResult.getBody()));
        viewHolder.userProfile.setText(tweetResult.getUser().getScreenName());
        viewHolder.ivProfileView.setImageResource(0);
        Picasso.with(getContext()).load(tweetResult.getUser().getProfileImageUri()).into(viewHolder.ivProfileView);
        return convertView;
    }
}

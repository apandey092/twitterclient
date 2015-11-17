package com.codepath.apps.twitterclient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.twitter.TwitterApplication;
import com.codepath.apps.twitterclient.twitter.TwitterClient;
import com.codepath.apps.twitterclient.util.RelativeTime;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class TweetActivity extends AppCompatActivity {

    private Tweet tweet;
    private TextView tvBody;
    private TextView tvTimestamp;
    private TextView tvUserName;
    private TextView tvFav;
    private TextView tvRetweet;

    private ImageView ivUserProfile;
    private ImageView ivRetweet;
    private ImageView ivReplyView;
    private ImageView ivFav;
    private TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        client = TwitterApplication.getRestClient();
        setupViews();
    }

    private void setupViews() {
        tweet = getIntent().getParcelableExtra("tweet");
        ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimestamp = (TextView) findViewById(R.id.tvCreatedAt);
        tvUserName = (TextView) findViewById(R.id.tvScreenName);
        tvFav = (TextView) findViewById(R.id.tvFav);
        tvRetweet = (TextView) findViewById(R.id.tvRetweet);
        if (tweet.getUser().getProfileImageUri() != null) {
            Picasso.with(this).load(tweet.getUser().getProfileImageUri()).into(ivUserProfile);
        }
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(RelativeTime.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvUserName.setText("@" + tweet.getUser().getScreenName());

        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        ivReplyView = (ImageView) findViewById(R.id.ivReply);
        ivFav = (ImageView) findViewById(R.id.ivFav);

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RetweetActivity.class);
                i.putExtra("tweet", tweet);
                v.getContext().startActivity(i);
            }
        });
        ivReplyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
                i.putExtra("tweet", tweet);
                v.getContext().startActivity(i);
            }
        });
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    client.postFavorite(new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivFav.setImageResource(R.drawable.fav_red);
                        }
                    }, String.valueOf(tweet.getUid()));
            }
        });

        if(tweet.getIsFavorite() != null && tweet.getIsFavorite()) {
            ivFav.setBackground(new ColorDrawable(Color.RED));
        }
        tvFav.setText(String.valueOf(tweet.getFavoriteCount()));
        tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

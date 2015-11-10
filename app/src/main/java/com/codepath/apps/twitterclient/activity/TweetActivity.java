package com.codepath.apps.twitterclient.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.util.RelativeTime;
import com.squareup.picasso.Picasso;

public class TweetActivity extends AppCompatActivity {

    private Tweet tweet;
    private TextView tvBody;
    private TextView tvTimestamp;
    private TextView tvUserName;

    private ImageView ivUserProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        setupViews();
    }

    private void setupViews() {
        tweet = getIntent().getParcelableExtra("tweet");
        ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimestamp = (TextView) findViewById(R.id.tvCreatedAt);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        if (tweet.getUser().getProfileImageUri() != null) {
            Picasso.with(this).load(tweet.getUser().getProfileImageUri()).into(ivUserProfile);
        }
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(RelativeTime.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvUserName.setText("@" + tweet.getUser().getScreenName());

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

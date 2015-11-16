package com.codepath.apps.twitterclient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.twitter.TwitterApplication;
import com.codepath.apps.twitterclient.twitter.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends AppCompatActivity {
    private ImageView ivProfileImage;
    private TextView tvUserName;

    private TwitterClient client;
    private Button btnTweet;
    private EditText evText;
    private TextView tvChars;
    private int charCount;
    private String userReplyId;
    private Tweet tweet;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        tweet = (Tweet) getIntent().getParcelableExtra("tweet");
//        Log.d("DEBUG TWEET", tweet.toString());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_compose_tweet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        setUpViews();
    }

    private void setUpViews() {

        ivProfileImage = (ImageView) findViewById(R.id.ivUserProfileView);
        evText = (EditText) findViewById(R.id.evTweet);
        if (tweet == null || tweet.getUser() == null) {
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);

                }
            });
        }else{
            user = tweet.getUser();
            userReplyId = String.valueOf(tweet.getUid());
            evText.setText("@"+user.getScreenName());
        }
//        Log.d("User ", user.toString());
        tvUserName = (TextView) findViewById(R.id.tvUserN);
        tvUserName.setText("@" + user.getScreenName());
        Picasso.with(getApplicationContext()).load(user.getProfileImageUri()).into(ivProfileImage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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

    public void onSave(View v) {
        Intent i = new Intent();
        EditText editTweet = (EditText) findViewById(R.id.evTweet);
        String twString = editTweet.getText().toString();
//        i.putExtra("tweet", tweet);
        String userReplyId = String.valueOf(tweet.getUid());
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        }, twString, userReplyId);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.miActionButton);
        // Retrieve the action-view from menu
        View v = MenuItemCompat.getActionView(actionViewItem);
        // Find the button within action-view
        btnTweet = (Button) v.findViewById(R.id.btnTweet);
        tvChars = (TextView) v.findViewById(R.id.tvChars);
        btnTweet.setBackground(new ColorDrawable(Color.parseColor("#4099FF")));
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave(v);
            }
        });
        evText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 140) {
                    btnTweet.setEnabled(false);
                    btnTweet.setClickable(false);
                    evText.setTextColor(Color.RED);

                }
                charCount = 140 - evText.getText().length();
                tvChars.setText("" + charCount);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Handle button click here
        return super.onPrepareOptionsMenu(menu);
    }
}

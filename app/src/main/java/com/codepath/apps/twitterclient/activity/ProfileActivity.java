package com.codepath.apps.twitterclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.twitter.TwitterApplication;
import com.codepath.apps.twitterclient.twitter.TwitterClient;
import com.codepath.apps.twitterclient.fragments.UserTimeLineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        user = (User)getIntent().getParcelableExtra("user");
        if(user != null){
            getSupportActionBar().setTitle("@"+user.getScreenName());
            populateUserHeader(user);
        }else {
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateUserHeader(user);
                }
            });
        }
//        String screenName = getIntent().getStringExtra("screen_name");
        if(savedInstanceState == null) {
            UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(user.getScreenName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimeLineFragment);
            ft.commit();
        }

    }

    private void populateUserHeader(User user) {
        TextView tvName = (TextView)findViewById(R.id.tvScreenName);
        TextView tvTagLine = (TextView)findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollwers);
        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfile);
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingsCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUri()).into(ivProfileImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

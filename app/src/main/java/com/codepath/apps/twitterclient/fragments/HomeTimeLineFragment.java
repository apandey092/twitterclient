package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.twitter.TwitterApplication;
import com.codepath.apps.twitterclient.twitter.TwitterClient;
import com.codepath.apps.twitterclient.activity.TweetActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.util.EndlessScrollListener;
import com.codepath.apps.twitterclient.util.Network;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by apandey on 11/12/15.
 */
public class HomeTimeLineFragment extends TweetsListFragment {
    private TwitterClient client;
    private String lastId;
    private SwipeRefreshLayout swipeContainer;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
//        populateTimeline(false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(false);
                return false;
            }
        });


        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent tweetIntent = new Intent(getActivity(), TweetActivity.class);
                Tweet tweet = (Tweet) parent.getItemAtPosition(position);
                tweetIntent.putExtra("tweet", tweet);
                startActivity(tweetIntent);
            }
        });

        setUpSwipe(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateTimeline(false);
    }

    private void populateTimeline(final boolean refresh) {
        if (!Network.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Internet not available", Toast.LENGTH_SHORT).show();
            aTweets.addAll(Tweet.getAll());
            swipeContainer.setRefreshing(false);
            return;
        }
        showProgressBar();
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (refresh) {
                    getTweetAdapter().clear();
                }
                addAll(Tweet.fromJsonArray(response));
                getTweetAdapter().notifyDataSetChanged();
                Tweet tweet = getTweets().get(getTweets().size() - 1);
                lastId = "" + tweet.getUid();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject err) {
                if (err != null) {
                    Log.d("ERROR", err.toString());
                }
                addAll(Tweet.getAll());
                swipeContainer.setRefreshing(false);

            }
        }, lastId);
        hideProgressBar();
    }

    private void setUpSwipe(View v) {
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void refresh(){
        getTweetAdapter().clear();
//        populateTimeline(true);
        swipeContainer.setRefreshing(false);
    }
}

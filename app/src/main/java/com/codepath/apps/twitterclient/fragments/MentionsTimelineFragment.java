package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.twitter.TwitterApplication;
import com.codepath.apps.twitterclient.twitter.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.util.EndlessScrollListener;
import com.codepath.apps.twitterclient.util.Network;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by apandey on 11/13/15.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
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
        setUpSwipe(v);
        return v;
    }

    public void populateTimeline(final boolean refresh) {
        if (!Network.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Internet not available", Toast.LENGTH_SHORT).show();
            aTweets.addAll(Tweet.getAll());
            swipeContainer.setRefreshing(false);
            return;
        }
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                if (refresh) {
                    getTweetAdapter().clear();
                }
                addAll(Tweet.fromJsonArray(response));
                getTweetAdapter().notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject err) {
                if (err != null) {
                    Log.d("ERROR", err.toString());
                }
                getTweetAdapter().clear();
                addAll(Tweet.getAll());
                swipeContainer.setRefreshing(false);
            }
        });
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


}

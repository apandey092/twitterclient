package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapter.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apandey on 11/12/15.
 */
public abstract class TweetsListFragment extends Fragment {
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    public ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;
    // Store reference to the progress bar later
    protected ProgressBar progressBarFooter;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
//        View footer = inflater.inflate(
//                R.layout.action_view_progress, container, false);
//        // Find the progressbar within footer
//        progressBarFooter = (ProgressBar)
//                footer.findViewById(R.id.pbFooterLoading);
//        // Add footer to ListView before setting adapter
//        lvTweets.addFooterView(footer);
        lvTweets.setAdapter(aTweets);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        super.onCreate(savedInstanceState);
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }
    public TweetsArrayAdapter getTweetAdapter(){
        return aTweets;
    }

    public ArrayList<Tweet> getTweets(){
        return tweets;
    }
    public ListView getLvTweets(){
        return lvTweets;
    }

    public void refresh(){

    }
    public void showProgressBar() {
        // Show progress item
        progressBarFooter.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        // Hide progress item
        progressBarFooter.setVisibility(View.INVISIBLE);
    }



}

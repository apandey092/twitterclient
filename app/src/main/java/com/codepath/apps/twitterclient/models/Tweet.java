package com.codepath.apps.twitterclient.models;

/**
 * Created by apandey on 11/3/15.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * [
 * {
 * "coordinates": null,
 * "truncated": false,
 * "created_at": "Tue Aug 28 21:16:23 +0000 2012",
 * "favorited": false,
 * "id_str": "240558470661799936",
 * "in_reply_to_user_id_str": null,
 * "entities": {
 * "urls": [
 * <p/>
 * ],
 * "hashtags": [
 * <p/>
 * ],
 * "user_mentions": [
 * <p/>
 * ]
 * },
 * "text": "just another test",
 * "contributors": null,
 * "id": 240558470661799936,
 * "retweet_count": 0,
 * "in_reply_to_status_id_str": null,
 * "geo": null,
 * "retweeted": false,
 * "in_reply_to_user_id": null,
 * "place": null,
 * "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
 * "user": {
 * "name": "OAuth Dancer",
 * "profile_sidebar_fill_color": "DDEEF6",
 * "profile_background_tile": true,
 * "profile_sidebar_border_color": "C0DEED",
 * "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 * "created_at": "Wed Mar 03 19:37:35 +0000 2010",
 * "location": "San Francisco, CA",
 * "follow_request_sent": false,
 * "id_str": "119476949",
 * "is_translator": false,
 * "profile_link_color": "0084B4",
 * "entities": {
 * "url": {
 * "urls": [
 * {
 * "expanded_url": null,
 * "url": "http://bit.ly/oauth-dancer",
 * "indices": [
 * 0,
 * 26
 * ],
 * "display_url": null
 * }
 * ]
 * },
 * "description": null
 * },
 */
// Parse the json,
public class Tweet {
    private String body;
    private long uid;
    private User user;

    private String createdAt;

//    Deserialize json

    public User getUser() {
        return user;
    }


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet fromJson(JSONObject json){
        Tweet tweet = new Tweet();
        try {
            tweet.body = (String)json.getString("text");
            tweet.uid =(Long)json.getLong("id");
            tweet.createdAt = (String)json.getString("created_at");
            tweet.user = User.fromJson((json.getJSONObject("user")));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

   public static  ArrayList<Tweet> fromJsonArray(JSONArray jsonArray){
       ArrayList<Tweet> tweetList = new ArrayList<>();
       for(int i=0; i<jsonArray.length(); i++){
           try {
               tweetList.add(fromJson(jsonArray.getJSONObject(i)));
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }
       return tweetList;
   }

}

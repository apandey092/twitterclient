package com.codepath.apps.twitterclient.models;

/**
 * Created by apandey on 11/3/15.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {
    @Column(name = "body")
    private String body;
    @Column(name = "tweet_id")
    private long uid;
    @Column(name = "user")
    private User user;

    @Column(name = "favorite_active")
    private Boolean isFavorite;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "created_at", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String createdAt;

    @Column(name = "retweet_count")
    private int retweetCount;

    //    Deserialize json
    public Tweet() {
        super();
    }

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

    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = (String) json.getString("text");
            tweet.uid = (Long) json.getLong("id");
            tweet.createdAt = (String) json.getString("created_at");
            tweet.user = User.fromJson((json.getJSONObject("user")));
            tweet.user = User.fromJson((json.getJSONObject("user")));
            tweet.isFavorite = json.getBoolean("favorited");
            if(json.has("favorite_count")) {
                tweet.favoriteCount = json.getInt("favorite_count");

            }
            if(json.has("retweet_count")) {
                tweet.retweetCount = json.getInt("retweet_count");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweetList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Tweet tweet = fromJson(jsonArray.getJSONObject(i));
                tweetList.add(tweet);
                tweet.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return tweetList;
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .orderBy("created_at DESC")
                .execute();
    }


    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeParcelable(this.user, 0);
        dest.writeValue(this.isFavorite);
        dest.writeInt(this.favoriteCount);
        dest.writeString(this.createdAt);
        dest.writeInt(this.retweetCount);
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.isFavorite = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.favoriteCount = in.readInt();
        this.createdAt = in.readString();
        this.retweetCount = in.readInt();
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    public int getRetweetCount() {
        return retweetCount;
    }
}

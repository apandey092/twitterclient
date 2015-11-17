package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apandey on 11/3/15.
 */

/**
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
 */
@Table(name="users")
public class User extends Model implements Parcelable {
    @Column(name="name")
    private String name;
    @Column(name = "user_id")
    private long uid;
    @Column(name = "screenName")
    private String screenName;
    @Column(name="profileImageUri")
    private String profileImageUri;
    @Column(name = "tagline")
    private String tagline;
    @Column(name= "followers_count")
    private int followersCount;
    @Column(name = "followings_count")
    private int followingsCount;


    public User(){
        super();
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public static User fromJson(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id_str");
            u.screenName = json.getString("screen_name");
            u.profileImageUri = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followersCount= json.getInt("followers_count");
            u.followingsCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        u.save();
        return u;


    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", uid=" + uid +
                ", screenName='" + screenName + '\'' +
                ", profileImageUri='" + profileImageUri + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUri);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageUri = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }
}

package com.cse110.data;

import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class FeedItem {
    private int id, favorites, comments;
    private String name, status, image, profilePic, major, timeStamp, url;
    private ParseObject userLikeActivity, post;
    private ParseUser user;
    private boolean userLiked;

    public FeedItem() {
    }

    public FeedItem(int id, String name, String image, String status,
                    String profilePic, String timeStamp, String url) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;
        this.major = null;
        this.favorites = 0;
        this.comments = 0;
        this.userLikeActivity = null;
        this.post = null;
        this.user = null;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFavorites() {
        return this.favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getComments() {
        return this.comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public boolean userLiked() {
        return this.userLikeActivity != null;
    }

    public void setUserLikeActivity(ParseObject userLikeActivity) {
        this.userLikeActivity = userLikeActivity;
    }

    public void removeUserLikeActivity() {
        if (this.userLikeActivity != null) {
            try {
                this.userLikeActivity.delete();
            } catch (ParseException e) {
                return;
            }

            this.userLikeActivity = null;
        }
    }

    public ParseObject getPost() {
        return this.post;
    }

    public void setPost(ParseObject post) {
        this.post = post;
    }

    public ParseUser getUser() {
        return this.user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }
}

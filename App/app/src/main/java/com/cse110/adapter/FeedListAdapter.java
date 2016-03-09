package com.cse110.adapter;

import com.cse110.app.R;
import com.cse110.FeedImageView;
import com.cse110.app.MyApplication;
import com.cse110.data.FeedItem;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView major = (TextView) convertView.findViewById(R.id.major);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);

        final FeedItem item = feedItems.get(position);

        final Button favoriteButton = (Button) convertView.findViewById(R.id.favoriteButton);

        name.setText(item.getName());

        if (item.getMajor() != null) {
            major.setVisibility(View.VISIBLE);
            major.setText(item.getMajor());
        }
        else
            major.setVisibility(View.GONE);

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }

        // user profile pic
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Favorite button
        if (item.userLiked()) {
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_18dp, 0, 0, 0);
        }
        else {
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_18dp, 0, 0, 0);
        }

        favoriteButton.setText(Integer.toString(item.getFavorites()));
        favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (item.userLiked()) {
                    item.removeUserLikeActivity();
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_18dp, 0, 0, 0);
                    item.setFavorites(item.getFavorites() - 1);
                    favoriteButton.setText(Integer.toString(item.getFavorites()));
                } else {
                    final ParseObject activity = new ParseObject("Activity");
                    activity.put("fromUser", ParseUser.getCurrentUser());
                    activity.put("toUser", item.getUser());
                    activity.put("post", item.getPost());
                    activity.put("type", "favorite");
                    try {
                        activity.save();
                    } catch (ParseException e) {
                        return;
                    }
                    item.setUserLikeActivity(activity);
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_18dp, 0, 0, 0);
                    item.setFavorites(item.getFavorites() + 1);
                    favoriteButton.setText(Integer.toString(item.getFavorites()));
                }
            }
        });

        // Feed image
        if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

}
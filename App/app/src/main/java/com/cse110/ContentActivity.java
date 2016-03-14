package com.cse110;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.cse110.adapter.FeedListAdapter;
import com.cse110.app.R;
import com.cse110.data.FeedItem;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public abstract class ContentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int POSTS_PER_QUERY = 10;

    public ListView listView;
    protected FeedListAdapter listAdapter;
    protected List<FeedItem> feedItems;
    public SwipeRefreshLayout swipeLayout;
    protected EndlessScrollListener scrollListener;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void buildActivityFeed() {
        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        ParseObject school = ParseUser.getCurrentUser().getParseObject("school");

        school.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    setTitle(object.getString("name"));
                } else {
                    setTitle("Hi, " + ParseUser.getCurrentUser().getString("name") + "!");
                }
            }
        });

        scrollListener = new EndlessScrollListener(this, 10);
        listView.setOnScrollListener(scrollListener);

        addMorePosts(0, POSTS_PER_QUERY);
    }

    protected void reloadPosts() {
        feedItems.clear();
        listAdapter.notifyDataSetChanged();
        scrollListener.reset();
        addMorePosts(0, POSTS_PER_QUERY);
    }

    public abstract void addMorePosts(final int offset, final int limit);

    protected void fetchPosts(final int offset,  ParseQuery<ParseObject> query) {
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "Error: Failed to retrieve posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }


                for (int i = 0; i < list.size(); i++) {
                    ParseObject post = list.get(i);
                    FeedItem item = new FeedItem();
                    item.setId(offset + i + 1);
                    ParseUser user = post.getParseUser("user");
                    if (user == null) {
                        continue;
                    }
                    item.setUser(user);
                    item.setPost(post);
                    item.setName(user.getString("name"));
                    item.setImge(null);
                    item.setStatus(post.getString("content"));

                    ParseQuery<ParseObject> queryActivities = ParseQuery.getQuery("Activity");
                    queryActivities.whereEqualTo("post", post);
                    queryActivities.include("fromUser");
                    List<ParseObject> activities;
                    try {
                        activities = queryActivities.find();
                    } catch (ParseException e2) {
                        activities = new ArrayList<>();
                        Toast.makeText(getApplicationContext(), "Error: Failed to get likes for post:" + e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    int favorites = 0;
                    int comments = 0;
                    for (int j = 0; j < activities.size(); j++) {
                        ParseObject activity = activities.get(j);
                        String type = activity.getString("type");
                        if (type.equals("favorite")) {
                            String fromUser = activity.getParseUser("fromUser").getUsername();
                            String currentUser = ParseUser.getCurrentUser().getUsername();
                            if (currentUser.equals(fromUser)) {
                                item.removeUserLikeActivity();
                                item.setUserLikeActivity(activity);
                            }
                            favorites++;
                        }
                        else {
                            comments++;
                        }
                    }

                    item.setFavorites(favorites);
                    item.setComments(comments);

                    String major = user.getString("major");

                    if (major != null && major.length() > 0)
                        item.setMajor(major);

                    String profilePicUrl = user.getParseFile("profilePicture").getUrl();
                    item.setProfilePic(profilePicUrl);
                    item.setTimeStamp(String.valueOf(post.getCreatedAt().getTime()));
                    item.setUrl(null);

                    feedItems.add(item);
                }

                listAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * It must be overriden by parent classes if manual swipe is enabled.
     */
    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
        reloadPosts();
        swipeLayout.setRefreshing(false);
    }
}

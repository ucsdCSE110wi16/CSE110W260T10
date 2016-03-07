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
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    public SwipeRefreshLayout swipeLayout;
    private EndlessScrollListener scrollListener;

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
        List <ParseObject> list;

        try {
            list = query.find();
        }
        catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Error: Failed to retrieve posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            ParseObject post = list.get(i);
            FeedItem item = new FeedItem();
            item.setId(offset + i + 1);
            ParseUser user = (ParseUser) post.getParseObject("user");
            item.setName(user.get("name").toString());
            item.setImge(null);
            item.setStatus(post.get("content").toString());

            String profilePicUrl = user.getParseFile("profilePicture").getUrl();
            item.setProfilePic(profilePicUrl);
            item.setTimeStamp(String.valueOf(post.getCreatedAt().getTime()));
            item.setUrl(null);

            feedItems.add(item);
        }
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

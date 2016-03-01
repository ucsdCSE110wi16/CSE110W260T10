package com.cse110;

import com.cse110.adapter.FeedListAdapter;
import com.cse110.app.AddPostActivity;
import com.cse110.app.DispatchActivity;
import com.cse110.app.MyApplication;
import com.cse110.app.R;
import com.cse110.data.FeedItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int postsPerQuery = 10;

    public ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    public SwipeRefreshLayout swipeLayout;
    private EndlessScrollListener scrollListener;

    private Toolbar toolbar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        addMorePosts(0, 10);


//        // We first check for cached request
//        Cache cache = MyApplication.getInstance().getRequestQueue().getCache();
//        Entry entry = cache.get(URL_FEED);
//        if (entry != null) {
//            // fetch the data from cache
//            try {
//                String data = new String(entry.data, "UTF-8");
//                try {
//                    parseJsonFeed(new JSONObject(data));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            // making fresh volley request and getting json
//            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
//                    URL_FEED, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    VolleyLog.d(TAG, "Response: " + response.toString());
//                    if (response != null) {
//                        parseJsonFeed(response);
//                    }
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                }
//            });
//
//            // Adding request to volley request queue
//            MyApplication.getInstance().addToRequestQueue(jsonReq);
//        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addMorePosts(final int offset, final int limit) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        query.whereEqualTo("school", ParseUser.getCurrentUser().get("school"));
        query.orderByDescending("createdAt");
        query.setSkip(offset);
        query.include("user");
        query.setLimit(limit);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject post = list.get(i);
                        FeedItem item = new FeedItem();
                        item.setId(offset + i + 1);
                        ParseUser user = (ParseUser) post.getParseObject("user");
                        item.setName(user.get("name").toString());
                        item.setImge(null);
                        item.setStatus(post.get("content").toString());
                        item.setProfilePic("http://api.androidhive.info/feed/img/nat.jpg");
                        item.setTimeStamp(String.valueOf(post.getCreatedAt().getTime()));
                        item.setUrl(null);

                        feedItems.add(item);
                    }
                    listAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: Failed to retrieve posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(this, DispatchActivity.class));
            finish();
            return true;
        }
        else if (id == R.id.action_create_post) {
            startActivity(new Intent(this, AddPostActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * It must be overriden by parent classes if manual swipe is enabled.
     */
    @Override public void onRefresh() {
        Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
        feedItems.clear();
        listAdapter.notifyDataSetChanged();
        scrollListener.reset();
        addMorePosts(0, 10);
        swipeLayout.setRefreshing(false);
    }

}
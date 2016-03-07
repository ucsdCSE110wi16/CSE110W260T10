package com.cse110;

import com.cse110.adapter.FeedListAdapter;
import com.cse110.app.AddPostActivity;
import com.cse110.app.DispatchActivity;
import com.cse110.app.R;
import com.cse110.app.UserProfileActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends ContentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    static final int POST_REQUEST = 0;
    static final int PROFILE_REQUEST = 1;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildActivityFeed();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void addMorePosts(final int offset, final int limit) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        query.whereEqualTo("school", ParseUser.getCurrentUser().get("school"));
        query.orderByDescending("createdAt");
        query.setSkip(offset);
        query.include("user");
        query.setLimit(limit);
        fetchPosts(offset, query);
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
            startActivityForResult(new Intent(this, AddPostActivity.class), POST_REQUEST);
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivityForResult(new Intent(this, UserProfileActivity.class), PROFILE_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == POST_REQUEST || requestCode == PROFILE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                reloadPosts();
            }
        }
    }

}
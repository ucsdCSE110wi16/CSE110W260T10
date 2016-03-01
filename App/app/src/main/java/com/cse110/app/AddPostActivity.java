package com.cse110.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class AddPostActivity extends AppCompatActivity {

    private EditText postEntry;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postEntry = (EditText) findViewById(R.id.postEntry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_post) {
            String content = postEntry.getText().toString();
            if (content.length() == 0) {
                Toast.makeText(getApplicationContext(), "Nothing?", Toast.LENGTH_SHORT).show();
            }
            else {
                ParseObject post = new ParseObject("Post");
                post.put("content", content);
                post.put("user", ParseUser.getCurrentUser());
                post.put("school", ParseUser.getCurrentUser().get("school"));
                try {
                    post.save();
                }
                catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Error: Failed to make post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                setResult(Activity.RESULT_OK, new Intent());
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

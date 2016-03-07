package com.cse110.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


public class UserProfileActivity extends AppCompatActivity {
    static final int REQUEST_PHOTO_FROM_GALLERY = 0;

    private Toolbar toolbar;
    private ParseImageView profilePictureView;
    private TextView tvDisplayName;
    private TextView tvMajor;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePictureView = (ParseImageView) findViewById(R.id.profilePic);
        displayProfilePicture();

        tvDisplayName = (TextView) findViewById(R.id.displayName);
        displayDisplayName();

        tvMajor = (TextView) findViewById(R.id.major);
        displayMajor();

        createMajorAlert();
        createDisplayNameAlert();

    }

    public void createMajorAlert() {
        final AlertDialog.Builder majorAlertBuilder = new AlertDialog.Builder(this);
        majorAlertBuilder.setMessage("Type in major");

        final EditText input = new EditText(this);
        majorAlertBuilder.setView(input);

        //Set positive button
        majorAlertBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();
                ParseUser.getCurrentUser().put("major", txt);
                ParseUser.getCurrentUser().saveInBackground();
                displayMajor();

            }

        });

        //Set negative button
        majorAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        final AlertDialog majorAlertDialog = majorAlertBuilder.create();

        //set listener for the EditText major box.
        tvMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                majorAlertDialog.show();
            }
        });
    }

    public void createDisplayNameAlert() {
        final AlertDialog.Builder displayNameBuilder = new AlertDialog.Builder(this);
        displayNameBuilder.setMessage("Type in full name");

        final EditText input = new EditText(this);
        displayNameBuilder.setView(input);

        //Set positive button
        displayNameBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();
                ParseUser.getCurrentUser().put("name", txt);
                ParseUser.getCurrentUser().saveInBackground();
                displayDisplayName();

            }

        });

        //Set negative button
        displayNameBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        final AlertDialog displayNameAlertDialog = displayNameBuilder.create();

        //set listener for the EditText major box.
        tvDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNameAlertDialog.show();
            }
        });
    }

    public void displayProfilePicture() {
        ParseFile parseFile = ParseUser.getCurrentUser().getParseFile("profilePicture");
        profilePictureView.setParseFile(parseFile);
        profilePictureView.loadInBackground();
    }

    public void displayDisplayName() {
        String displayName = ParseUser.getCurrentUser().getString("name");
        tvDisplayName.setText(displayName);
    }

    public void displayMajor() {
        String major = ParseUser.getCurrentUser().getString("major");
        if (major != null && major.length() > 0) {
            tvMajor.setText(major);
        }
        else {
            tvMajor.setText("Click here to select major.");
        }
    }

    public void dispatchPhotoGallaryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_PHOTO_FROM_GALLERY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_profile_pic) {
            dispatchPhotoGallaryIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_PHOTO_FROM_GALLERY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null,
                        null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                final ParseFile file = new ParseFile("profilePicture.png", image);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            currentUser.put("profilePicture", file);
                            currentUser.saveInBackground();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
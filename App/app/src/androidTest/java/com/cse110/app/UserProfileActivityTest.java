package com.cse110.app;

/**
 * Created by Kevin on 3/8/16.
 */

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.app.Activity;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.cse110.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Kevin on 3/8/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileActivityTest extends ActivityInstrumentationTestCase2<DispatchActivity> {

    private static final String usernametest = "kevinwong";
    private static final String passwordtest = usernametest;

    @Rule
    public ActivityTestRule<UserProfileActivity> mActivityRule = new ActivityTestRule<>(UserProfileActivity.class);

    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void TestLogin () {
        onView(ViewMatchers.withId(R.id.login_username_input)).perform(ViewActions.typeText(usernametest));
        onView(ViewMatchers.withId(R.id.login_password_input)).perform(ViewActions.typeText(passwordtest));
        onView(ViewMatchers.withId(R.id.parse_login_button)).perform(ViewActions.click());

    }


}

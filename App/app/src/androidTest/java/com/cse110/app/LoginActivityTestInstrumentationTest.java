package com.cse110.app;

import com.cse110.app.DispatchActivity;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.test.ViewAsserts.assertOnScreen;
import static java.util.regex.Pattern.matches;

import org.junit.Rule;
import org.junit.runner.RunWith;

import org.junit.Test;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTestInstrumentationTest extends ActivityInstrumentationTestCase2<DispatchActivity> {

    private String mStringToBeTyped = "Espresso";

    private com.cse110.app.Login Login;
    private EditText password;

    private static final String usernametest = "kevinwong";
    private static final String passwordtest = usernametest;

    @Rule
    public ActivityTestRule<DispatchActivity> mActivityRule = new ActivityTestRule<>(DispatchActivity.class);

    public LoginActivityTestInstrumentationTest() {
        super(DispatchActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void Testing () {

    }
}

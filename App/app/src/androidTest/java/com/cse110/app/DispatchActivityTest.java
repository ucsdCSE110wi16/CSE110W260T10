package com.cse110.app;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Kevin on 3/8/16.
 */
public class DispatchActivityTest extends ActivityInstrumentationTestCase2<DispatchActivity> {

    private static final String usernametest = "kevinwong";
    private static final String passwordtest = usernametest;

    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(Login.class);

    public LoginPageTest() {
        super(Login.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void TestLogin () {
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(ViewActions.typeText(usernametest));
        Espresso.onView(ViewMatchers.withId(R.id.etPassword)).perform(ViewActions.typeText(passwordtest));
        Espresso.onView(ViewMatchers.withId(R.id.bLogin)).perform(ViewActions.click());

    }
}

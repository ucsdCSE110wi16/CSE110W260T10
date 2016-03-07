package com.cse110.app;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
//import android.support.test.espresso.ViewInteraction;


@RunWith(AndroidJUnit4.class)
public class DispatchTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<DispatchActivity> mActivityRule = new ActivityTestRule<>(
            DispatchActivity.class);

// make the tester tyoe in and input a valid string
    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

// After inputting a valid string, test it and try to login
    @Test
    public void testSignUpThenLogin() {
        // Type text and then press the button.
        Espresso.onView(ViewMatchers.withId(R.id.editTextUserInput))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.changeTextBt)).perform(click());

        // Check that the text was changed.
        Espresso.onView(ViewMatchers.withId(R.id.textToBeChanged))
                .check(matches(withText(mStringToBetyped)));
    }
}

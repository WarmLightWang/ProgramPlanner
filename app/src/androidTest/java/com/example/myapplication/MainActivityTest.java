package com.example.myapplication;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.SignUpActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the main activity. Will ensure login and signup
 * activities are launched when the respective buttons are pressed.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * As far as I could tell, this is currently
     * the proper way to set up intents.
     * It looks like previous versions required rules
     * and such, but those are now deprecated.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Clicks the sign up button and checks that
     * SignUpActivity is launched.
     */
    @Test
    public void testStartSignUpOnClick() {
        Espresso.onView(ViewMatchers.withId(R.id.imageButtonSignUp)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(SignUpActivity.class.getName()));
    }

    /**
     * Clicks the login button and checks that
     * LoginActivity is launched.
     */
    @Test
    public void testStartLoginOnClick() {
        Espresso.onView(ViewMatchers.withId(R.id.imageButtonLogIn)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(LoginActivity.class.getName()));
    }

    /**
     * See setup comment. There may be a rule that does this, but this is what I found.
     */
    @After
    public void tearDown() {
        Intents.release();
    }
}

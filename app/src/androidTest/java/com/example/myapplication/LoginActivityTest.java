package com.example.myapplication;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.activity.ForgetPassActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.ProjectMainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the login activity.
 * Will check that the proper error
 * message is displayed on incorrect username and password.
 * Also checks activity change on proper button presses.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Setup for Espresso intents.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Test attempting to log in with an invalid password.
     * Verify that the proper error message is set.
     */
    @Test
    public void testLoginInvalidPass() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextAccountName)).perform(ViewActions.typeText("InvalidName"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("testPass"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogIn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errormessage)).check(ViewAssertions.matches(ViewMatchers.withText("Either Username or Password is Incorrect")));
    }

    /**
     * Test a successful login attempt.
     * Make sure the proper activity is launched.
     */
    @Test
    public void testLoginSuccess() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextAccountName)).perform(ViewActions.typeText("UITester"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("password"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogIn)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ProjectMainActivity.class.getName()));
    }

    /**
     * Test the forget password path and check for intents
     */
    @Test
    public void testForgetPass() {
        Espresso.onView(ViewMatchers.withId(R.id.forgotPassword)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ForgetPassActivity.class.getName()));
    }

    /**
     * Teardown intents
     */
    @After
    public void tearDown() {
        Intents.release();
    }
}

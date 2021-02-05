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
import com.example.myapplication.activity.ForgetQuesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the forgetPass activity.
 * Will check that the proper error
 * message is displayed on incorrect username.
 * Also checks activity change on proper button presses.
 */
@RunWith(AndroidJUnit4.class)
public class ForgetPassTest {
    @Rule
    public ActivityScenarioRule<ForgetPassActivity> activityRule =
            new ActivityScenarioRule<>(ForgetPassActivity.class);

    /**
     * Set up for intents.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Test invalid account name
     * Ensure error view is properly set
     */
    @Test
    public void testInvalidName() {
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.typeText("NoNamer"));
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        //The test was sometimes running faster than the error view was being set, this wait,
        //though inelegant, should be enough to allow the app to check with the database and
        //update errView.
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println("An error occurred in ForgetPass Invalid name test. Test Failed.");
        }
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("The username does not exist")));
    }

    /**
     * Test a valid account name and ensure
     * correct intent
     */
    @Test
    public void testValidName() {
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.typeText("UITester"));
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println("An error occurred in ForgetPass Valid name test. Test Failed.");
        }
        Intents.intended(IntentMatchers.hasComponent(ForgetQuesActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}

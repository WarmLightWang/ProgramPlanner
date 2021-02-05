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
import com.example.myapplication.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Tests the ResetPassActivity class.
 * Checks invalid fields and activity change on
 * both button presses
 */
@RunWith(AndroidJUnit4.class)
public class ResetPassActivityTest {
    /**
     * Tests will start from forgetPassActivity to
     * ensure we can match info to the proper test account.
     */
    @Rule
    public ActivityScenarioRule<ForgetPassActivity> activityRule =
            new ActivityScenarioRule<>(ForgetPassActivity.class);

    /**
     * Set up the intents and navigate to the
     * activity under test with correct info
     */
    @Before
    public void setUp() {

        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.typeText("UITester"));
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println("An error occurred in ResetPass setup. Test Failed.");
        }
        Espresso.onView(ViewMatchers.withId(R.id.questionAnswer)).perform(ViewActions.typeText("Espresso"));
        Espresso.onView(ViewMatchers.withId(R.id.questionAnswer)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        Intents.init();
    }

    /**
     * Tests clicking confirm with no info
     * Ensures error is properly set.
     */
    @Test
    public void testEmptyFieldReset() {
        Espresso.onView(ViewMatchers.withId(R.id.ConfirmButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Password Length must at least 6 characters")));
    }

    /**
     * Test non matching passwords.
     * Ensures proper error is set
     */
    @Test
    public void testNewPassMismatch() {
        Espresso.onView(ViewMatchers.withId(R.id.newPassword)).perform(ViewActions.typeText("TheseDo"));
        Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.typeText("NotMatch"));
        Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.ConfirmButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Password do not match")));

    }

    /**
     * Test cancel button.
     * Ensures activity change
     */
    @Test
    public void testCancel() {
        Espresso.onView(ViewMatchers.withId(R.id.CancelButton)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    /**
     * Test confirming with the same password so as not to break the tests.
     * As its a valid password, should take us back to the main activity.
     */
    @Test
    public void testConfirm() {
        Espresso.onView(ViewMatchers.withId(R.id.newPassword)).perform(ViewActions.typeText("password"));
        Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.typeText("password"));
        Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.ConfirmButton)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    /**
     * Releases intents.
     */
    @After
    public void tearDown() {
        Intents.release();
    }
}

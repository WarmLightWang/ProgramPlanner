package com.example.myapplication;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.SignUpActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the SignUpActivity.
 * Ensures proper errors are set when invalid,
 * and checks return to Main on success
 * <p>
 * Name prefixed with A to make sure these tests run first.
 * For whatever reason, running after ResetPassActivityTest
 * caused issues with the intent back to main from here
 * and instead returned to ResetPassActivity. Error
 * could not be replicated in full app, so I'm guessing
 * its some weird error with the testing environment.
 */
@RunWith(AndroidJUnit4.class)
public class ASignUpActivityTest {

    @Rule
    public ActivityScenarioRule<SignUpActivity> activityRule =
            new ActivityScenarioRule<>(SignUpActivity.class);

    /**
     * Using what is believed to currently be the proper
     * way to initialize intents. A rule may exist that is better,
     * but the only one I could find was deprecated.
     */
    @Before
    public void setUp() {
        Intents.init();
    }

    /**
     * Test that error view is set when
     * any field is empty.
     */
    @Test
    public void testErrorFieldEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Fields are Empty")));
    }

    /**
     * Test that error view is set when
     * the password fields don't match.
     */
    @Test
    public void testErrorFieldNotSame() {
        Espresso.onView(ViewMatchers.withId(R.id.enterAccountName)).perform(ViewActions.typeText("testName"));
        Espresso.onView(ViewMatchers.withId(R.id.enterPassword)).perform(ViewActions.typeText("testPass"));
        Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.typeText("testPassnt"));
        Espresso.onView(ViewMatchers.withId(R.id.enterAnswer)).perform(ViewActions.scrollTo()).perform(ViewActions.typeText("testAnswer"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Password incorrect")));
    }

    /**
     * Test that the proper error is shown when
     * the username already exists
     */
    @Test
    public void testErrorOnDuplicate() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.enterAccountName)).perform(ViewActions.typeText("UITester"));
            Espresso.onView(ViewMatchers.withId(R.id.enterPassword)).perform(ViewActions.typeText("testPass"));
            Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.typeText("testPass"));

            Espresso.onView(ViewMatchers.withId(R.id.enterAnswer)).perform(ViewActions.scrollTo()).perform(ViewActions.typeText("testAnswer"));
            Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
            //The test was sometimes running faster than the error view was being set, this wait,
            //though inelegant, should be enough to allow the app to check with the database and
            //update errView.
            Thread.sleep(1000);
            Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Username is already exist in database")));
        } catch (Exception e) {
            System.err.println("An error occurred in the sign in test error on duplicate. Test failed.");
        }
    }

    /**
     * Test the sign in is successful
     * and that the activity is changed.
     */
    @Test
    public void testSignUpSuccess() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.enterAccountName)).perform(ViewActions.typeText("FreeTestName"));
            Espresso.onView(ViewMatchers.withId(R.id.enterPassword)).perform(ViewActions.typeText("testPass"));
            Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.typeText("testPass"));
            Espresso.onView(ViewMatchers.withId(R.id.enterAnswer)).perform(ViewActions.scrollTo()).perform(ViewActions.typeText("answer"));
            Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
            //The checks and insertions to the database take time. Because this is a web hosted database,
            //the time needed to check with it and respond may change depending on internet speeds.
            //sleep 500 was sufficient to consistently pass the test with my garbage internet, but
            //you may need more time depending on other factors as well. There's a cleaner way to do this
            // but I don't want to waste too much time doing all that stuff when this works for now.
            Thread.sleep(500);
            //checks that we're back to main activity
            Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
        } catch (Exception e) {
            System.err.println("An error occurred in test SignUp success. Test failed.");
        }
    }

    /**
     * Test that the proper errView
     * message is set when a password
     * of invalid length is passed.
     */
    @Test
    public void testErrorOnInvalidPass() {
        Espresso.onView(ViewMatchers.withId(R.id.enterAccountName)).perform(ViewActions.typeText("FreeTestName"));
        Espresso.onView(ViewMatchers.withId(R.id.enterPassword)).perform(ViewActions.typeText("test"));
        Espresso.onView(ViewMatchers.withId(R.id.reEnterPassword)).perform(ViewActions.typeText("test"));
        Espresso.onView(ViewMatchers.withId(R.id.enterAnswer)).perform(ViewActions.scrollTo()).perform(ViewActions.typeText("answer"));
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Password Length must at least 6 characters")));
    }

    /**
     * Using what is believed to currently be the proper
     * way to tear down intents between tests. There may be a
     * better one, but this is what I could find.
     */
    @After
    public void tearDown() {
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference users = DB.getReference("Users");
        users.child("FreeTestName").removeValue();
        Intents.release();
    }
}

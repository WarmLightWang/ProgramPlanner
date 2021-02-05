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
import com.example.myapplication.activity.ResetPassActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the forgetQues activity.
 * Will check that the proper error
 * message is displayed on incorrect answer.
 * Also checks activity change on proper button presses.
 */
@RunWith(AndroidJUnit4.class)
public class ForgetQuesTest {
    /**
     * Tests will start from forgetPassActivity to
     * ensure we can match answers to the proper account.
     */
    @Rule
    public ActivityScenarioRule<ForgetPassActivity> activityRule =
            new ActivityScenarioRule<>(ForgetPassActivity.class);

    /**
     * Set up for intents.
     * Also navigates to activity
     * under test with correct info
     */
    @Before
    public void setUp() {
        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.typeText("UITester"));
        Espresso.onView(ViewMatchers.withId(R.id.accountName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println("An error occurred in ForgetQues setup. Test Failed.");
        }
    }

    /**
     * Test that the proper error is given
     * for an invalid answer.
     */
    @Test
    public void testInvalidAnswer() {
        Espresso.onView(ViewMatchers.withId(R.id.questionAnswer)).perform(ViewActions.typeText("Wrong Answer"));
        Espresso.onView(ViewMatchers.withId(R.id.questionAnswer)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Incorrect Security Answer")));
    }

    /**
     * Test that the proper intent is given
     * when a correct security answer is supplied
     */
    @Test
    public void testCorrectAnswer() {
        Espresso.onView(ViewMatchers.withId(R.id.questionAnswer)).perform(ViewActions.typeText("Espresso"));
        Espresso.onView(ViewMatchers.withId(R.id.questionAnswer)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNextStep)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ResetPassActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}

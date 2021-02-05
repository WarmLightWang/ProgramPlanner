package com.example.myapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.VerificationModes;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.activity.AddTaskActivity;
import com.example.myapplication.activity.CreateProjectActivity;
import com.example.myapplication.activity.EventActivity;
import com.example.myapplication.activity.GraphActivity;
import com.example.myapplication.activity.ImageChooser;
import com.example.myapplication.activity.JoinProjectActivity;
import com.example.myapplication.activity.LogViewActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.NotebookActivity;
import com.example.myapplication.activity.ProjectMainActivity;
import com.example.myapplication.activity.RoleViewActivity;
import com.example.myapplication.activity.TaskAssignActivity;
import com.example.myapplication.activity.TimelineActivity;
import com.example.myapplication.activity.ViewFeedbackActivity;
import com.example.myapplication.activity.ViewInvitationActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests possible actions from the main screen after logging in
 */
@RunWith(AndroidJUnit4.class)
public class ProjectMainActivityTest {
    /**
     * Start from login to make sure we have correct credentials
     */
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Set up by init intents and navigating
     * to activity under test with credentials
     */
    @Before
    public void setUp() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextAccountName)).perform(ViewActions.typeText("UITester"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("password"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogIn)).perform(ViewActions.click());
        Intents.init();
    }

    /**
     * Test create project by clicking create and
     * then cancel and ensuring both intents were passed
     */
    @Test
    public void testCreateCancel() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonCreate)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(CreateProjectActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.buttonCancel)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ProjectMainActivity.class.getName()));
    }

    /**
     * Test join project by clicking join and
     * then cancel, ensuring both intents were passed
     */
    @Test
    public void testJoinCancel() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonJoin)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(JoinProjectActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.buttonInvite)).perform(ViewActions.click());
        Intents.intended((IntentMatchers.hasComponent(ViewInvitationActivity.class.getName())));
        Espresso.pressBack();
        Espresso.onView(ViewMatchers.withId(R.id.buttonCancel)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ProjectMainActivity.class.getName()));
    }

    /**
     * Test logging out by clicking the not your
     * account dialogue then logging out.
     * Ensure proper intents are passed
     */
    @Test
    public void testLogout() {
        Espresso.onView(ViewMatchers.withId(R.id.notYourAccountTip)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogOut)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    /**
     * Test Create project by creating a new project
     * ensure proper error is shown when creating a project
     * of the same name
     */
    @Test
    public void testCreateSame() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonCreate)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textBoxProjectName)).perform(ViewActions.typeText("UITest Project 1"));
        Espresso.onView(ViewMatchers.withId(R.id.textBoxProjectName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonConfirm)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTip)).check(ViewAssertions.matches(ViewMatchers.withText("The project is already exist")));
    }

    /**
     * Test create project for a new project.
     * Ensure new project is added to home screen and intents are set
     */
    @Test
    public void testCreateRealProject() {
        //After this test, you must manually remove the created project by logging in
        //with username: UITester password:password to delete the second project on their list.
        //This will allow the test to be run again
        //You may also delete the project directly from firebase, but that takes a bit of searchingU
        Espresso.onView(ViewMatchers.withId(R.id.buttonCreate)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textBoxProjectName)).perform(ViewActions.typeText("UITest Project 2"));
        Espresso.onView(ViewMatchers.withId(R.id.textBoxProjectName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonConfirm)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ProjectMainActivity.class.getName()));
    }

    /**
     * Test join project. Attempts to join a project with invalid code
     */
    @Test
    public void testJoinFail() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonJoin)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textBoxInvitationCode)).perform(ViewActions.typeText("000000"));
        Espresso.onView(ViewMatchers.withId(R.id.textBoxInvitationCode)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonJoin)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTip)).check(ViewAssertions.matches(ViewMatchers.withText("The invitation code is invalid")));
    }

    /**
     * Test apply to project. Attempts to apply to join a project.
     * Checks that the intent is sent
     */
    @Test
    public void testApplyToProjectAsDev() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonJoin)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textBoxProjectName)).perform(ViewActions.typeText("UI Test Project Other"));
        Espresso.onView(ViewMatchers.withId(R.id.textBoxProjectName)).perform(ViewActions.closeSoftKeyboard());
        //Enter and select spinner
        Espresso.onView(ViewMatchers.withId(R.id.spinnerRole)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("developer")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonApply)).perform(ViewActions.click());
        //Check that application finished.
        Espresso.onView(ViewMatchers.withId(R.id.buttonJoin)).check(ViewAssertions.matches(ViewMatchers.withText("Join")));
    }
//Manager access tests

    /**
     * Task management test. I think the version of code I have here is bugged.
     * To prevent potential issues, this will just click through a few options
     * and make sure the right intents are passed.
     */
    @Test
    public void testTaskManagement() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskAssignment)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonAdd)).perform(ViewActions.click());
        Intents.intended((IntentMatchers.hasComponent(AddTaskActivity.class.getName())));
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddTask)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddTask)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTip)).check(ViewAssertions.matches(ViewMatchers.withText("Please enter a value for task name")));
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(TaskAssignActivity.class.getName()), VerificationModes.times(2));
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
    }

    /**
     * Test visiting feedback. Mostly just tests that
     * the proper activities are intended, as I can't make a
     * client to give feedback
     */
    @Test
    public void testClickIntoFeedback() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonViewFeedBack)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ViewFeedbackActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
    }

    /**
     * Test Notebook. First enters with no notes, then types a note,
     * submits, then exists
     */
    @Test
    public void testNotebook() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNoteBook)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(NotebookActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNote)).perform(ViewActions.typeText("Espresso waz here!"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextNote)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonSubmit)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonNoteBook)).perform(ViewActions.click());
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference users = DB.getReference("Project");
        users.child("54/Notebook").removeValue();
    }

    /**
     * Test the logs functionality.
     * Will work similarly to the notebook test.
     */
    @Test
    public void testLogsFromDev() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLog)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(LogViewActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextMultiLine3)).perform(ViewActions.typeText("Espresso waz here!"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextMultiLine3)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonSubmit)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLog)).perform(ViewActions.click());
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference users = DB.getReference("Project");
        users.child("54/Log").removeValue();
    }

    /**
     * Test the timeline features.
     */
    @Test
    public void testAddTimeline() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonTimeLine)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(TimelineActivity.class.getName()));
        //Enter the add event
        Espresso.onView(ViewMatchers.withId(R.id.buttonAdd)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(EventActivity.class.getName()));
        //Attempt to add a blank
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddEvent)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTip)).check(ViewAssertions.matches(ViewMatchers.withText("Either title or date is invalid")));
        //Fill fields
        Espresso.onView(ViewMatchers.withId(R.id.textBoxEventName)).perform(ViewActions.typeText("UI Testing"));
        Espresso.onView(ViewMatchers.withId(R.id.textBoxEventName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.textViewDate)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.checkBox)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddEvent)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(TimelineActivity.class.getName()), Intents.times(2));
        Espresso.onView(ViewMatchers.withId(R.id.buttonAdd)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
    }

    /**
     * Test edit in timeline. Will edit an event
     */
    @Test
    public void testEditTimeline() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonTimeLine)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(TimelineActivity.class.getName()));
        //Enter the edit mode
        Espresso.onView(ViewMatchers.withId(R.id.buttonEdit)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonEdit)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonEdit)).perform(ViewActions.click());
        //Select event to edit
        Espresso.onView(ViewMatchers.withParent(ViewMatchers.withId(R.id.EventLayout))).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(EventActivity.class.getName()));
    }

    /**
     * Test removing an event. Three stages.
     * 1. Enter delete mode and cancel.
     * 2. Select something to delete and cancel.
     * 3. Confirm delete with nothing selected
     * 4. confirm with something selected
     */
    @Test
    public void testRemoveTimeline() {
        //Setup
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonTimeLine)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(TimelineActivity.class.getName()));
        //1
        Espresso.onView(ViewMatchers.withId(R.id.buttonDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonCancel)).perform(ViewActions.click());
        //2
        Espresso.onView(ViewMatchers.withId(R.id.buttonDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withParent(ViewMatchers.withId(R.id.EventLayout))).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("CANCEL")).perform(ViewActions.click());
        //3
        Espresso.onView(ViewMatchers.withId(R.id.buttonDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("CANCEL")).perform(ViewActions.click());
        //4
        Espresso.onView(ViewMatchers.withParent(ViewMatchers.withId(R.id.EventLayout))).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("CONFIRM")).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withParent(ViewMatchers.withId(R.id.EventLayout))).check(ViewAssertions.matches(ViewMatchers.withText("There is no event for the project")));
    }

    /**
     * Test adding graphics. Will need at least 1 downloaded image
     * on virtual device for test to work.
     */
    @Test
    public void testGraphAdd() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonGraph)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(GraphActivity.class.getName()));

        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonGraph)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonUpload)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ImageChooser.class.getName()));

        Espresso.onView(ViewMatchers.withId(R.id.bt_upload_image)).perform(ViewActions.click());
        //Stub for image chooser.
        Matcher<Intent> expected = IntentMatchers.hasType("image/*");
        Instrumentation.ActivityResult result = imageChoiceStub();
        Intents.intending(expected).respondWith(result);
        Espresso.onView(ViewMatchers.withId(R.id.bt_choose_image)).perform(ViewActions.click());
        //Now in image chooser . This may cause issues.
        Intents.intended(expected);
        Espresso.onView(ViewMatchers.withId(R.id.bt_upload_image)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error occurred while waiting for photo upload. Test failed.");
        }
        Espresso.onView(ViewMatchers.withId(R.id.bt_back)).perform(ViewActions.click());
    }

    /**
     * Test roles.
     * Unfortunately, since the inviting doesn't work for me for
     * whatever reason, I can't ui test most of the role functionality.
     * The result of this is that I'll check the intent of accessing the screen
     * and see where that goes.
     */
    @Test
    public void testRoleIntent() {
        Espresso.onView(ViewMatchers.withText("54:UITest Project 1  (manager)")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonManageRole)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(RoleViewActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
    }

    /**
     * Remove the second project.
     * Due to exceedingly long delete times, will manually remove the object to prevent later conflict
     * and that the activity reverts to the main scree.
     */
    @Test
    public void testRemoveProject() {
        //The below int is the current id for the project in question. As
        //The testing process develops, or other projects are added, this will need to be updated.
        //If this test fails, log in as username: UITester password: password and see new value
        int currId = 54;
        Espresso.onView(ViewMatchers.withText(String.format("%d:UITest Project 1  (manager)", currId))).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ProjectMainActivity.class.getName()));
        Espresso.onView(ViewMatchers.withText(String.format("%d:UITest Project 1  (manager)", currId))).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.buttonDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Confirm")).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(ProjectMainActivity.class.getName()));

    }

    /**
     * Release intents.
     */
    @After
    public void tearDown() {
        Intents.release();

    }

    /*
     * Stub the image choosing behavior
     * Found from https://www.youtube.com/watch?v=CpQQ6-vo5bw
     * with some bug fixes from https://stackoverflow.com/questions/4896223/how-to-get-an-uri-of-an-image-resource-in-android
     */
    private Instrumentation.ActivityResult imageChoiceStub() {
        Resources testRes = InstrumentationRegistry.getInstrumentation().getContext().getResources();
        Uri imageURI = Uri.parse("android.resource://src/main/res/drawable/" + R.drawable.arrowicon);
        Intent resultIntent = new Intent();
        resultIntent.setData(imageURI);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent);
    }
}

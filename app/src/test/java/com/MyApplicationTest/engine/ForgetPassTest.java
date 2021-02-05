package com.MyApplicationTest.engine;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.example.myapplication.activity.ForgetPassActivity;
import com.example.myapplication.activity.ForgetQuesActivity;
import com.example.myapplication.engine.ForgetPass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests the ForgetPass class.
 * Most tests will probably have to be at a higher level
 * due to the integrated nature of this class
 */
public class ForgetPassTest {
    @Mock
    FirebaseDatabase databaseMock;

    @Mock
    DatabaseReference referenceMock;

    @Before
    public void setUp() {
        databaseMock = Mockito.mock(FirebaseDatabase.class);
        referenceMock = Mockito.mock(DatabaseReference.class);
        when(databaseMock.getReference(anyString())).thenReturn(referenceMock);
    }
    //<constructor tests>

    /**
     * This constructor calls check username, so
     * we will verify that the proper line was called.
     */
    @Test
    public void testForgetPassConstructor() {
        ForgetPassActivity testPassAct = new ForgetPassActivity();
        try (MockedStatic<FirebaseDatabase> mockFirebase = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockFirebase.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            ForgetPass testForgetPass = new ForgetPass(testPassAct, "test");
            //Verifies we reached check username
            Mockito.verify(referenceMock, Mockito.times(1)).addValueEventListener(Mockito.any());
        }
    }
    //</constructor tests>

    /**
     * Test that reference.addValueEventListener is called in getQuestion
     * We will verify that it was called after creating a forget pass and
     * calling the function
     */
    @Test
    public void testForgetQuesAndGetQuestion() {
        ForgetQuesActivity testQuesAct = new ForgetQuesActivity();
        try (MockedStatic<FirebaseDatabase> mockFirebase = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockFirebase.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            ForgetPass testForgetPass = new ForgetPass(testQuesAct, "test");
            testForgetPass.getQuestion();
            //Verify that the method was entered, as we can't easily test the listeners.
            Mockito.verify(referenceMock, Mockito.times(1)).addValueEventListener(Mockito.any());
        }
    }

    /**
     * Test that reference.addValueEventListener is called in verify question
     * We can not easily test the listeners themselves so we will make sure that
     * this method is called.
     */
    @Test
    public void testVerifyQuestion() {
        ForgetQuesActivity testQuesAct = new ForgetQuesActivity();
        try (MockedStatic<FirebaseDatabase> mockFirebase = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockFirebase.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            ForgetPass testForgetPass = new ForgetPass(testQuesAct, "test");
            testForgetPass.verifyQuestion("answer");
            //Verify that the method was entered, as we can't easily test the listeners.
            Mockito.verify(referenceMock, Mockito.times(1)).addValueEventListener(Mockito.any());
        }
    }
}

package com.MyApplicationTest.engine;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import com.example.myapplication.element.User;
import com.example.myapplication.activity.SignUpActivity;
import com.example.myapplication.engine.SignUp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Tests the SignUp class.
 * Methods will be tested in a somewhat integrated state
 * due to their closely tied functionality.
 * ~/app/src/main/java/com/example/myapplication/engine/SignUp.java
 */
public class SignUpTest {
    @Mock
    FirebaseDatabase databaseMock;

    @Mock
    DatabaseReference referenceMock;

    @Mock
    SignUpActivity signUpMock;

    @Before
    public void setUp() {
        //passFlag = false;
        databaseMock = Mockito.mock(FirebaseDatabase.class);
        referenceMock = Mockito.mock(DatabaseReference.class);
        signUpMock = Mockito.mock(SignUpActivity.class);
        when(databaseMock.getReference(anyString())).thenReturn(referenceMock);

    }

    /**
     * Test constructor with hashPassword returning null.
     * Should call signup and set the activity's ErrView
     * this should verify that activity and user are set by constructor,
     * as well as covering a branch of signup.
     */
    @Test
    public void testConstructorAndErrSet() {
        User testUser = new User("testName", "CheckPass");
        //This static mocking technique found at
        //https://tech.cognifide.com/blog/2020/mocking-static-methods-made-possible-in-mockito-3.4.0/
        try (MockedStatic<FirebaseDatabase> mockFirebase = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockFirebase.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            try (MockedStatic<User> mockUser = Mockito.mockStatic(User.class)) {
                mockUser.when(() -> User.hashPassword(anyString())).thenReturn(null);
                SignUp testSignUp = new SignUp(signUpMock, testUser);

                //Verify that setErrView was called due to null password
                verify(signUpMock, times(1)).setErrView("Encountered unexpected error");
            }
        }
    }

    /**
     * Test signup with a non null user password.
     * This will call updateDatabase.
     * Verify that the addListenerForSingleValueEvent is called
     */
    @Test
    public void testSignUpNotNull() {
        User testUser = new User("testName", "testPass");
        try (MockedStatic<FirebaseDatabase> mockFirebase = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockFirebase.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            SignUp testSignUp = new SignUp(signUpMock, testUser);
            verify(referenceMock, times(1)).addListenerForSingleValueEvent(any());
        }
    }

    /**
     * Test validatePassword.
     * Will be called once with null password on constructor,
     * to make sure it fails
     */
    @Test
    public void testValidatePasswordFalse() {
        User testUser = new User("testName", null);
        try (MockedStatic<FirebaseDatabase> mockFirebase = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockFirebase.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            SignUp testSignUp = new SignUp(signUpMock, testUser);
            //validate that setErrView was called with "Password Length must be at least 6 characters"
            verify(signUpMock, times(1)).setErrView("Password Length must at least 6 characters");
        }
    }
}

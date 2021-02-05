package com.MyApplicationTest.engine;

import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.element.User;
import com.example.myapplication.engine.Login;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Tests the login class.
 * As much of it as possible will be unit tested,
 * though some of it may need higher level
 * tests due to its integrated nature.
 */
public class LoginTest {
    @Mock
    FirebaseDatabase databaseMock;

    @Mock
    DatabaseReference referenceMock;

    @Mock
    LoginActivity loginMock;

    /**
     * Sets up the mocks for testing
     */
    @Before
    public void setUp() {
        databaseMock = Mockito.mock(FirebaseDatabase.class);
        referenceMock = Mockito.mock(DatabaseReference.class);
        loginMock = Mockito.mock(LoginActivity.class);
        when(databaseMock.getReference(anyString())).thenReturn(referenceMock);
    }

    /**
     * Tests that retrieveDatabase is entered.
     * Verifies that addValueEventListener is
     * called. Interior will have to be checked
     * by higher level tests.
     */
    @Test
    public void testRetrieveDatabase() {
        try(MockedStatic<FirebaseDatabase> firebaseMock = Mockito.mockStatic(FirebaseDatabase.class)){
            firebaseMock.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            Login testLogin = new Login(loginMock);
            //Verify that this function was called.
            Mockito.verify(referenceMock, Mockito.times(1)).addValueEventListener(Mockito.any());
        }
    }

    /**
     * Tests the login function for a
     * null passHash. Verify that setErrText
     * is called.
     */
    @Test
    public void testLoginHashNull() {
        try(MockedStatic<FirebaseDatabase> firebaseMock = Mockito.mockStatic(FirebaseDatabase.class)) {
            firebaseMock.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            Login testLogin = new Login(loginMock);
            testLogin.login(new User("TestName", null));
            //Verify we reached the correct branch
            System.out.println("***One NullPointerException expected to be printed above from hashPassword***");
            Mockito.verify(loginMock, times(1)).setErrText("Encounter unexpected error");
        }
    }

    /**
     * Tests the login function for a non null hash.
     * Checks that user.password was changed.
     */
    @Test
    public void testLoginNotNull() {
        try(MockedStatic<FirebaseDatabase> firebaseMock = Mockito.mockStatic(FirebaseDatabase.class)) {
            firebaseMock.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            Login testLogin = new Login(loginMock);
            User testUser = new User("TestName", "testPass");
            ArrayList<User> testList = new ArrayList<User>();
            testList.add(testUser);
            testLogin.setUserList(testList);
            testLogin.login(testUser);
            assertNotEquals("The user's password was not changed to the hashed version.",
                    "testPass", testUser.password);
        }
    }

    /**
     * Tests the verifyUser Function for a user not in the list
     * tested by going though the "login" function
     * as verifyUser is private
     */
    @Test
    public void testVerifyUserNotInList() {
        try(MockedStatic<FirebaseDatabase> firebaseMock = Mockito.mockStatic(FirebaseDatabase.class)) {
            firebaseMock.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            Login testLogin = new Login(loginMock);
            User testUser = new User("TestName", "testPass");
            ArrayList<User> testList = new ArrayList<User>();
            testList.add(new User("FakeName", "FakePass"));
            testLogin.setUserList(testList);
            testLogin.login(testUser);
            Mockito.verify(loginMock, times(1)).setErrText("Either Username or Password is Incorrect");
        }
    }

    /**
     * Tests the verifyUser function for a user on the list
     * Tested by going trough the "login function
     * as verifyUser is private
     */
    @Test
    public void testVerifyUserOnList() {
        try(MockedStatic<FirebaseDatabase> firebaseMock = Mockito.mockStatic(FirebaseDatabase.class)) {
            firebaseMock.when(FirebaseDatabase::getInstance).thenReturn(databaseMock);
            Login testLogin = new Login(loginMock);
            User testUser = new User("TestName", "testPass");
            ArrayList<User> testList = new ArrayList<User>();
            testList.add(new User("FalseName", "FalsePass"));
            testList.add(new User("FakeName", "FakePass"));
            testList.add(new User("FalseName", "FalsePass"));
            testList.add(new User("FakeName", "FakePass"));
            testList.add(new User("FalseName", "FalsePass"));
            testList.add(new User("FakeName", "FakePass"));
            testList.add(testUser);
            testLogin.setUserList(testList);
            testLogin.login(testUser);
            Mockito.verify(loginMock, times(1)).finishLogin(testUser.username);
        }
    }
}

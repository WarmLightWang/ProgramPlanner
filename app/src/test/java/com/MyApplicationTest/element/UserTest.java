package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.User;

/**
 * Tests the user class.
 * Currently just a constructor test.
 * ~/app/src/main/java/com/example/myapplication/User.java
 */
public class UserTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */

    @Test
    public void testConstructorUsername() {
        User testUser =
                new User("test name", "blank", 0, "blank");
        assertTrue("User constructor failed to properly initialize the 'username' field.", testUser.username.equals("test name"));
    }

    @Test
    public void testConstructorPassword() {
        User testUser =
                new User("blank", "test pass", 0, "blank");
        assertTrue("User constructor failed to properly initialize the 'password' field.", testUser.password.equals("test pass"));
    }

    @Test
    public void testConstructorQuestionIndex() {
        User testUser =
                new User("blank", "blank", 1, "blank");
        assertTrue("User constructor failed to properly initialize the 'questionIndex field.", testUser.questionIndex == 1);
    }

    @Test
    public void testConstructorAnswer() {
        User testUser =
                new User("blank", "blank", 0, "test answer");
        assertTrue("User constructor failed to properly initialize the 'answer' field.", testUser.answer.equals("test answer"));
    }

    // 2 param constructor tests

    @Test
    public void testConstructorUsername2Param() {
        User testUser =
                new User("test name", "blank");
        assertTrue("2 Param User constructor failed to properly initialize the 'username' field.", testUser.username.equals("test name"));
    }

    @Test
    public void testConstructorPassword2Param() {
        User testUser =
                new User("blank", "test pass");
        assertTrue("2 Param User constructor failed to properly initialize the 'password' field.", testUser.password.equals("test pass"));
    }

    @Test
    public void testConstructorQuestionIndex2Param() {
        User testUser =
                new User("blank", "blank");
        assertTrue("2 Param User constructor failed to properly initialize the 'questionIndex field.", testUser.questionIndex == -1);
    }

    @Test
    public void testConstructorAnswer2Param() {
        User testUser =
                new User("blank", "blank");
        assertTrue("2 Param User constructor failed to properly initialize the 'answer' field.", testUser.answer == null);
    }
    //</Constructor Tests>

    //<Hash passwords tests>

    /**
     * Tests that hashPassword returns the hashed version of 'password'.
     * The expected value was found by running the hashing algorithm outside of this function.
     * While this may be a flawed method of testing, it will at least confirm that the same algorithm
     * produces the same results.
     */
    @Test
    public void testHashPasswordGeneric() {
        assertTrue("HashPassword did not return the expected resulting value. (SEE DOCUMENTATION OF THIS TEST)",
                "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8".equals(User.hashPassword("password")));
    }

    /**
     * Tests hashPassword on an empty string.
     * Expected string was found as above, and also verified on the web for SHA-256 of empty string
     */
    @Test
    public void testHashPasswordEmpty() {
        assertTrue("HashPassword did not return the expected value for empty string. (SEE DOCUMENTATION OF THIS TEST)",
                "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".equals(User.hashPassword("")));
    }

    /**
     * HashPassword will be passed a null parameter,
     * which should cause a nullPointerException to be thrown.
     * The function should handle this and return null.
     */
    @Test
    public void testHashPasswordNull() {
        assertNull("HashPassword failed to return null upon encountering an exception.",
                User.hashPassword(null));
        System.out.println("***One java.lang.NullPointerException should be thrown from com.MyApplicationTest...testHashPasswordNull***");
    }

    /**
     * Test isEqual on users with same username/password
     * should return true
     */
    @Test
    public void testIsEqualTrue() {
        User testUser0 = new User("testName", "testPass");
        User testUser1 = new User("testName", "testPass");
        assertTrue("IsEquals did not return true for 2 equal users.",
                testUser0.isEqual(testUser1));
    }

    /**
     * Test isEqual on users that aren't equal
     * should return false
     */
    @Test
    public void testIsEqualFalse() {
        User testUser0 = new User("testName", "testPass");
        User testUser1 = new User("testName1", "testPass1");
        assertFalse("IsEquals did not return false for 2 non-equal users.",
                testUser0.isEqual(testUser1));
    }
}
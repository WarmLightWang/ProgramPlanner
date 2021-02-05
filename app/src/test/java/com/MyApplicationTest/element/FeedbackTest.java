package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Feedback;

/**
 * Tests the Feedback class.
 * Tests all current methods
 * ~/app/src/main/java/com/example/myapplication/Feedback.java
 */
public class FeedbackTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */
    @Test
    public void testConstructorUsername() {
        Feedback testFeed = new Feedback("test username", "blank");
        assertTrue("Feedback constructor failed to properly initialize the 'username' field.", testFeed.username.equals("test username"));
    }

    @Test
    public void testConstructorComment() {
        Feedback testFeed = new Feedback("blank", "test comment");
        assertTrue("Feedback constructor failed to properly initialize the 'comment' field.", testFeed.comment.equals("test comment"));
    }

    //Default constructor tests

    @Test
    public void testDefaultConstructorUsername() {
        Feedback testFeed = new Feedback();
        assertTrue("Default Feedback constructor failed to properly initialize the 'username' field.", testFeed.username.equals(""));
    }

    @Test
    public void testDefaultConstructorComment() {
        Feedback testFeed = new Feedback();
        assertTrue("Default Feedback constructor failed to properly initialize the 'comment' field.", testFeed.comment.equals(""));
    }

    //</Constructor Tests>

    /**
     * Test the Username getter.
     * Should be trivial to pass.
     * If it fails, something is VERY wrong.
     */
    @Test
    public void testGetUsername() {
        Feedback testFeed = new Feedback("test username", "blank");
        assertEquals("GetUsername did not return the object's username.", testFeed.username, testFeed.getUsername());
    }

    /**
     * Test the comment getter.
     * Should be trivial to pass.
     * If it fails, something is VERY wrong.
     */
    @Test
    public void testGetComment() {
        Feedback testFeed = new Feedback("blank", "test comment");
        assertEquals("GetComment did not return the object's comment.", testFeed.comment, testFeed.getComment());
    }
}

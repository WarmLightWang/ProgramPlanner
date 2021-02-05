package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Log;

/**
 * Tests the Log class.
 * Currently just constructor Test.
 * ~/app/src/main/java/com/example/myapplication/Log.java
 */
public class LogTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */

    @Test
    public void testConstructorDate() {
        Log testLog = new Log("Test date", "blank", "blank");
        assertTrue("Log constructor failed to initialize 'date' field.", testLog.date.equals("Test date"));
    }

    @Test
    public void testConstructorContent() {
        Log testLog = new Log("blank", "test content", "blank");
        assertTrue("Log constructor failed to initialize 'content' field.", testLog.content.equals("test content"));
    }

    @Test
    public void testConstructUsername() {
        Log testLog = new Log("blank", "blank", "test name");
        assertTrue("Log constructor failed to initialize 'username' field.", testLog.username.equals("test name"));
    }
}

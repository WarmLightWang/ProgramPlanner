package com.MyApplicationTest.element;

import com.example.myapplication.element.Application;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Tests the application class.
 * Currently just constructor
 * ~/app/src/main/java/com/example/myapplication/element/Application.java
 */
public class ApplicationTest {
    /**
     * Test constructor.
     * Ensure fields are assigned.
     */
    @Test
    public void testConstructor() {
        Application testApp = new Application("TestName", "TestRole");
        assertTrue("Constructor failed to initialize application fields.",
                testApp.username.equals("TestName") && testApp.roles.equals("TestRole"));
    }
}

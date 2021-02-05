package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Event;

/**
 * Tests the Event class.
 * Tests for all current methods
 * ~/app/src/main/java/com/example/myapplication/Event.java
 */
public class EventTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */

    //3 param constructor tests
    @Test
    public void testConstructorEventTitle() {
        Event testEvent = new Event("Test Title", "blank", false);
        assertTrue("Event constructor failed to initialize the 'eventTitle' field.", testEvent.eventTitle.equals("Test Title"));
    }

    @Test
    public void testConstructorEventDate() {
        Event testEvent = new Event("blank", "testDate", false);
        assertTrue("Event constructor failed to initialize the 'eventDate' field.", testEvent.eventDate.equals("testDate"));
    }

    @Test
    public void testConstructorIsNotify() {
        Event testEvent = new Event("blank", "blank", true);
        assertTrue("Event constructor failed to initialize the 'isNotify' field.", testEvent.isNotify);
    }

    //4 param constructor tests
    @Test
    public void testConstructor4EventId() {
        Event testEvent = new Event("13", "blank", "blank", false);
        assertTrue("4 param constructor failed to initialize 'evendId' field.",
                testEvent.eventId.equals("13"));
    }

    @Test
    public void testConstructor4EventTitle() {
        Event testEvent = new Event("blank", "Test Title", "blank", false);
        assertTrue("Event constructor failed to initialize the 'eventTitle' field.", testEvent.eventTitle.equals("Test Title"));
    }

    @Test
    public void testConstructor4EventDate() {
        Event testEvent = new Event("blank", "blank", "testDate", false);
        assertTrue("Event constructor failed to initialize the 'eventDate' field.", testEvent.eventDate.equals("testDate"));
    }

    @Test
    public void testConstructor4IsNotify() {
        Event testEvent = new Event("blank", "blank", "blank", true);
        assertTrue("Event constructor failed to initialize the 'isNotify' field.", testEvent.isNotify);
    }

    /**
     * Test the toString function.
     * Ensures created string is accurately formatted.
     */
    @Test
    public void testToString() {
        String correct = "Event Title: Test Title, Event Date: Test Date\n";
        Event testEvent = new Event("Test Title", "Test Date", false);
        assertTrue("Event.toString did not return correct string.", correct.equals(testEvent.toString()));
    }
}

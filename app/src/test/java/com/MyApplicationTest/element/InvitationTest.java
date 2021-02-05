package com.MyApplicationTest.element;

import org.junit.Test;

import com.example.myapplication.element.Invitation;

import static org.junit.Assert.*;

/**
 * Tests the invitation class.
 * Currently just constructor
 * ~/app/src/main/java/com/example/myapplication/element/Invitation.java
 */
public class InvitationTest {
    /**
     * Test Invitation constructor.
     * Check that projectId is assigned.
     */
    @Test
    public void testConstructor() {
        Invitation testInvitation = new Invitation("12", "testName", "testRole");
        assertTrue("Constructor didn't initialize fields as required.",
                testInvitation.projectId.equals("12") && testInvitation.projectName.equals("testName")
                        && testInvitation.projectRole.equals("testRole"));
    }
}

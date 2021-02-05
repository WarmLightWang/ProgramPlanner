package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Project;
import com.example.myapplication.element.Session;

/**
 * Tests the Session class.
 * Covers all methods in session of November 1st commit.
 * ~/app/src/main/java/com/example/myapplication/Session.java
 */
public class SessionTest {
    /**
     * Test getInstance.
     * verifies that get instance returns the
     * statically assigned value.
     */
    @Test
    public void testGetInstance() {
        assertNotNull(Session.getInstance());
    }

    /**
     * Tests setUserName
     * Also covers getUserName
     * to verify a username was added
     */
    @Test
    public void testSetUserNameName() {
        Session testSession = Session.getInstance();
        testSession.setUserName("Test Name");
        assertTrue("The 'userName' field was not properly updated with the passed userName.",
                "Test Name".equals(testSession.getUserName()));
    }

    /**
     * Tests setUserName
     * Also covers getCurrProject
     * to verify a project was created.
     */
    @Test
    public void testSetUserNameCurr() {
        Session testSession = Session.getInstance();
        testSession.setUserName("Test Name");
        assertNotNull("CurrProject was not filled by setting username.", testSession.getCurrProject());
    }

    /**
     * Tests setCurrProject
     * Also covers getCurrProject
     * to verify a project was set
     */
    @Test
    public void testSetCurrProject() {
        Session testSession = Session.getInstance();
        testSession.setCurrProject(new Project());
        assertNotNull("CurrProject was not assigned by setCurrProject", testSession.getCurrProject());
    }

    /**
     * Tests getProjectName.
     * also covers setCurrProject
     * to create a named project
     */
    @Test
    public void testGetProjectName() {
        Session testSession = Session.getInstance();
        testSession.setCurrProject(new Project(1, "testName"));
        assertTrue("GetProjectName did not give the active project name.",
                testSession.getProjectName().equals("testName"));
    }

    /**
     * Tests getProjectId.
     * also covers setCurrProject
     * to create a set id for the project.
     */
    @Test
    public void testGetProjectId() {
        Session testSession = Session.getInstance();
        testSession.setCurrProject(new Project(12, "testName"));
        assertTrue("GetProjectId did not give the expected id as a string.",
                testSession.getProjectId().equals("12"));
    }
}

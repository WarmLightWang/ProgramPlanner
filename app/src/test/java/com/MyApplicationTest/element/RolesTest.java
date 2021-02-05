package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Roles;

/**
 * Tests the Roles class.
 * Currently just constructor Test.
 * ~/app/src/main/java/com/example/myapplication/Roles.java
 */
public class RolesTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */

    @Test
    public void testConstructorProjectId() {
        Roles testRole = new Roles("1", "blank", "blank");
        assertTrue("Roles constructor failed to initialize 'projectId' field.", testRole.projectId.equals("1"));
    }

    @Test
    public void testConstructorProjectName() {
        Roles testRole = new Roles("blank", "test name", "blank");
        assertTrue("Roles constructor failed to initialize 'projectName' field.", testRole.projectName.equals("test name"));
    }

    @Test
    public void testConstructorRoles() {
        Roles testRole = new Roles("blank", "blank", "test role");
        assertTrue("Roles constructor failed to initial 'roles' field.", testRole.roles.equals("test role"));
    }

    //</Constructor Tests>
}

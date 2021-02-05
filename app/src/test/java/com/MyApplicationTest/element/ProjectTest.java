package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Project;

/**
 * Tests all methods in the Project class
 * See test comments for more details
 * ~/app/src/main/java/com/example/myapplication/Project.java
 */
public class ProjectTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     * Two objects will be created to make sure static fields are changed properly and non-statics are not.
     * First created object will be used for both kinds, to verify that the field changed or didn't change respectively.
     */

    @Test
    public void testConstructorIProjectId() {
        Project testProject = new Project(5, "blank", "blank", "blank");
        Project testProject1 = new Project(4, "blank", "blank", "blank");
        assertEquals("Project constructor failed to initialize the 'i_projectId' field.", 5, testProject.projectId);
    }

    @Test
    public void testConstructorIProjectName() {
        Project testProject = new Project(0, "create Name", "blank", "blank");
        Project testProject1 = new Project(0, "overwrite Name", "blank", "blank");
        assertTrue("Project constructor failed to initialize the 'i_projectName' field.", testProject.projectName.equals("create Name"));
    }

    @Test
    public void testConstructorIClientCode() {
        Project testProject = new Project(0, "blank", "create Code", "blank");
        Project testProject1 = new Project(0, "blank", "overwrite Code", "blank");
        assertTrue("Project constructor failed to initialize the 'i_clientCode' field.", testProject.clientCode.equals("create Code"));
    }

    @Test
    public void testConstructorIDevCode() {
        Project testProject = new Project(0, "blank", "blank", "create Code");
        Project testProject1 = new Project(0, "blank", "blank", "overwrite Code");
        assertTrue("Project constructor failed to initialize the 'i_clientCode' field.", testProject.devCode.equals("create Code"));
    }

    //</Constructor Tests>

    /*
     * <generateCode Tests>
     *  The generate code tests will test different
     *  paths through the function
     * Randomness is difficult to test, so we will instead make sure there are the right number
     * of digits
     */

    @Test
    public void testGenerateCodeIsClientLength() {
        String generated = Project.generateCode(true);
        assertEquals("Generated code length was not 6.", 6, generated.length());
    }

    @Test
    public void testGenerateCodeIsClientIsOdd() {
        String generated = Project.generateCode(true);
        assertEquals("Generated code for client was not odd.", 1, Integer.parseInt(generated) % 2);
    }

    @Test
    public void testGenerateCodeIsntClientLength() {
        String generated = Project.generateCode(false);
        assertEquals("Generated code length was not 6.", 6, generated.length());
    }

    @Test
    public void testGenerateCodeIsntClientEven() {
        String generated = Project.generateCode(false);
        assertEquals("Generated code for non-client was not even.", 0, Integer.parseInt(generated) % 2);
    }

    @Test
    public void testGenerateCodeManyLength() {
        int[] codes = new int[100];
        for (int i = 0; i < 100; i++) {
            codes[i] = Project.generateCode((i % 2) == 0).length();
            assertEquals("Incorrect length on code number " + i + ".", 6, codes[i]);
        }
    }

    @Test
    public void testGenerateCodeManyOddEven() {
        String[] codes = new String[100];
        for (int i = 0; i < 100; i++) {
            codes[i] = Project.generateCode((i % 2) == 1);
            assertEquals("Incorrect length on code number " + i + ".", i % 2, Integer.parseInt(codes[i]) % 2);
        }
    }
    //</generateCode tests>

    //Getters tested with constructor tests.

    /**
     * Test the to string method. Not a super complex method, simply
     * concats an int to a string. We'll give it one test anyway.
     */
    @Test
    public void testToString() {
        Project testProject = new Project(1, "blank", "blank client", "blank code");
        Project testProject1 = new Project(2, "blank", "blank", "blank");
        assertTrue("To string did not return the properly formatted string.",
                "Name: blank, Id: 1, Client: blank client, Dev: blank code".equals(testProject.toString()));
    }
}

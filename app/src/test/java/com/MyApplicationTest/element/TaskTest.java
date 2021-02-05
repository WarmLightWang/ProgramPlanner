package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.example.myapplication.element.Task;

/**
 * Tests the Task class.
 * Currently just constructor Test.
 * ~/app/src/main/java/com/example/myapplication/Roles.java
 */
public class TaskTest {
    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */

    @Test
    public void testNoListParamConstructorTask() {
        Task testTask = new Task("1", "Test task");
        assertTrue("No list parameter Task constructor failed to initialize 'task' field.",
                testTask.task.equals("Test task"));
    }

    @Test
    public void testNoListParamConstructorList() {
        Task testTask = new Task("1", "Test task");
        assertTrue("No list parameter Task constructor failed to create empty list for 'memberList' field.",
                testTask.memberList.isEmpty());
    }

    @Test
    public void testNoListParamConstructorId() {
        Task testTask = new Task("1", "Test task");
        assertTrue("No list parameter Task constructor failed to initialize 'taskId' field.",
                testTask.taskId.equals("1"));
    }

    @Test
    public void testNoIdParamConstructorTask() {
        Task testTask = new Task("Test task", new ArrayList<String>());
        assertTrue("No id parameter Task constructor failed to initialize 'task' field.",
                testTask.task.equals("Test task"));
    }

    @Test
    public void testNoIdParamConstructorList() {
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("test 1");
        Task testTask = new Task("Test task", testList);
        assertTrue("No id parameter Task constructor failed to use list for 'memberList' field.",
                testTask.memberList.get(0).equals("test 1"));
    }

    @Test
    public void test3ParamConstructorId() {
        ArrayList<String> testList = new ArrayList<String>();
        Task testTask = new Task("1", "Test name", testList);
        assertTrue("3 parameter Task constructor failed to initialize 'taskId' field.",
                testTask.taskId.equals("1"));
    }

    @Test
    public void test3ParamConstructorName() {
        ArrayList<String> testList = new ArrayList<String>();
        Task testTask = new Task("1", "Test name", testList);
        assertTrue("3 parameter Task constructor failed to initialize 'task' field.",
                testTask.task.equals("Test name"));
    }

    @Test
    public void test3ParamConstructorList() {
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("test 1");
        Task testTask = new Task("1", "Test name", testList);
        assertTrue("3 parameter Task constructor failed to use list for 'memberList' field.",
                testTask.memberList.get(0).equals("test 1"));
    }

    //</Constructor Tests>

    /**
     * Test the toString method.
     * Verifies a properly formatted string is returned.
     * This tests the possible error case of an uninitialized id.
     */
    @Test
    public void testToString() {
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("test 1");
        Task testTask = new Task("1", "TestName", testList);
        assertTrue("ToString did not return a string in the expected format.",
                "1: TestName\n(test 1, )".equals(testTask.toString()));

    }
}
package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Notebook;

/**
 * Tests the Notebook class.
 * Currently just constructor Test.
 * ~/app/src/main/java/com/example/myapplication/Notebook.java
 */
public class NotebookTest {

    /*
     * <Constructor Tests>
     * The following tests all test one aspect of the constructor.
     * They are very simple in nature, and merely assert that the
     * values of the created object reflect those passed in the constructor.
     * One test for each field.
     */

    @Test
    public void testConstructorUsername() {
        Notebook testBook = new Notebook("test name", "blank");
        assertTrue("Notebook constructor failed to initialize 'username' field.", testBook.username.equals("test name"));
    }

    @Test
    public void testConstructorContent() {
        Notebook testBook = new Notebook("blank", "test content");
        assertTrue("Notebook constructor failed to initialize 'content' field.", testBook.content.equals("test content"));
    }
    //</Constructor Test>
}

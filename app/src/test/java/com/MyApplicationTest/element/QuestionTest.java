package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Question;

/**
 * Tests the Question class.
 * Checks that the static variable can be accessed.
 * ~/app/src/main/java/com/example/myapplication/Question.java
 */
public class QuestionTest {
    //<QUESTION test>
    @Test
    public void testQuestion0() {
        assertTrue("Accessed string was no expected question.",
                "What is your mother's maiden name?".equals(Question.QUESTION[0]));
    }

    @Test
    public void testQuestion1() {
        assertTrue("Accessed string was no expected question.",
                "What is the name of your first pet?".equals(Question.QUESTION[1]));
    }

    @Test
    public void testQuestion2() {
        assertTrue("Accessed string was no expected question.",
                "What was your first car?".equals(Question.QUESTION[2]));
    }

    @Test
    public void testQuestion3() {
        assertTrue("Accessed string was no expected question.",
                "What elementary school did you attend?".equals(Question.QUESTION[3]));
    }

    @Test
    public void testQuestion4() {
        assertTrue("Accessed string was no expected question.",
                "What is the name of the town where you were born?".equals(Question.QUESTION[4]));
    }

    //</QUESTION test>
}

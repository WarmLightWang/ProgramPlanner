package com.MyApplicationTest.element;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.element.Image;

/**
 * Tests all methods in the Image class
 * See test comments for more details
 * May need to update in the future.
 * ~/app/src/main/java/com/example/myapplication/Image.java
 */
public class ImageTest {
    //<constructor test>
    @Test
    public void test1ParamConstructor() {
        Image testImage = new Image("test/url");
        assertTrue("1 parameter image constructor failed to initialize 'ImageUrl' field.",
                "test/url".equals(testImage.ImageUrl));
    }

    @Test
    public void testBlankConstructor() {
        Image testImage = new Image();
        assertNull("Blank image constructor somehow put a value in 'ImageUrl' field.",
                testImage.ImageUrl);
    }
}

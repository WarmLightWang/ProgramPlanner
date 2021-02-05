package com.MyApplicationTest.engine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;

import static org.junit.Assert.*;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.example.myapplication.engine.ImageAdapter;
import com.example.myapplication.element.Image;

import static org.mockito.Mockito.when;

/**
 * Tests the ImageAdapter class.
 * Methods will be unit tested as possible,
 * but may require higher level tests
 * due to the integrated nature of the class
 * ~/app/src/main/java/com/example/myapplication/engine/ImageAdapter.java
 */
public class ImageAdapterTest {

    @Mock
    Context mockContext;

    @Mock
    ViewGroup mockGroup;

    @Mock
    View mockView;

    @Mock
    LayoutInflater mockInflater;

    /**
     * Set up the mocks
     */
    @Before
    public void setUp() {
        mockContext = Mockito.mock(Context.class);
        mockGroup = Mockito.mock(ViewGroup.class);
        mockView = Mockito.mock(View.class);
        mockInflater = Mockito.mock(LayoutInflater.class);
        when(mockInflater.inflate(Mockito.anyInt(), Mockito.any(), Mockito.anyBoolean())).thenReturn(mockView);
    }

    /**
     * Test onCreateViewHolder. Makes certain that an
     * instance of imageHolder is returned.
     */
    @Test
    public void testOnCreateViewHolder() {
        ArrayList<Image> imageList = new ArrayList<Image>();
        imageList.add(new Image("test/image/url"));
        ImageAdapter testAdapter = new ImageAdapter(mockContext, imageList);
        try (MockedStatic<LayoutInflater> mockLayout = Mockito.mockStatic(LayoutInflater.class)) {
            mockLayout.when(() -> LayoutInflater.from(Mockito.any())).thenReturn(mockInflater);
            //System.out.println(LayoutInflater.from(mockContext).inflate(4, mockGroup, false));
            assertTrue("OnCreateViewHolder failed to return an instance of imageHolder.",
                    ImageAdapter.ImageHolder.class.equals(testAdapter.onCreateViewHolder(mockGroup, 1).getClass()));
        }
    }

    /**
     * Simple test of this itemCount getter.
     */
    @Test
    public void testGetItemCount() {
        ArrayList<Image> imageList = new ArrayList<Image>();
        imageList.add(new Image("test/image/url"));
        ImageAdapter testAdapter = new ImageAdapter(mockContext, imageList);
        assertEquals("GetItemCount did not return the expected value of 1.",
                1, testAdapter.getItemCount());
    }
    //The other functions in this class and interior class
    //are too closely tied to the front end to effectively unit test.
    //They will be covered by higher level tests.
}

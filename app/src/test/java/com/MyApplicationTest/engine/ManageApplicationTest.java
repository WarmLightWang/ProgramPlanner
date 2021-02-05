package com.MyApplicationTest.engine;

import android.view.View;

import com.example.myapplication.activity.ViewApplicationActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Not implemented in this commit
 * Test the ManageApplication class.
 * Some parts will need to be covered with
 * higher level tests due to its integrated nature.
 */
public class ManageApplicationTest {
    @Mock
    FirebaseDatabase databaseMock;

    @Mock
    DatabaseReference referenceMock;

    @Mock
    ViewApplicationActivity activityMock;

    /**
     * Sets up the mocks for testing
     */
    @Before
    public void setUp() {
        databaseMock = Mockito.mock(FirebaseDatabase.class);
        referenceMock = Mockito.mock(DatabaseReference.class);
        activityMock = Mockito.mock(ViewApplicationActivity.class);
        when(databaseMock.getReference(anyString())).thenReturn(referenceMock);
    }
}

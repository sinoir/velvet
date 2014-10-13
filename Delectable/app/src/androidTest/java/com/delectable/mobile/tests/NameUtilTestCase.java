package com.delectable.mobile.tests;

import com.delectable.mobile.util.NameUtil;

import junit.framework.TestCase;

public class NameUtilTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetSplitNameWithNullParam() {
        String name = null;
        String[] expectedName = new String[2];
        expectedName[0] = "";
        expectedName[1] = "";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

    public void testGetSplitNameWithEmptyString() {
        String name = "";
        String[] expectedName = new String[2];
        expectedName[0] = "";
        expectedName[1] = "";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

    public void testGetSplitNameWithEmptySpacesString() {
        String name = "  ";
        String[] expectedName = new String[2];
        expectedName[0] = "";
        expectedName[1] = " ";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

    public void testGetSplitNameWithFirstName() {
        String name = "First";
        String[] expectedName = new String[2];
        expectedName[0] = "First";
        expectedName[1] = " ";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

    public void testGetSplitNameWithFirstAndLastName() {
        String name = "First Last";
        String[] expectedName = new String[2];
        expectedName[0] = "First";
        expectedName[1] = "Last";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

    public void testGetSplitNameWithFirstNameLeadingSpace() {
        String name = " First";
        String[] expectedName = new String[2];
        expectedName[0] = "First";
        expectedName[1] = " ";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

    public void testGetSplitNameWithFirstNameTrailingSpace() {
        String name = "First ";
        String[] expectedName = new String[2];
        expectedName[0] = "First";
        expectedName[1] = " ";
        String[] actualName = NameUtil.getSplitName(name);
        assertEquals(expectedName[0], actualName[0]);
        assertEquals(expectedName[1], actualName[1]);
    }

}

package com.delectable.mobile.tests;

import com.delectable.mobile.util.HelperUtil;

import junit.framework.TestCase;

/**
 * Created by abednarek on 5/21/14.
 */
public class HelperUtilTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSnakeCaseToCamelCase() {
        String snaked = "hello_camel_case";
        String expectedString = "helloCamelCase";
        String actualString = HelperUtil.snakeCaseToCamelCase(snaked);
        assertEquals(expectedString, actualString);
    }

    public void testGetterMethodNameFromFieldName() {
        String fieldName = "hello_camel_case";
        String expectedString = "getHelloCamelCase";
        String actualString = HelperUtil.getterMethodNameFromFieldName(fieldName);
        assertEquals(expectedString, actualString);
    }
}

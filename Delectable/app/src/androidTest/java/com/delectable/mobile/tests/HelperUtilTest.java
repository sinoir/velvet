package com.delectable.mobile.tests;

import com.delectable.mobile.util.HelperUtil;

import junit.framework.TestCase;

import java.util.HashMap;

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

    public void testBuildUrlWithParameters() {
        String fakeUrl = "http://foobar.com";
        String expectedUrl = fakeUrl + "?foo=bar&something=far";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("foo", "bar");
        params.put("something", "far");

        String actualUrl = HelperUtil.buildUrlWithParameters(fakeUrl, params);
        assertEquals(expectedUrl, actualUrl);
    }

    public void testBuildUrlWithNoParameters() {
        String expectedUrl = "http://foobar.com";
        HashMap<String, String> params = new HashMap<String, String>();

        String actualUrl = HelperUtil.buildUrlWithParameters(expectedUrl, params);
        assertEquals(expectedUrl, actualUrl);
    }

    public void testBuildUrlWithNullParameters() {
        String expectedUrl = "http://foobar.com";
        String actualUrl = HelperUtil.buildUrlWithParameters(expectedUrl, null);
        assertEquals(expectedUrl, actualUrl);
    }
}

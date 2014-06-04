package com.delectable.mobile.tests;

import com.delectable.mobile.util.DateHelperUtil;

import junit.framework.TestCase;

import java.sql.Date;

public class DateHelperUtilTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLongDateFromDouble() {
        double rawDoubleDate = 1398530839.6009998;
        long expectedLongDate = 1398530839600l;
        assertEquals(expectedLongDate, DateHelperUtil.longDateFromDouble(rawDoubleDate));
    }

    public void testDateFromDateDouble() {
        double rawDoubleDate = 1398530839.6009998;
        Date expectedDate = new Date(1398530839600l);
        assertEquals(expectedDate, DateHelperUtil.dateFromDouble(rawDoubleDate));
    }
}

package com.carbonfive.flash.encoder;

import java.util.*;
import junit.framework.*;
import com.carbonfive.flash.*;

public class DateEncoderTest
  extends    TestCase
{
  public DateEncoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(DateEncoderTest.class);
    return suite;
  }

  protected void setUp() throws Exception
  {
  }

  protected void tearDown() throws Exception
  {
  }

  public void testNativeEncode()
    throws Exception
  {
    DateEncoder encoder = new DateEncoder();

    Date    date    = new Date();
    Date    sqlDate = new java.sql.Date(date.getTime());

    assertTrue(encoder.encodeObject(Context.getBaseContext(), date)    instanceof Date);
    assertTrue(encoder.encodeObject(Context.getBaseContext(), sqlDate) instanceof Date);

    Date    encodedDate    = (Date)    encoder.encodeObject(Context.getBaseContext(), date);
    Date    encodedSqlDate = (Date)    encoder.encodeObject(Context.getBaseContext(), sqlDate);

    assertEquals(date,   encodedDate);
    assertEquals(date,   encodedSqlDate); // should be a java.lang.Date, not a java.sql.Date
    assertEquals(Date.class, encodedSqlDate.getClass());
  }

}
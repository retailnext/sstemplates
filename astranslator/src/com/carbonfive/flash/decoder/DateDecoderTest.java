package com.carbonfive.flash.decoder;

import java.util.*;
import junit.framework.*;

public class DateDecoderTest
  extends    TestCase
{
  public DateDecoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(DateDecoderTest.class);
    return suite;
  }

  private DateDecoder decoder = null;

  protected void setUp()
    throws Exception
  {
    decoder = new DateDecoder();
  }

  protected void tearDown()
    throws Exception
  {
    decoder = null;
  }

  public void testSqlDateDecode() throws Exception
  {
    Date date = new Date();
    Object decoded = decoder.decodeObject(date, java.sql.Date.class);
    assertTrue("Expecting java.sql.Date, got: " + decoded.getClass().getName(), decoded instanceof java.sql.Date);
    assertEquals(new java.sql.Date(date.getTime()), decoded);
  }
}
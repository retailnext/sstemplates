package com.carbonfive.flash.encoder;

import java.util.*;
import junit.framework.*;
import com.carbonfive.flash.*;

public class NativeEncoderTest
  extends    TestCase
{
  public NativeEncoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(NativeEncoderTest.class);
    return suite;
  }

  private EncoderFactory factory = null;

  protected void setUp()
    throws Exception
  {
    factory =  EncoderFactory.getInstance();
  }

  protected void tearDown()
    throws Exception
  {
    factory = null;
  }

  public void testNativeEncode()
    throws Exception
  {
    NativeEncoder encoder = new NativeEncoder();

    Date    date    = new Date();
    Date    sqlDate = new java.sql.Date(date.getTime());
    String  string  = "hello";
    Boolean bool    = Boolean.TRUE;

    assertTrue(encoder.encodeObject(Context.getBaseContext(), date)    instanceof Date);
    assertTrue(encoder.encodeObject(Context.getBaseContext(), sqlDate) instanceof Date);
    assertTrue(encoder.encodeObject(Context.getBaseContext(), string)  instanceof String);
    assertTrue(encoder.encodeObject(Context.getBaseContext(), bool)    instanceof Boolean);

    Date    encodedDate    = (Date)    encoder.encodeObject(Context.getBaseContext(), date);
    Date    encodedSqlDate = (Date)    encoder.encodeObject(Context.getBaseContext(), sqlDate);
    String  encodedString  = (String)  encoder.encodeObject(Context.getBaseContext(), string);
    Boolean encodedBool    = (Boolean) encoder.encodeObject(Context.getBaseContext(), bool);

    assertEquals(date,   encodedDate);
    assertEquals(date,   encodedSqlDate); // should be a java.lang.Date, not a java.sql.Date
    assertEquals(Date.class, encodedSqlDate.getClass());
    assertEquals(string, encodedString);
    assertEquals(bool,   encodedBool);
  }

}
package com.carbonfive.flash;

import java.util.*;
import junit.framework.*;
import org.w3c.dom.*;

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

    Date    date   = new Date();
    String  string = "hello";
    Boolean bool   = Boolean.TRUE;

    assertTrue(encoder.encodeObject(date)   instanceof Date);
    assertTrue(encoder.encodeObject(string) instanceof String);
    assertTrue(encoder.encodeObject(bool)   instanceof Boolean);

    Date    encodedDate   = (Date)    encoder.encodeObject(date);
    String  encodedString = (String)  encoder.encodeObject(string);
    Boolean encodedBool   = (Boolean) encoder.encodeObject(bool);

    assertEquals(date,   encodedDate);
    assertEquals(string, encodedString);
    assertEquals(bool,   encodedBool);
  }

}
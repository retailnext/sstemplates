package com.carbonfive.flash.encoder;

import java.util.*;
import junit.framework.*;
import com.carbonfive.flash.encoder.*;

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

    assertTrue(encoder.encodeObject(encoder.encodeShell(date), date)     instanceof Date);
    assertTrue(encoder.encodeObject(encoder.encodeShell(string), string) instanceof String);
    assertTrue(encoder.encodeObject(encoder.encodeShell(bool), bool)     instanceof Boolean);

    Date    encodedDate   = (Date)    encoder.encodeObject(encoder.encodeShell(date), date);
    String  encodedString = (String)  encoder.encodeObject(encoder.encodeShell(string), string);
    Boolean encodedBool   = (Boolean) encoder.encodeObject(encoder.encodeShell(bool), bool);

    assertEquals(date,   encodedDate);
    assertEquals(string, encodedString);
    assertEquals(bool,   encodedBool);
  }

}
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

  protected void setUp() throws Exception
  {
  }

  protected void tearDown() throws Exception
  {
  }

  public void testNativeEncode()
    throws Exception
  {
    NativeEncoder encoder = new NativeEncoder();

    String  string  = "hello";
    Boolean bool    = Boolean.TRUE;

    assertTrue(encoder.encodeObject(Context.getBaseContext(), string)  instanceof String);
    assertTrue(encoder.encodeObject(Context.getBaseContext(), bool)    instanceof Boolean);

    String  encodedString  = (String)  encoder.encodeObject(Context.getBaseContext(), string);
    Boolean encodedBool    = (Boolean) encoder.encodeObject(Context.getBaseContext(), bool);

    assertEquals(string, encodedString);
    assertEquals(bool,   encodedBool);
  }

}
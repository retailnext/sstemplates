package com.carbonfive.flash.decoder;

import java.util.*;
import junit.framework.*;

public class NativeDecoderTest
  extends    TestCase
{
  public NativeDecoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(NativeDecoderTest.class);
    return suite;
  }

  private DecoderFactory factory = null;

  protected void setUp()
    throws Exception
  {
    factory =  DecoderFactory.getInstance();
  }

  protected void tearDown()
    throws Exception
  {
    factory = null;
  }

  public void testNativeDecode()
    throws Exception
  {
    NativeDecoder decoder = new NativeDecoder();

    Date    date   = new Date();
    String  string = "hello";
    Boolean bool   = Boolean.TRUE;

    assertTrue(decoder.decodeObject(date,   Date.class)    instanceof Date);
    assertTrue(decoder.decodeObject(string, String.class)  instanceof String);
    assertTrue(decoder.decodeObject(bool,   Boolean.class) instanceof Boolean);

    Date    decodedDate   = (Date)    decoder.decodeObject(date,   Date.class);
    String  decodedString = (String)  decoder.decodeObject(string, String.class);
    Boolean decodedBool   = (Boolean) decoder.decodeObject(bool,   Boolean.class);

    assertEquals(date,   decodedDate);
    assertEquals(string, decodedString);
    assertEquals(bool,   decodedBool);
  }

  public void testNativeDecodeConversion() throws Exception
  {
    NativeDecoder decoder = new NativeDecoder();

    assertTrue(decoder.decodeObject("true", Boolean.class) instanceof Boolean);
    assertTrue(decoder.decodeObject("10",   Integer.class) instanceof Integer);
    assertTrue(decoder.decodeObject("10",   Float.class)   instanceof Float);
    assertTrue(decoder.decodeObject("10",   Double.class)  instanceof Double);

    assertEquals(Boolean.TRUE,    decoder.decodeObject("true", Boolean.class));
    assertEquals(new Integer(10), decoder.decodeObject("10",   Integer.class));
    assertEquals(new Float(10),   decoder.decodeObject("10",   Float.class));
    assertEquals(new Double(10),  decoder.decodeObject("10",   Double.class));
  }

  public void testNativeDecoderConversionError() throws Exception
  {
    NativeDecoder decoder = new NativeDecoder();

    try
    {
      decoder.decodeObject("mike", Integer.class);
      fail("Should have thrown NumberFormatException");
    }
    catch (NumberFormatException nfe)
    {
      // good
    }
  }
}
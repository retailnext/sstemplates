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
}
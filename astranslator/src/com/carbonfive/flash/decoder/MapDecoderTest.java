package com.carbonfive.flash.decoder;

import java.util.*;
import junit.framework.*;
import flashgateway.io.*;

public class MapDecoderTest
  extends    TestCase
{

  /**
   * This contructor provides a new MapDecoderTest.
   * </p>
   * @param name The String needed to build this object
   */
  public MapDecoderTest(String name)
  {
    super(name);
  }

  /**
   * Builds the test suite using introspection.
   * </p>
   * @return Test - The Test to be returned
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite(MapDecoderTest.class);
    return suite;
  }

  protected void setUp()
    throws Exception
  {
  }

  protected void tearDown()
    throws Exception
  {
  }

  public void testDecodeObject()
    throws Exception
  {
    HashMap map = new HashMap();
    map.put("one", "one");
    map.put("two", "two");
    
    MapDecoder decoder = new MapDecoder();
    assertTrue(decoder.decodeObject(map, Map.class)       instanceof HashMap);
    assertTrue(decoder.decodeObject(map, HashMap.class)   instanceof HashMap);
    assertTrue(decoder.decodeObject(map, Hashtable.class) instanceof Hashtable);
  }

  public void testDecodeNullValue() throws Exception
  {
    ASObject aso = new ASObject();
    aso.put("one", "one");
    aso.put("two", null);

    MapDecoder decoder = new MapDecoder();
    Map decoded = (Map) decoder.decodeObject(aso, Map.class);
    assertNotNull(decoded);
    assertEquals("one", decoded.get("one"));
    assertNull(decoded.get("two"));
  }

}

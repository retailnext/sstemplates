package com.carbonfive.flash;

import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.dom.*;
import junit.framework.*;
import flashgateway.io.ASObject;

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

}

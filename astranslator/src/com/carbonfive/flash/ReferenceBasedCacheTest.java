package com.carbonfive.flash;

import java.util.*;
import junit.framework.*;

public class ReferenceBasedCacheTest
  extends    TestCase
{

  public ReferenceBasedCacheTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(ReferenceBasedCacheTest.class);
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

  public void testCacheWorks()
    throws Exception
  {
    ReferenceBasedCache cache = new ReferenceBasedCache();

    // these object should be equal() but not ==
    Date obj1 = new Date();
    Date obj2 = new Date(obj1.getTime());

    assertTrue(obj1.equals(obj2));
    assertTrue(! (obj1 == obj2));

    cache.put(obj1, "obj1");
    cache.put(obj2, "obj2");

    assertEquals(2, cache.size());
    assertTrue(cache.containsKey(obj1));
    assertTrue(cache.containsKey(obj2));
    assertEquals("obj1", cache.get(obj1));
    assertEquals("obj2", cache.get(obj2));
  }
}

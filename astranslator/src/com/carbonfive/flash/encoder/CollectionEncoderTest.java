package com.carbonfive.flash.encoder;

import java.util.*;
import junit.framework.*;
import com.carbonfive.flash.*;

public class CollectionEncoderTest
  extends    TestCase
{
  public CollectionEncoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(CollectionEncoderTest.class);
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

  public void testNullEntry()
  {
    Collection java = new ArrayList();
    java.add("1");
    java.add(null);

    CollectionEncoder encoder = new CollectionEncoder();
    Object encoded = encoder.encodeObject(Context.getBaseContext(), java);

    assertTrue(encoded instanceof ArrayList);

    ArrayList as = (ArrayList) encoded;
    assertEquals("1", as.get(0));
    assertNull(as.get(1));
  }
}
package com.carbonfive.flash.decoder;

import java.util.*;
import junit.framework.*;

public class CollectionDecoderTest
  extends    TestCase
{
  public CollectionDecoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(CollectionDecoderTest.class);
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
    ArrayList list = new ArrayList();
    list.add("one");
    list.add("two");
    
    CollectionDecoder decoder = new CollectionDecoder();
    assertTrue(decoder.decodeObject(list, List.class)       instanceof ArrayList);
    assertTrue(decoder.decodeObject(list, ArrayList.class)  instanceof ArrayList);
    assertTrue(decoder.decodeObject(list, Set.class)        instanceof HashSet);
    assertTrue(decoder.decodeObject(list, HashSet.class)    instanceof HashSet);
    assertTrue(decoder.decodeObject(list, Collection.class) instanceof ArrayList);
  }

  public void testDecodeIntegerCollection()
    throws Exception
  {
    List list = new ArrayList();
    list.add(new Double(1));
    list.add(new Double(2));
    CollectionDecoder decoder = new CollectionDecoder();
    List list2 = (List) decoder.decodeObject(list, Collection.class);
    assertTrue(list2.get(0) instanceof Integer);
    assertTrue(list2.get(1) instanceof Integer);
    assertEquals(Arrays.asList(new Integer[] { new Integer(1), new Integer(2) }), list2);

    list = new ArrayList();
    list.add(new Double(1.5));
    list.add(new Double(2));
    list2 = (List) decoder.decodeObject(list, Collection.class);
    assertTrue(list2.get(0) instanceof Double);
    assertTrue(list2.get(1) instanceof Double);
    assertEquals(Arrays.asList(new Double[] { new Double(1.5), new Double(2) }), list2);
  }

  public void testNullEntry() throws Exception
  {
    List list = new ArrayList();
    list.add("1");
    list.add(null);

    CollectionDecoder decoder = new CollectionDecoder();
    List list2 = (List) decoder.decodeObject(list, Collection.class);
    assertTrue(list2.get(0) instanceof String);
    assertEquals("1", list2.get(0));
    assertNull(list2.get(1));
  }
}

package com.carbonfive.flash.decoder;

import java.util.*;
import junit.framework.*;

public class ArrayDecoderTest
  extends    TestCase
{
  public ArrayDecoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(ArrayDecoderTest.class);
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

  public void testArrayListToObjectArrayDecode()
  {
    ArrayList list = new ArrayList();
    Object obj1 = new Object();
    Object obj2 = new Object();
    list.add(obj1);
    list.add(obj2);

    Class arrayClass = new Object[2].getClass();

    ActionScriptDecoder decoder = factory.getDecoder(list, arrayClass);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(list, arrayClass);
    assertTrue(decodedObject instanceof Object[]);

    Object[] array = (Object[]) decodedObject;
    assertNotNull(array);
    assertEquals(2, array.length);
    assertEquals(obj1, array[0]);
    assertEquals(obj2, array[1]);
  }

  public void testArrayListToIntegerArrayDecode()
  {
    ArrayList list = new ArrayList();
    Integer int1 = new Integer(1);
    Integer int2 = new Integer(2);
    Double  dbl1 = new Double(int1.intValue());
    Double  dbl2 = new Double(int2.intValue());
    list.add(dbl1);
    list.add(dbl2);

    Class arrayClass = new Integer[2].getClass();

    ActionScriptDecoder decoder = factory.getDecoder(list, arrayClass);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(list, arrayClass);
    assertTrue(decodedObject instanceof Integer[]);

    Integer[] array = (Integer[]) decodedObject;
    assertNotNull(array);
    assertEquals(2, array.length);
    assertEquals(int1, array[0]);
    assertEquals(int2, array[1]);
  }

  public void testArrayListToIntArrayDecode()
  {
    ArrayList list = new ArrayList();
    Double  dbl1 = new Double(1);
    Double  dbl2 = new Double(2);
    list.add(dbl1);
    list.add(dbl2);

    Class arrayClass = new int[2].getClass();

    ActionScriptDecoder decoder = factory.getDecoder(list, arrayClass);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(list, arrayClass);
    assertTrue(decodedObject instanceof int[]);

    int[] array = (int[]) decodedObject;
    assertNotNull(array);
    assertEquals(2, array.length);
    assertEquals(1, array[0]);
    assertEquals(2, array[1]);
  }

  public void testNullEntry() throws Exception
  {
    List list = new ArrayList();
    list.add("1");
    list.add(null);

    ArrayDecoder decoder = new ArrayDecoder();
    Object decoded = decoder.decodeObject(list, new String[2].getClass());

    assertTrue(decoded instanceof String[]);

    String[] array = (String[]) decoded;
    assertEquals("1", array[0]);
    assertNull(array[1]);
  }
}

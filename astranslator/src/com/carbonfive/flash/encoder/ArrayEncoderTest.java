package com.carbonfive.flash.encoder;

import java.util.*;
import junit.framework.*;
import com.carbonfive.flash.encoder.*;

public class ArrayEncoderTest
  extends    TestCase
{
  public ArrayEncoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(ArrayEncoderTest.class);
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

  public void testObjectArrayToArrayListEncode()
  {
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object[] array = new Object[] { obj1, obj2 };

    ActionScriptEncoder encoder = factory.getEncoder(array);
    assertNotNull(encoder);

    Object encodedObject = encoder.encodeObject(encoder.encodeShell(array), array);
    assertNotNull(encodedObject);
    assertTrue(encodedObject instanceof ArrayList);

    ArrayList list = (ArrayList) encodedObject;
    assertEquals(2, list.size());
    assertEquals(obj1, list.get(0));
    assertEquals(obj2, list.get(1));
  }

  public void testIntegerArrayToArrayListEncode()
  {
    Integer int1 = new Integer(1);
    Integer int2 = new Integer(2);
    Integer[] array = new Integer[] { int1, int2 };
    Double  dbl1 = new Double(int1.intValue());
    Double  dbl2 = new Double(int2.intValue());

    ActionScriptEncoder encoder = factory.getEncoder(array);
    assertNotNull(encoder);

    Object encodedObject = encoder.encodeObject(encoder.encodeShell(array), array);
    assertNotNull(encodedObject);
    assertTrue(encodedObject instanceof ArrayList);

    ArrayList list = (ArrayList) encodedObject;
    assertEquals(2, list.size());
    assertTrue(list.get(0) instanceof Double);
    assertTrue(list.get(1) instanceof Double);
    assertEquals(dbl1, list.get(0));
    assertEquals(dbl2, list.get(1));
  }
}
package com.carbonfive.flash.decoder;

import java.math.*;
import junit.framework.*;

public class NumberDecoderTest
  extends    TestCase
{
  public NumberDecoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(NumberDecoderTest.class);
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

  public void testNumberDecode()
    throws Exception
  {
    assertDecodable(Byte.class);
    assertDecodable(Byte.TYPE);
    assertDecodable(Short.class);
    assertDecodable(Short.TYPE);
    assertDecodable(Integer.class);
    assertDecodable(Integer.TYPE);
    assertDecodable(Long.class);
    assertDecodable(Long.TYPE);
    assertDecodable(Float.class);
    assertDecodable(Float.TYPE);
    assertDecodable(Double.class);
    assertDecodable(Double.TYPE);
    assertDecodable(BigDecimal.class);
  }

  private static final double DELTA = 0.000001;
  private void assertDecodable(Class desiredClass)
    throws Exception
  {
    NumberDecoder decoder       = new NumberDecoder();
    Double        encodedNumber = new Double(2.0);
    Object        decodedNumber = decoder.decodeObject(encodedNumber, desiredClass);
    Class         realDesiredClass = ( desiredClass.isPrimitive()    ?
                                       getNonPrimative(desiredClass) :
                                       desiredClass );

    assertNotNull(decodedNumber);
    assertEquals(realDesiredClass, decodedNumber.getClass());
    assertEquals(encodedNumber.doubleValue(), ((Number) decodedNumber).doubleValue(), DELTA);
  }

  static final Class getNonPrimative(Class primitiveClass)
  {
    if      (Byte.TYPE.equals(primitiveClass))    return Byte.class;
    else if (Short.TYPE.equals(primitiveClass))   return Short.class;
    else if (Integer.TYPE.equals(primitiveClass)) return Integer.class;
    else if (Long.TYPE.equals(primitiveClass))    return Long.class;
    else if (Float.TYPE.equals(primitiveClass))   return Float.class;
    else if (Double.TYPE.equals(primitiveClass))  return Double.class;
    else return null;
  }
}
package com.carbonfive.flash.encoder;

import java.util.*;
import java.math.*;
import junit.framework.*;
import com.carbonfive.flash.encoder.*;

public class NumberEncoderTest
  extends    TestCase
{
  public NumberEncoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(NumberEncoderTest.class);
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

  public void testNumberEncode()
    throws Exception
  {
    assertEncodable(new Byte((byte) 2));
    assertEncodable(new Short((short) 2));
    assertEncodable(new Integer(2));
    assertEncodable(new Long(2));
    assertEncodable(new Float(2.0));
    assertEncodable(new Double(2.0));
    assertEncodable(new BigDecimal(2.0));
  }

  private static final double DELTA = 0.000001;
  private void assertEncodable(Number decodedNumber)
    throws Exception
  {
    NumberEncoder encoder = new NumberEncoder();

    Object encodedNumber = encoder.encodeObject(encoder.encodeShell(decodedNumber), decodedNumber);

    assertNotNull(encodedNumber);
    assertEquals(Double.class, encodedNumber.getClass());
    assertEquals(decodedNumber.doubleValue(), ((Double) encodedNumber).doubleValue(), DELTA);
  }

}
package com.carbonfive.flash.encoder;

import com.carbonfive.flash.*;
import com.carbonfive.flash.test.*;
import junit.framework.*;

import java.math.*;
import java.util.*;

public class EncoderFactoryTest
  extends    TestCase
{
  public EncoderFactoryTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(EncoderFactoryTest.class);
    return suite;
  }

  protected void setUp() throws Exception
  {
  }

  protected void tearDown() throws Exception
  {
  }

  public void testCreateNumberEncoder() throws Exception
  {
    validate(new Byte((byte) 1), NumberEncoder.class);
    validate(new Short((short) 1), NumberEncoder.class);
    validate(new Integer(1), NumberEncoder.class);
    validate(new Long(1), NumberEncoder.class);
    validate(new Float(1.0), NumberEncoder.class);
    validate(new Double(1.0), NumberEncoder.class);
    validate(new BigDecimal(1.0), NumberEncoder.class);
  }

  public void testCreateDateEncoder() throws Exception
  {
    validate(new Date(), DateEncoder.class);
    validate(new java.sql.Date(new Date().getTime()), DateEncoder.class);
  }

  public void testCreateArrayEncoder() throws Exception
  {
    validate(new Object[1], ArrayEncoder.class);
  }

  public void testCreateCollectionEncoder() throws Exception
  {
    validate(new ArrayList(), CollectionEncoder.class);
    validate(new HashSet(), CollectionEncoder.class);
  }

  public void testCreateMapEncoder() throws Exception
  {
    validate(new HashMap(), MapEncoder.class);
    validate(new Hashtable(), MapEncoder.class);
  }

  public void testCreateJavaBeanEncoder() throws Exception
  {
    validate(TestBean.getTestBean(), JavaBeanEncoder.class);
  }

  public void testInstanceOf() throws Exception
  {
    assertTrue(EncoderFactory.instanceOf(String.class, "java.lang.String"));
    assertTrue(EncoderFactory.instanceOf(Integer.class, "java.lang.Number"));
    assertTrue(! EncoderFactory.instanceOf(String.class, "java.lang.Number"));
  }

  public void testSerializable() throws Exception
  {
    validate(this.getClass().getMethod("testSerializable", null), NullEncoder.class);
  }

  private void validate(Object obj, Class encoderClass)
  {
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(Context.getBaseContext(), obj);
    assertNotNull(encoder);
    if (encoder instanceof CachingEncoder)
    {
      String msg = "Encoding " + obj.getClass().getName() + " does not use a " + encoderClass.getName() +
                   ".  It uses a " + ((CachingEncoder) encoder).getNextEncoder().getClass().getName();
      assertTrue(msg, encoderClass.isAssignableFrom(((CachingEncoder) encoder).getNextEncoder().getClass()));
    }
    else
    {
      String msg = "Encoding " + obj.getClass().getName() + " does not use a " + encoderClass.getName() +
                   ".  It uses a " + encoder.getClass().getName();
      assertTrue(msg, encoderClass.isAssignableFrom(encoder.getClass()));
    }
  }
}
package com.carbonfive.flash.encoder;

import java.util.*;
import java.math.*;
import junit.framework.*;
import com.carbonfive.flash.encoder.*;
import com.carbonfive.flash.*;

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

  protected void setUp()
    throws Exception
  {
  }

  protected void tearDown()
    throws Exception
  {
  }

  public void testCreateNumberEncoder()
    throws Exception
  {
    validate(new Byte((byte) 1), NumberEncoder.class);
    validate(new Short((short) 1), NumberEncoder.class);
    validate(new Integer(1), NumberEncoder.class);
    validate(new Long(1), NumberEncoder.class);
    validate(new Float(1.0), NumberEncoder.class);
    validate(new Double(1.0), NumberEncoder.class);
    validate(new BigDecimal(1.0), NumberEncoder.class);
  }

  public void testCreateArrayEncoder()
    throws Exception
  {
    validate(new Object[1], ArrayEncoder.class);
  }

  public void testCreateCollectionEncoder()
    throws Exception
  {
    validate(new ArrayList(), CollectionEncoder.class);
    validate(new HashSet(), CollectionEncoder.class);
  }

  public void testCreateMapEncoder()
    throws Exception
  {
    validate(new HashMap(), MapEncoder.class);
    validate(new Hashtable(), MapEncoder.class);
  }

  public void testCreateJavaBeanEncoder()
    throws Exception
  {
    validate(TestBean.getTestBean(), JavaBeanEncoder.class);
  }

  private void validate(Object obj, Class encoderClass)
  {
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(obj);
    assertNotNull(encoder);
    if (encoder instanceof CachingEncoder)
    {
      String msg = "Encoding " + obj.getClass().getName() + " does not use a " + encoderClass.getName() +
                   ".  It uses a " + ((CachingEncoder) encoder).getNextEncoder().getClass().getName();
      assertTrue(encoderClass.isAssignableFrom(((CachingEncoder) encoder).getNextEncoder().getClass()));
    }
    else
    {
      String msg = "Encoding " + obj.getClass().getName() + " does not use a " + encoderClass.getName() +
                   ".  It uses a " + encoder.getClass().getName();
      assertTrue(encoderClass.isAssignableFrom(encoder.getClass()));
    }
  }
}
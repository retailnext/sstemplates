package com.carbonfive.flash.decoder;

import java.util.*;
import java.math.*;
import junit.framework.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.test.*;

public class DecoderFactoryTest
  extends    TestCase
{
  public DecoderFactoryTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(DecoderFactoryTest.class);
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

  public void testCreateNumberTranslator()
    throws Exception
  {
    validate(new Double(1.0), Byte.class, NumberDecoder.class);
    validate(new Double(1.0), Short.class, NumberDecoder.class);
    validate(new Double(1.0), Integer.class, NumberDecoder.class);
    validate(new Double(1.0), Long.class, NumberDecoder.class);
    validate(new Double(1.0), Float.class, NumberDecoder.class);
    validate(new Double(1.0), Double.class, NumberDecoder.class);
    validate(new Double(1.0), BigDecimal.class, NumberDecoder.class);
  }

  public void testCreateArrayDecoder()
    throws Exception
  {
    validate(new ArrayList(), new Object[1].getClass(), ArrayDecoder.class);
  }

  public void testCreateCollectionDecoder()
    throws Exception
  {
    validate(new ArrayList(), ArrayList.class, CollectionDecoder.class);
    validate(new ArrayList(), List.class, CollectionDecoder.class);
    validate(new ArrayList(), Set.class, CollectionDecoder.class);
    validate(new ArrayList(), HashSet.class, CollectionDecoder.class);
  }

  public void testCreateMapDecoder()
    throws Exception
  {
    validate(new ASObject(), HashMap.class, MapDecoder.class);
    validate(new ASObject(), Map.class, MapDecoder.class);
    validate(new ASObject(), Hashtable.class, MapDecoder.class);
  }

  public void testCreateJavaBeanDecoder()
    throws Exception
  {
    ASObject aso = new ASObject();
    aso.setType(TestBean.class.getName());

    validate(aso, com.carbonfive.flash.test.TestBean.class, JavaBeanDecoder.class);
  }

  private void validate(Object obj, Class desiredClass, Class decoderClass)
  {
    ActionScriptDecoder decoder = DecoderFactory.getInstance().getDecoder(obj, desiredClass);
    assertNotNull(decoder);
    if (decoder instanceof CachingDecoder)
    {
      String msg = "Decoding " + obj.getClass().getName() + " -> " + desiredClass.getName() + " does not use a " + decoderClass.getName() +
                   ".  It uses a " + ((CachingDecoder) decoder).getNextDecoder().getClass().getName();
      assertTrue(msg, decoderClass.isAssignableFrom(((CachingDecoder) decoder).getNextDecoder().getClass()));
    }
    else
    {
      String msg = "Decoding " + obj.getClass().getName() + " -> " + desiredClass.getName() + " does not use a " + decoderClass.getName() +
                   ".  It uses a " + decoder.getClass().getName();
      assertTrue(msg, decoderClass.isAssignableFrom(decoder.getClass()));
    }
  }
}
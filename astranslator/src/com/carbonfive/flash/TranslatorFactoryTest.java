package com.carbonfive.flash;

import java.util.*;
import java.math.*;
import junit.framework.*;
import flashgateway.io.ASObject;

public class TranslatorFactoryTest
  extends    TestCase
{
  /**
   * This contructor provides a new TranslatorFactoryTest.
   * </p>
   * @param name The String needed to build this object
   */
  public TranslatorFactoryTest(String name)
  {
    super(name);
  }

  /**
   * Builds the test suite using introspection.
   * </p>
   * @return Test - The Test to be returned
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite(TranslatorFactoryTest.class);
    return suite;
  }

  ASTranslator      asTrans  = null;
  TranslatorFactory factory  = null;

  protected void setUp()
    throws Exception
  {
    asTrans  = new ASTranslator();
    factory  = TranslatorFactory.getInstance();
  }

  protected void tearDown()
    throws Exception
  {
    asTrans = null;
    factory = null;
  }

  public void testCreateNumberTranslator()
    throws Exception
  {
    validateTo(new Byte((byte) 1), NumberTranslator.class);
    validateTo(new Short((short) 1), NumberTranslator.class);
    validateTo(new Integer(1), NumberTranslator.class);
    validateTo(new Long(1), NumberTranslator.class);
    validateTo(new Float(1.0), NumberTranslator.class);
    validateTo(new Double(1.0), NumberTranslator.class);
    validateTo(new BigDecimal(1.0), NumberTranslator.class);

    validateFrom(new Double(1.0), Byte.class, NumberTranslator.class);
    validateFrom(new Double(1.0), Short.class, NumberTranslator.class);
    validateFrom(new Double(1.0), Integer.class, NumberTranslator.class);
    validateFrom(new Double(1.0), Long.class, NumberTranslator.class);
    validateFrom(new Double(1.0), Float.class, NumberTranslator.class);
    validateFrom(new Double(1.0), Double.class, NumberTranslator.class);
    validateFrom(new Double(1.0), BigDecimal.class, NumberTranslator.class);
  }

  public void testCreateArrayTranslator()
    throws Exception
  {
    validateTo(new Object[1], ArrayTranslator.class);

    validateFrom(new ArrayList(), new Object[1].getClass(), ArrayTranslator.class);
  }

  public void testCreateCollectionTranslator()
    throws Exception
  {
    validateTo(new ArrayList(), CollectionTranslator.class);
    validateTo(new HashSet(), CollectionTranslator.class);

    validateFrom(new ArrayList(), ArrayList.class, CollectionTranslator.class);
    validateFrom(new ArrayList(), HashSet.class, CollectionTranslator.class);
  }

  public void testCreateMapTranslator()
    throws Exception
  {
    validateTo(new HashMap(), MapTranslator.class);
    validateTo(new Hashtable(), MapTranslator.class);

    validateFrom(new ASObject(), HashMap.class, MapTranslator.class);
    validateFrom(new ASObject(), Hashtable.class, MapTranslator.class);
  }

  public void testCreateJavaBeanTranslator()
    throws Exception
  {
    validateTo(ASTranslatorTest.getTestBean(), JavaBeanTranslator.class);

    ASObject aso = new ASObject();
    aso.setType(ASTranslatorTest.TestBean.class.getName());

    validateFrom(aso, ASTranslatorTest.TestBean.class, JavaBeanTranslator.class);
  }

  private void validateTo(Object obj, Class translatorClass)
  {
    Translator trans = factory.getTranslatorToActionScript(asTrans, obj);
    assertNotNull(trans);
    String msg = "Mapping (to AS) " + obj.getClass().getName() + " does not use a " + translatorClass.getName() +
                 ".  It uses a " + trans.getClass().getName();
    assertTrue(translatorClass.isAssignableFrom(trans.getClass()));
  }

  private void validateFrom(Object obj, Class desiredClass, Class translatorClass)
  {
    Translator trans = factory.getTranslatorFromActionScript(asTrans, obj, desiredClass);
    assertNotNull(trans);
    String msg = "Mapping (from AS) " + obj.getClass().getName() + " -> " + desiredClass.getName() + " does not use a " + translatorClass.getName() + 
                 ".  It uses a " + trans.getClass().getName();
    assertTrue(msg, translatorClass.isAssignableFrom(trans.getClass()));
  }
}


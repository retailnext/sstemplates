package com.carbonfive.flash.encoder;

import junit.framework.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.encoder.*;
import com.carbonfive.flash.*;
import com.carbonfive.flash.test.*;

public class JavaBeanEncoderTest
  extends    TestCase
{
  public JavaBeanEncoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(JavaBeanEncoderTest.class);
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

  private static final double DELTA = 0.00001;

  public void testJavaBeanDecode()
    throws Exception
  {
    com.carbonfive.flash.test.TestBean bean = com.carbonfive.flash.test.TestBean.getTestBean(); // gets a filled in TestBean

    ActionScriptEncoder encoder = factory.getEncoder(Context.getBaseContext(), bean);
    assertNotNull(encoder);

    Object encodedObject = encoder.encodeObject(Context.getBaseContext(), bean);
    assertNotNull(encodedObject);
    assertTrue(encodedObject instanceof ASObject);

    ASObject aso = (ASObject) encodedObject;

    assertTrue(aso.get("intField") instanceof Double);
    assertTrue(aso.get("shortField") instanceof Double);
    assertTrue(aso.get("longField") instanceof Double);
    assertTrue(aso.get("doubleField") instanceof Double);
    assertTrue(aso.get("strField") instanceof String);

    assertEquals(bean.getIntField(), ((Double) aso.get("intField")).intValue());
    assertEquals(bean.getShortField(), ((Double) aso.get("shortField")).shortValue());
    assertEquals(bean.getLongField(), ((Double) aso.get("longField")).longValue());
    assertEquals(bean.getDoubleField(), ((Double) aso.get("doubleField")).doubleValue(), DELTA);
    assertEquals(bean.getStrField(), aso.get("strField"));
  }

  public void testNoObjectProperties() throws Exception
  {
    com.carbonfive.flash.test.TestBean bean = TestBean.getTestBean();

    ActionScriptEncoder encoder = factory.getEncoder(Context.getBaseContext(), bean);
    assertNotNull(encoder);

    Object encodedObject = encoder.encodeObject(Context.getBaseContext(), bean);
    assertNotNull(encodedObject);
    assertTrue(encodedObject instanceof ASObject);

    ASObject aso = (ASObject) encodedObject;

    assertTrue(! aso.containsKey("class"));
  }
}
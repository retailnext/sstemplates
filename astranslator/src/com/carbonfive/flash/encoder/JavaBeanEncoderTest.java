package com.carbonfive.flash.encoder;

import junit.framework.*;
import org.w3c.dom.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.encoder.*;
import com.carbonfive.flash.*;

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
    TestBean bean = TestBean.getTestBean(); // gets a filled in TestBean

    ActionScriptEncoder encoder = factory.getEncoder(bean);
    assertNotNull(encoder);

    Object encodedObject = encoder.encodeObject(encoder.encodeShell(bean), bean);
    assertNotNull(encodedObject);
    assertTrue(encodedObject instanceof ASObject);

    ASObject aso = (ASObject) encodedObject;

    assertTrue(aso.get("intField") instanceof Double);
    assertTrue(aso.get("shortField") instanceof Double);
    assertTrue(aso.get("longField") instanceof Double);
    assertTrue(aso.get("doubleField") instanceof Double);
    assertTrue(aso.get("strField") instanceof String);
    assertTrue(aso.get("xmlField") instanceof Document);

    assertEquals(bean.getIntField(), ((Double) aso.get("intField")).intValue());
    assertEquals(bean.getShortField(), ((Double) aso.get("shortField")).shortValue());
    assertEquals(bean.getLongField(), ((Double) aso.get("longField")).longValue());
    assertEquals(bean.getDoubleField(), ((Double) aso.get("doubleField")).doubleValue(), DELTA);
    assertEquals(bean.getStrField(), (String) aso.get("strField"));
    assertEquals(bean.getXmlField(), (Document) aso.get("xmlField"));
  }

}
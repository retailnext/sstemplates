package com.carbonfive.flash.decoder;

import junit.framework.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.test.*;

public class JavaBeanDecoderTest
  extends    TestCase
{
  public JavaBeanDecoderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(JavaBeanDecoderTest.class);
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

  private static final double DELTA = 0.00001;

  public void testJavaBeanDecode()
    throws Exception
  {
    TestBean bean = com.carbonfive.flash.test.TestBean.getTestBean(); // gets a filled in TestBean

    ASObject aso = new ASObject();
    aso.setType(com.carbonfive.flash.test.TestBean.class.getName());
    aso.put("intField", new Double(bean.getIntField()));
    aso.put("shortField", new Double(bean.getShortField()));
    aso.put("longField", new Double(bean.getLongField()));
    aso.put("doubleField", new Double(bean.getDoubleField()));
    aso.put("strField", bean.getStrField());

    ActionScriptDecoder decoder = factory.getDecoder(aso, com.carbonfive.flash.test.TestBean.class);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(aso, com.carbonfive.flash.test.TestBean.class);
    assertNotNull(decodedObject);
    assertTrue(decodedObject instanceof com.carbonfive.flash.test.TestBean);

    com.carbonfive.flash.test.TestBean decodedBean = (com.carbonfive.flash.test.TestBean) decodedObject;
    assertEquals(bean.getIntField(), decodedBean.getIntField());
    assertEquals(bean.getShortField(), decodedBean.getShortField());
    assertEquals(bean.getLongField(), decodedBean.getLongField());
    assertEquals(bean.getDoubleField(), decodedBean.getDoubleField(), DELTA);
    assertEquals(bean.getStrField(), decodedBean.getStrField());
  }
}
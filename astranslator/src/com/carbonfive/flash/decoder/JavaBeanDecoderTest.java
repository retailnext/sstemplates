package com.carbonfive.flash.decoder;

import junit.framework.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.*;

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
    TestBean bean = TestBean.getTestBean(); // gets a filled in TestBean

    ASObject aso = new ASObject();
    aso.setType(TestBean.class.getName());
    aso.put("intField", new Double(bean.getIntField()));
    aso.put("shortField", new Double(bean.getShortField()));
    aso.put("longField", new Double(bean.getLongField()));
    aso.put("doubleField", new Double(bean.getDoubleField()));
    aso.put("strField", bean.getStrField());
    aso.put("xmlField", bean.getXmlField());

    ActionScriptDecoder decoder = factory.getDecoder(aso, TestBean.class);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(aso, TestBean.class);
    assertNotNull(decodedObject);
    assertTrue(decodedObject instanceof TestBean);

    TestBean decodedBean = (TestBean) decodedObject;
    assertEquals(bean.getIntField(), decodedBean.getIntField());
    assertEquals(bean.getShortField(), decodedBean.getShortField());
    assertEquals(bean.getLongField(), decodedBean.getLongField());
    assertEquals(bean.getDoubleField(), decodedBean.getDoubleField(), DELTA);
    assertEquals(bean.getStrField(), decodedBean.getStrField());
    assertEquals(bean.getXmlField(), decodedBean.getXmlField());
  }
}
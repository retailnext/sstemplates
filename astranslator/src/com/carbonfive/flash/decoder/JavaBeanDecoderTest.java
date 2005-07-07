package com.carbonfive.flash.decoder;

import com.carbonfive.flash.test.*;
import flashgateway.io.*;
import junit.framework.*;

import java.util.*;

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

  public void testJavaBeanDecode() throws Exception
  {
    TestBean bean = com.carbonfive.flash.test.TestBean.getTestBean(); // gets a filled in TestBean

    ASObject aso = new ASObject();
    aso.setType(TestBean.class.getName());
    aso.put("intField", new Double(bean.getIntField()));
    aso.put("shortField", new Double(bean.getShortField()));
    aso.put("longField", new Double(bean.getLongField()));
    aso.put("doubleField", new Double(bean.getDoubleField()));
    aso.put("strField", bean.getStrField());

    ActionScriptDecoder decoder = factory.getDecoder(aso, TestBean.class);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(aso, TestBean.class);
    assertNotNull(decodedObject);
    assertTrue(decodedObject instanceof com.carbonfive.flash.test.TestBean);

    TestBean decodedBean = (TestBean) decodedObject;
    assertEquals(bean.getIntField(), decodedBean.getIntField());
    assertEquals(bean.getShortField(), decodedBean.getShortField());
    assertEquals(bean.getLongField(), decodedBean.getLongField());
    assertEquals(bean.getDoubleField(), decodedBean.getDoubleField(), DELTA);
    assertEquals(bean.getStrField(), decodedBean.getStrField());
  }

  public void testJavaBeanDecodeWithInterface() throws Exception
  {
    ASObject outer = new ASObject();
    outer.setType(TestBean.class.getName());

    ASObject inner = new ASObject();
    inner.setType(HashSet.class.getName());

    outer.put("colField", inner);

    ActionScriptDecoder decoder = factory.getDecoder(outer, TestBean.class);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(outer, TestBean.class);
    assertNotNull(decodedObject);
    assertTrue(decodedObject instanceof TestBean);

    TestBean decodedBean = (TestBean) decodedObject;
    assertNotNull(decodedBean.getColField());
    assertEquals(HashSet.class, decodedBean.getColField().getClass());
  }

  public void testJavaBeanInterfaceDecode() throws Exception
  {
    ASObject bean = new ASObject();
    bean.setType(TestBean.class.getName());
    bean.put("intField", new Double(30));

    ActionScriptDecoder decoder = factory.getDecoder(bean, Testable.class);
    assertNotNull(decoder);

    Object decodedObject = decoder.decodeObject(bean, Testable.class);
    assertNotNull(decodedObject);
    assertEquals(TestBean.class, decodedObject.getClass());

    TestBean decodedBean = (TestBean) decodedObject;
    assertEquals(30, decodedBean.getIntField());
  }
}
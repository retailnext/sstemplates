package com.carbonfive.flash;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import junit.framework.*;
import flashgateway.io.ASObject;

public class ASTranslatorTest
  extends    TestCase
{

  /**
   * This contructor provides a new ASTranslatorTest.
   * </p>
   * @param name The String needed to build this object
   */
  public ASTranslatorTest(String name)
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
    TestSuite suite = new TestSuite(ASTranslatorTest.class);
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


  public void testTestObjects()
    throws Exception
  {
    java.beans.Beans.instantiate(this.getClass().getClassLoader(), TestBean.class.getName());
    java.beans.Beans.instantiate(this.getClass().getClassLoader(), DeepTestBean.class.getName());
  }

  public void testTranslateToASObject()
    throws Exception
  {
    TestBean bean = TestBean.getTestBean();

    ASObject as = (ASObject) new ASTranslator().toActionScript(bean);

    validateTestBean(bean, as);
  }

  public void testDeepTranslateToASObject()
    throws Exception
  {
    TestBean bean = TestBean.getTestBean();

    DeepTestBean deepbean = new DeepTestBean();
    deepbean.setBeanField(bean);

    ASObject as = (ASObject) new ASTranslator().toActionScript(deepbean);
    assertNotNull(as);

    assertEquals(DeepTestBean.class.getName(), as.getType());

    assertTrue(as.containsKey("beanField"));
    ASObject as2 = (ASObject) as.get("beanField");
    validateTestBean(bean, as2);
  }

  public void testComplexSpecificTranslateToASObject()
    throws Exception
  {
    ComplexSpecificTestBean a = new ComplexSpecificTestBean();

    a.setListField(new ArrayList());
    a.getListField().add("one");

    TestBean bean = TestBean.getTestBean();

    a.getListField().add(bean);
    a.getListField().add(bean);

    a.setMapField(new HashMap());
    a.getMapField().put("this", "that");
    a.getMapField().put("bean", bean);

    // translate

    ASObject as = (ASObject) new ASTranslator().toActionScript(a);
    assertNotNull(as);
    assertEquals(ComplexSpecificTestBean.class.getName(), as.getType());

    // test list field

    assertTrue(as.containsKey("listField"));
    ArrayList list = (ArrayList) as.get("listField");
    assertNotNull(list);
    assertEquals(3, list.size());
    assertEquals("one", list.get(0));

    ASObject as1 = (ASObject) list.get(1);
    validateTestBean(bean, as1);
    ASObject as2 = (ASObject) list.get(2);
    validateTestBean(bean, as2);

    // test map field

    assertTrue(as.containsKey("mapField"));
    HashMap map = (HashMap) as.get("mapField");
    assertNotNull(map);
    assertTrue(map.containsKey("this"));
    assertEquals("that", map.get("this"));

    ASObject as3 = (ASObject) map.get("bean");
    validateTestBean(bean, as2);

    // assertTrue(as2 == as3);
  }

  public void testTranslateToBean()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(TestBean.class.getName());
    as.put("strField", "A string");
    as.put("intField", new Double(3));
    as.put("xmlField", TestBean.getXmlDocument());

    TestBean bean = (TestBean) new ASTranslator().fromActionScript(as);
    assertNotNull(bean);
    assertEquals(as.get("intField"), new Double(bean.getIntField()));
    assertEquals(as.get("strField"), bean.getStrField());
    Document xmlField = (Document) as.get("xmlField");
    assertEquals(xmlField.getDocumentElement().getTagName(), TestBean.getXmlDocument().getDocumentElement().getTagName());
  }

  public void testDeepTranslateToBean()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(DeepTestBean.class.getName());

    ASObject as2 = new ASObject();
    as2.setType(TestBean.class.getName());
    as2.put("strField", "A string");
    as2.put("intField", new Double(3));

    as.put("beanField", as2);

    DeepTestBean deepBean = (DeepTestBean) new ASTranslator().fromActionScript(as);
    assertNotNull(deepBean);

    TestBean bean = deepBean.getBeanField();
    assertNotNull(bean);

    assertEquals(as2.get("intField"), new Double(bean.getIntField()));
    assertEquals(as2.get("strField"), bean.getStrField());
  }


  public void testBeanCollection()
    throws Exception
  {
    TestBean bean = new TestBean();
    bean.setStrField( "testtesttest" );
    ArrayList listOfBeans = new ArrayList();

    for( int element=0; element<10; element++)
    {
      listOfBeans.add( bean );
    }

    assertEquals( 10, listOfBeans.size() );
    ArrayList listOfBeansTranslatedToASObjects = (ArrayList) new ASTranslator().toActionScript( listOfBeans );

    assertEquals( 10, listOfBeansTranslatedToASObjects.size() );
    Object uncastElementFromList = listOfBeansTranslatedToASObjects.get( 0 );

    ASObject aso = (ASObject) uncastElementFromList;
  }

  public void testComplexSpecificTranslateToBean()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(ComplexSpecificTestBean.class.getName());

    ArrayList asList = new ArrayList();
    asList.add("one");
    asList.add("two");

    ASObject as2 = new ASObject();
    as2.setType(TestBean.class.getName());
    as2.put("strField", "hello");
    as2.put("intField", new Double(10));
    asList.add(as2);

    ASObject asMap = new ASObject();
    asMap.put("hi", "there");
    asMap.put("bean", as2);

    as.put("listField", asList);
    as.put("mapField", asMap);

    // translate
    ComplexSpecificTestBean cbean = (ComplexSpecificTestBean) new ASTranslator().fromActionScript(as);
    assertNotNull(cbean);

    // validate
    assertNotNull(cbean.getListField());
    assertEquals(3, cbean.getListField().size());
    assertEquals("one", cbean.getListField().get(0));
    assertEquals("two", cbean.getListField().get(1));
    assertTrue(cbean.getListField().get(2) instanceof TestBean);

    TestBean tbean = (TestBean) cbean.getListField().get(2);
    assertEquals("hello", tbean.getStrField());
    assertEquals(10, tbean.getIntField());

    assertNotNull(cbean.getMapField());
    assertEquals("there", cbean.getMapField().get("hi"));
    assertTrue(cbean.getMapField().get("bean") instanceof TestBean);

    tbean = (TestBean) cbean.getMapField().get("bean");
    assertEquals("hello", tbean.getStrField());
    assertEquals(10, tbean.getIntField());
  }

  public void testComplexGenericTranslateToASObject()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(ComplexGenericTestBean.class.getName());

    ArrayList asList = new ArrayList();
    asList.add("one");
    asList.add("two");

    ASObject as2 = new ASObject();
    as2.setType(TestBean.class.getName());
    as2.put("strField", "hello");
    as2.put("intField", new Double(10));
    asList.add(as2);

    ASObject asMap = new ASObject();
    asMap.put("hi", "there");
    asMap.put("bean", as2);

    as.put("listField", asList);
    as.put("mapField", asMap);

    // translate
    ComplexGenericTestBean cbean = (ComplexGenericTestBean) new ASTranslator().fromActionScript(as);
    assertNotNull(cbean);

    // validate
    assertNotNull(cbean.getListField());
    assertEquals(3, cbean.getListField().size());
    assertEquals("one", cbean.getListField().get(0));
    assertEquals("two", cbean.getListField().get(1));
    assertTrue(cbean.getListField().get(2) instanceof TestBean);

    TestBean tbean = (TestBean) cbean.getListField().get(2);
    assertEquals("hello", tbean.getStrField());
    assertEquals(10, tbean.getIntField());

    assertNotNull(cbean.getMapField());
    assertEquals("there", cbean.getMapField().get("hi"));
    assertTrue(cbean.getMapField().get("bean") instanceof TestBean);

    tbean = (TestBean) cbean.getMapField().get("bean");
    assertEquals("hello", tbean.getStrField());
    assertEquals(10, tbean.getIntField());
  }

  public void testComplexGenericTranslateToBean()
    throws Exception
  {
  }

  public void testReferencesTranslateToASObject()
    throws Exception
  {
    HashMap map = new HashMap();
    TestBean bean = TestBean.getTestBean();

    map.put("one", bean);
    map.put("two", bean);

    ASObject as = (ASObject) new ASTranslator().toActionScript(map);
    assertNotNull(as);
    assertNotNull(as.get("one"));
    assertNotNull(as.get("two"));
    assertTrue("should be the same object reference:\n" + as.get("one") + "\n" + as.get("two"), as.get("one") == as.get("two"));
  }

  public void testNoReferencesTranslateToASObject()
    throws Exception
  {
    ComplexSpecificTestBean bean = new ComplexSpecificTestBean();
    ArrayList list = new ArrayList();
    list.add(TestBean.getTestBean());
    list.add(TestBean.getTestBean());
    bean.setListField(list);

    ASObject as = (ASObject) new ASTranslator().toActionScript(bean);
    ArrayList asList = (ArrayList) as.get("listField");
    assertNotNull(asList.get(0));
    assertNotNull(asList.get(1));
    assertTrue("should not be the same object reference", asList.get(0) != asList.get(1));
  }

  public void testReferencesTranslateToBean()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(ComplexSpecificTestBean.class.getName());

    ASObject asBean1 = new ASObject();
    asBean1.setType(TestBean.class.getName());
    asBean1.put("strField", "hello");
    asBean1.put("intField", new Double(10));

    ASObject asBean2 = new ASObject();
    asBean2.setType(TestBean.class.getName());
    asBean2.put("strField", "hello");
    asBean2.put("intField", new Double(10));

    ASObject asMap = new ASObject();
    asMap.put("one",   asBean1);
    asMap.put("two",   asBean2);
    asMap.put("three", asBean1);

    as.put("mapField", asMap);

    // translate
    ComplexSpecificTestBean cbean = (ComplexSpecificTestBean) new ASTranslator().fromActionScript(as);
    assertNotNull(cbean);

    // validate
    assertNotNull(cbean.getMapField());
    assertTrue("one and two should not be ==", cbean.getMapField().get("one") != cbean.getMapField().get("two"));
    assertTrue("two and three should not be ==", cbean.getMapField().get("two") != cbean.getMapField().get("three"));
    assertTrue("one and three should be ==", cbean.getMapField().get("one") == cbean.getMapField().get("three"));
  }

  public void testNoReferencesTranslateToBean()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(ComplexSpecificTestBean.class.getName());

    ASObject asBean1 = new ASObject();
    asBean1.setType(TestBean.class.getName());
    asBean1.put("strField", "hello");
    asBean1.put("intField", new Double(10));

    ASObject asBean2 = new ASObject();
    asBean2.setType(TestBean.class.getName());
    asBean2.put("strField", "hello");
    asBean2.put("intField", new Double(10));

    ArrayList asList = new ArrayList();
    asList.add(asBean1);
    asList.add(asBean2);

    as.put("listField", asList);

    ComplexSpecificTestBean bean = (ComplexSpecificTestBean) new ASTranslator().fromActionScript(as);
    ArrayList list = bean.getListField();
    assertNotNull(list.get(0));
    assertNotNull(list.get(1));
    assertTrue("should not be the same object reference", list.get(0) != list.get(1));
  }

  public void testTranslateEmptySet()
    throws Exception
  {
    Object encodedSet = new ASTranslator().toActionScript(new HashSet());
    assertNotNull(encodedSet);

    ArrayList list = new ArrayList();
    assertEquals(list, encodedSet);
  }

  public void testCircularTranslateToASObject()
    throws Exception
  {
    CircularTestBean ctb = new CircularTestBean();
    ctb.setMe(ctb);
    Object encoded = new ASTranslator().toActionScript(ctb);

    assertNotNull(encoded);
    assertTrue(encoded instanceof ASObject);
    ASObject as = (ASObject) encoded;
    assertTrue(as.get("me") == as);
  }

  public void testCircularTranslateFromASObject()
    throws Exception
  {
    ASObject as = new ASObject();
    as.setType(CircularTestBean.class.getName());
    as.put("me", as);
    Object obj = new ASTranslator().fromActionScript(as);
    assertNotNull(obj);
    assertTrue(obj instanceof CircularTestBean);
    CircularTestBean ctb = (CircularTestBean) obj;
    assertTrue(ctb == ctb.getMe());
  }

  private void validateTestBean(TestBean bean, ASObject as)
    throws Exception
  {
    assertNotNull(as);
    String msg = as.toString();

    assertEquals(msg, TestBean.class.getName(), as.getType());

    assertTrue(msg, as.containsKey("intField"));
    Object intField = as.get("intField");
    assertTrue(msg, intField instanceof Double);
    assertEquals(msg, new Double(bean.getIntField()), intField);

    assertTrue(msg, as.containsKey("shortField"));
    Object shortField = as.get("shortField");
    assertTrue(msg, shortField instanceof Double);
    assertEquals(msg, new Double(bean.getShortField()), shortField);

    assertTrue(msg, as.containsKey("longField"));
    Object longField = as.get("longField");
    assertTrue(msg, longField instanceof Double);
    assertEquals(msg, new Double(bean.getLongField()), longField);

    assertTrue(msg, as.containsKey("doubleField"));
    Object doubleField = as.get("doubleField");
    assertTrue(msg, doubleField instanceof Double);
    assertEquals(msg, new Double(bean.getDoubleField()), doubleField);

    assertTrue(msg, as.containsKey("strField"));
    Object strField = as.get("strField");
    assertTrue(msg, strField instanceof String);
    assertEquals(msg, bean.getStrField(), strField);
    
    assertTrue(msg, as.containsKey("xmlField"));
    Object xmlField = as.get("xmlField");
    assertTrue(msg, xmlField instanceof Document);
    assertEquals(msg, bean.getXmlField().getDocumentElement().getTagName(), TestBean.getXmlDocument().getDocumentElement().getTagName());
  }

  public void testCacheGetsEmptied()
  {
  }

  public static class DeepTestBean
    implements java.io.Serializable
  {
    private TestBean beanField;
    public TestBean getBeanField() { return this.beanField; }
    public void setBeanField(TestBean t) { this.beanField = t; }
  }

  public static class ComplexSpecificTestBean
    implements java.io.Serializable
  {
    private ArrayList listField;
    private HashMap mapField;
    public ArrayList getListField() { return this.listField; }
    public void setListField(ArrayList l) { this.listField = l; }
    public HashMap getMapField() { return this.mapField; }
    public void setMapField(HashMap h) { this.mapField = h; }
  }

  public static class ComplexGenericTestBean
    implements java.io.Serializable
  {
    private List listField;
    private Map mapField;
    public List getListField() { return this.listField; }
    public void setListField(List l) { this.listField = l; }
    public Map getMapField() { return this.mapField; }
    public void setMapField(Map h) { this.mapField = h; }
  }

  public static class CircularTestBean
    implements Serializable
  {
    private CircularTestBean me;
    public CircularTestBean getMe() { return me; }
    public void setMe(CircularTestBean c) { me = c; }
  }

}

package com.carbonfive.flash;

import junit.framework.*;

public class TranslationFilterTest
  extends    TestCase
{

  public TranslationFilterTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(TranslationFilterTest.class);
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

  public void testIgnoreClass()
    throws Exception
  {
    TranslationFilter filter = TranslationFilter.getBaseFilter();
    filter.ignoreClass(TestBean.class);
    TestBean bean = new TestBean();
    assertTrue(filter.doIgnoreClass(bean.getClass()));
  }

  public void testIgnoreProperty()
    throws Exception
  {
    TranslationFilter filter = TranslationFilter.getBaseFilter();
    filter.ignoreProperty(TestBean.class, "intField");
    TestBean bean = TestBean.getTestBean();
    assertTrue(filter.doIgnoreProperty(bean.getClass(), "intField"));
  }

  public void testIgnoreSuperclass()
    throws Exception
  {
    TranslationFilter filter = TranslationFilter.getBaseFilter();
    filter.ignoreClass(Object.class);
    TestBean bean = new TestBean();
    assertTrue(filter.doIgnoreClass(bean.getClass()));
  }

  public void testIgnoreSuperProperty()
    throws Exception
  {
    TranslationFilter filter = TranslationFilter.getBaseFilter();
    filter.ignoreProperty(Object.class, "class");
    TestBean bean = TestBean.getTestBean();
    assertTrue(filter.doIgnoreProperty(bean.getClass(), "class"));
  }
}

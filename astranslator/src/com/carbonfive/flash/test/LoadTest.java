package com.carbonfive.flash.test;

import junit.framework.*;
import com.carbonfive.flash.test.*;
import com.carbonfive.flash.*;

/**
 * This class is used to run load tests easily.  It is excluded from the test
 * suite run by 'ant test'.
 */
public class LoadTest
  extends    TestCase
{
  public LoadTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(LoadTest.class);
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

  public void testLoad()
    throws Exception
  {
    LargeObject lo = LargeObject.create();
    System.out.println("ASTranslating...");
    ASTranslatorFactory.getInstance().getASTranslator().toActionScript(lo);
    System.out.println("Done");
  }
}

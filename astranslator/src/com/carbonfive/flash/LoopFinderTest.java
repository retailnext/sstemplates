package com.carbonfive.flash;

import junit.framework.*;

public class LoopFinderTest
  extends    TestCase
{

  public LoopFinderTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(LoopFinderTest.class);
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

  public void testNullClass() throws Exception
  {
    new LoopFinder().add(null);
  }

  public void testSequencedHashMapNPE() throws Exception
  {
    new LoopFinder().add(LoopFinder.class);
  }
}

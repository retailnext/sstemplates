package com.carbonfive.gis;

import java.util.*;
import junit.framework.*;

public class ZipCodeManagerTest
  extends    TestCase
{
  public ZipCodeManagerTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(ZipCodeManagerTest.class);
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

  private static final double DELTA = 0.000000001;

  public void testLoadZipCodes()
    throws Exception
  {
    ZipCodeManager.loadZipCodes();
  }

  public void testGetLocation()
    throws Exception
  {
    SpatialLocation c5 = ZipCodeManager.getLocation("94107");
    assertNotNull(c5);
    assertEquals(-122.395770, c5.getLongitudeAsDegrees(), DELTA);
    assertEquals(37.766529,   c5.getLatitudeAsDegrees(),  DELTA);
  }
}
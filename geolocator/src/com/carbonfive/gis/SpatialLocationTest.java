package com.carbonfive.gis;

import java.util.*;
import java.util.logging.*;
import junit.framework.*;

public class SpatialLocationTest
  extends    TestCase
{
  private static final Logger log = Logger.getLogger(SpatialLocationTest.class.getName());

  public SpatialLocationTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(SpatialLocationTest.class);
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

  public void testStrictMath()
    throws Exception
  {
    assertEquals(1.0, 20.0 / (StrictMath.toDegrees(StrictMath.toRadians(20.0))), 0.000001);
  }

  public void testConversions()
    throws Exception
  {
    SpatialLocation loc = new SpatialLocation();
    loc.setLongitudeAsDegrees(20.0);
    loc.setLatitudeAsDegrees(30.0);
    assertEquals(20.0, loc.getLongitudeAsDegrees(), 0.00001);
    assertEquals(30.0, loc.getLatitudeAsDegrees(), 0.00001);
  }

  public void testDistance()
    throws Exception
  {
    SpatialLocation carbonFive    = ZipCodeManager.getLocation("94107");
    SpatialLocation yankeeStadium = ZipCodeManager.getLocation("10451");

    assertNotNull(carbonFive);
    assertNotNull(yankeeStadium);

    assertEquals(Calculator.toKm(2570.30), carbonFive.getDistance(yankeeStadium), 1);
    assertEquals(Calculator.toKm(2570.30), carbonFive.getDistance2(yankeeStadium), 1);
  }

  public void testDistance2()
    throws Exception
  {
    SpatialLocation c5        = ZipCodeManager.getLocation("94107");
    SpatialLocation cupertino = ZipCodeManager.getLocation("95014");

    assertNotNull(c5);
    assertNotNull(cupertino);

    assertEquals(Calculator.toKm(36.4), c5.getDistance(cupertino), 1);
    assertEquals(Calculator.toKm(36.4), c5.getDistance2(cupertino), 1);
  }

  public void testLongitudeRange()
    throws Exception
  {
  }

  public void testLatitudeRange()
    throws Exception
  {
  }

  public void testSquareRange()
    throws Exception
  {
  }

  public void testIsInRange()
    throws Exception
  {
    SpatialLocation c5         = ZipCodeManager.getLocation("94107");
    SpatialLocation cupertino  = ZipCodeManager.getLocation("95014");

    assertNotNull(c5);
    assertNotNull(cupertino);

    double good = Calculator.toKm(56.0);
    double bad  = Calculator.toKm(20.0);

    assertTrue(c5.getLongitude() + c5.getLongitudeRange(good) >= cupertino.getLongitude());
    assertTrue(c5.getLongitude() - c5.getLongitudeRange(good) <= cupertino.getLongitude());

    assertTrue(c5.getLatitude() + c5.getLatitudeRange(good) >= cupertino.getLatitude());
    assertTrue(c5.getLatitude() - c5.getLatitudeRange(good) <= cupertino.getLatitude());

    assertTrue(c5.isInRange(cupertino, c5.getLongitudeRange(good), c5.getLatitudeRange(good)));
    assertTrue(! c5.isInRange(cupertino, c5.getLongitudeRange(bad), c5.getLatitudeRange(bad)));
  }

  public void testRange()
    throws Exception
  {
    SpatialLocation cupertino = ZipCodeManager.getLocation("95014");
    assertNotNull(cupertino);

    List nearby = cupertino.getLocationsWithinRadius(ZipCodeManager.getAllLocations(), Calculator.toKm(3.0));
    assertNotNull(nearby);

    List correct = new ArrayList();
    correct.add(ZipCodeManager.getLocation("95014"));
    correct.add(ZipCodeManager.getLocation("94087"));
    correct.add(ZipCodeManager.getLocation("95129"));

    assertEquals(correct.size(), nearby.size());

    for (Iterator i = correct.iterator(); i.hasNext(); )
    {
      assertTrue(nearby.contains(i.next()));
    }
  }

  public void testOrderedRange()
    throws Exception
  {
    SpatialLocation cupertino = ZipCodeManager.getLocation("95014");
    assertNotNull(cupertino);

    List nearby = cupertino.getLocationsWithinRadius(ZipCodeManager.getAllLocations(), Calculator.toKm(3.0));
    assertNotNull(nearby);
    assertEquals(3, nearby.size());

    SpatialLocation closest = ZipCodeManager.getLocation("95014");
    SpatialLocation closer  = ZipCodeManager.getLocation("94087");
    SpatialLocation close   = ZipCodeManager.getLocation("95129");

    assertEquals(nearby.get(0), closest);
    assertEquals(nearby.get(1), closer);
    assertEquals(nearby.get(2), close);
  }

  public void testHeading()
    throws Exception
  {
    SpatialLocation sf = ZipCodeManager.getLocation("94107");
    SpatialLocation la = ZipCodeManager.getLocation("90210");
    assertNotNull(sf);
    assertNotNull(la);

    double heading = sf.getHeading(la);
    String msg = "Heading (94107, 90210)";
    assertEquals(msg, 138.0, StrictMath.toDegrees(heading), 2.0);
  }

  public void testNearby()
    throws Exception
  {
    SpatialLocation c5 = ZipCodeManager.getLocation("94107");
    assertNotNull(c5);

    List nearby = c5.getLocationsNearby(ZipCodeManager.getAllLocations(), 5);
    assertNotNull(nearby);
    assertEquals(5, nearby.size());

    log.fine(nearby.toString());

    assertEquals(ZipCodeManager.getLocation("94107"), nearby.get(0));
    assertEquals(ZipCodeManager.getLocation("941HH"), nearby.get(1));
    assertEquals(ZipCodeManager.getLocation("94103"), nearby.get(2));
    assertEquals(ZipCodeManager.getLocation("94102"), nearby.get(3));
    assertEquals(ZipCodeManager.getLocation("94105"), nearby.get(4));
  }
}
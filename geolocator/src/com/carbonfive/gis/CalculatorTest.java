package com.carbonfive.gis;

import junit.framework.*;

import java.util.*;
import java.io.*;

public class CalculatorTest
    extends TestCase
{
  private Collection pizzaParlors = new ArrayList();
  private PizzaParlor sf, ny, chicago, la, boston;
  private Calculator calculator = null;

  public CalculatorTest(String s)
  {
    super(s);
  }

  public void setUp() throws Exception
  {
    super.setUp();

    calculator = Calculator.getInstance();

    sf      = new PizzaParlor("Cable Car Pizza",       "San Francisco", "CA", "94133");
    ny      = new PizzaParlor("East Village Pizzaria", "New York",      "NY", "10003");
    chicago = new PizzaParlor("Big Tony's Pizza",      "Chicago",       "IL", "60622");
    la      = new PizzaParlor("Mr. Pizza",             "Los Angeles",   "CA", "90036");
    boston  = new PizzaParlor("Nino's Pizza",          "Boston",        "MA", "02114");

    pizzaParlors.add(sf);
    pizzaParlors.add(ny);
    pizzaParlors.add(chicago);
    pizzaParlors.add(la);
    pizzaParlors.add(boston);
  }

  public void tearDown() throws Exception
  {
    super.tearDown();
  }

  /**
   * The distance between San Francisco and Los Angeles is approx. 553.5 km
   * This is approx 343.9 miles
   * 0.1 km/mile error bar
   */
  public void testGetDistance() throws Exception
  {
    assertEquals(553.5, calculator.getDistance(sf, la), 0.1);
    assertEquals(343.9, Calculator.toMiles(553.5), 0.1);
  }

  /**
   * The heading from Los Angeles to New York is 0.0 radians
   * This is aprrox. 67.6 degrees (12noon is 0, 3pm is 90)
   * 0.01 radian/degree error bar
   */
  public void testGetHeading() throws Exception
  {
    assertEquals(1.18, calculator.getHeading(la, ny), 0.01);
    assertEquals(67.6, StrictMath.toDegrees(1.18), 0.01);
  }

  /**
   * The two nearest parlors to Los Angeles are itself, then San Francisco, then Chicago
   */
  public void testGetLocationsNearby() throws Exception
  {
    List nearby = Arrays.asList(new PizzaParlor[] { la, sf, chicago });
    assertEquals(nearby, calculator.getLocationsNearby(la, pizzaParlors, 3));
  }

  /**
   * The three parlors within 1200km (745 miles) of New York are itself, then Boston, then Chicago
   */
  public void testGetLocationsWithinRadius() throws Exception
  {
    List within = Arrays.asList(new PizzaParlor[] { ny, boston, chicago });
    assertEquals(within, calculator.getLocationsWithinRadius(ny, pizzaParlors, 1200));
  }

  private static class PizzaParlor implements Serializable, Location
  {
    private String name;
    private String city;
    private String state;
    private String zipCode;

    private PizzaParlor(String name, String city, String state, String zipCode)
    {
      this.name = name;
      this.city = city;
      this.state = state;
      this.zipCode = zipCode;
    }

    public String getZipCode() { return zipCode; }

    public String toString()
    {
      return name + " (" + city + ", " + state + " " + zipCode + ")";
    }
  }
}
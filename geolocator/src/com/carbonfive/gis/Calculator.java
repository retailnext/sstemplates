package com.carbonfive.gis;

import java.util.*;
import java.util.logging.*;

/**
 * Provides an easy interface into Zip Code based calculations.  Using a built-in
 * database of Zip Code to Geo-coordinates (longitude and latitude), this class
 * allows client code to ask common location-related questions.<br/></br>
 *
 * <h3>Usage</h3>
 *
 * In order to use this class, first you must slightly modify your existing classes
 * that represent locations with Zip Codes by making them extend com.carbonfive.gis.Location.
 * If your objects already have a getZipCode() method, then that is enough.  If your
 * method to retrieve Zip Code is named differently, you will have to crate a proxy
 * method that maps getZipCode() to your method.<br/><br/>
 *
 * After that is done, you can pass those objects to the Calculator in the methods
 * described below.<br/><br/>
 * 
 * <h3>Units</h3>
 *
 * All units are described in kilometers (distance) and radians (angle).<br/>
 * Distance conversion methods are available in this class.<br/>
 * Angle conversion methods are availabe in java.lang.StrictMath.<br/>
 */
public class Calculator
{
  private static final Logger log = Logger.getLogger(Calculator.class.getName());

  private static final double MILES_TO_KILOMETERS_FACTOR = 1.6093;

  private static Calculator instance = null;

  private Calculator() { }

  /**
   * Get a Calculator instance.
   */
  public static final synchronized Calculator getInstance()
  {
    if (instance == null)
    {
      instance = new Calculator();
    }
    return instance;
  }

  /**
   * Converts miles to kilometers.
   */
  public static final double toKm(double miles)
  {
    return miles * MILES_TO_KILOMETERS_FACTOR;
  }

  /**
   * Converts kilometers to miles.
   */
  public static final double toMiles(double km)
  {
    return km / MILES_TO_KILOMETERS_FACTOR;
  }

  /**
   * Get the distance (in km) between two locations, based on Zip Code.
   *
   * @param source The source location
   * @param dest   The destination location
   * @return The distance between the two locations in km
   * @throws CalculatorException If either location cannot be found in the Zip Code database
   * @see SpatialLocation#getDistance(SpatialLocation)
   */
  public double getDistance(Location source, Location dest)
    throws CalculatorException
  {
    SpatialLocation sourceSL = getSpatialLocation(source);
    SpatialLocation destSL   = getSpatialLocation(dest);

    if (sourceSL == null) throw new CalculatorException("Location not found: " + source.getZipCode());
    if (destSL   == null) throw new CalculatorException("Location not found: " + dest.getZipCode());

    return sourceSL.getDistance(destSL);
  }

  /**
   * Returns the list of Locations located within the given radius (in km) from the source,
   * based on Zip Code.
   *
   * @param source The source Location
   * @param possibles The collection of possible Locations to be used
   * @param radius The radius used for the search, in km
   * @return The list of Locations located within the radius, sorted by increasing distance
   * @throws CalculatorException If the source location cannot be found in the Zip Code database
   * @see SpatialLocation#getLocationsWithinRadius(Collection, double)
   */
  public List getLocationsWithinRadius(Location source, Collection possibles, double radius)
    throws CalculatorException
  {
    SpatialLocation sourceSL = getSpatialLocation(source);

    if (sourceSL == null) throw new CalculatorException("Location not found: " + source.getZipCode());

    Collection               possibleSLs = new ArrayList();
    Location                 loc         = null;
    SpatialLocationDecorator sld         = null;
    for (Iterator i = possibles.iterator(); i.hasNext(); )
    {
      loc = (Location) i.next();
      sld = getSpatialLocation(loc);
      if (sld == null)
      {
        log.warning("Location not found: " + loc.getZipCode());
      }
      else
      {
        possibleSLs.add(sld);
      }
    }
    
    List resultSLs = sourceSL.getLocationsWithinRadius(possibleSLs, radius);
    List resultLs = new ArrayList();
    for (Iterator i = resultSLs.iterator(); i.hasNext(); )
    {
      sld = (SpatialLocationDecorator) i.next();
      resultLs.add(sld.getLocation());
    }

    return resultLs;
  }

  /**
   * Get the initial heading, or true course (in radians East of North) from the source
   * location to the destination location, based on Zip Code.
   *
   * @param source The source location
   * @param dest   The destination location
   * @return The initial heading, or true course, from the source to the destination, in radians East of North
   * @throws CalculatorException If either location cannot be found in the Zip Code database
   * @see SpatialLocation#getHeading(SpatialLocation)
   */
  public double getHeading(Location source, Location dest)
    throws CalculatorException
  {
    SpatialLocation sourceSL = getSpatialLocation(source);
    SpatialLocation destSL   = getSpatialLocation(dest);

    if (sourceSL == null) throw new CalculatorException("Location not found: " + source.getZipCode());
    if (destSL   == null) throw new CalculatorException("Location not found: " + dest.getZipCode());

    return sourceSL.getHeading(destSL);
  }

  /**
   * Returns the list of up to n nearest Locations from the source, based on Zip Code. <br/><br/>
   * 
   * This method will return less than n Locations if the not enough Locations exist in the
   * possibles Collection.
   *
   * @param source The source Location
   * @param possibles The collection of possible Locations to be used
   * @param n The desired amount of Locations to be returned
   * @return The list of up to n Locations located nearest to the source, sorted by increasing distance
   * @throws CalculatorException If the source location cannot be found in the Zip Code database
   * @see SpatialLocation#getLocationsNearby(Collection, int)
   */
  public List getLocationsNearby(Location source, Collection possibles, int n)
    throws CalculatorException
  {
    SpatialLocation sourceSL = getSpatialLocation(source);

    if (sourceSL == null) throw new CalculatorException("Location not found: " + source.getZipCode());

    Collection               possibleSLs = new ArrayList();
    Location                 loc         = null;
    SpatialLocationDecorator sld         = null;
    for (Iterator i = possibles.iterator(); i.hasNext(); )
    {
      loc = (Location) i.next();
      sld = getSpatialLocation(loc);
      if (sld == null)
      {
        log.warning("Location not found: " + loc.getZipCode());
      }
      else
      {
        possibleSLs.add(sld);
      }
    }
    
    List resultSLs = sourceSL.getLocationsWithinRadius(possibleSLs, n);
    List resultLs = new ArrayList();
    for (Iterator i = resultSLs.iterator(); i.hasNext(); )
    {
      sld = (SpatialLocationDecorator) i.next();
      resultLs.add(sld.getLocation());
    }

    return resultLs;
  }

  SpatialLocationDecorator getSpatialLocation(Location loc)
  {
    SpatialLocation          sl  = ZipCodeManager.getLocation(loc.getZipCode());
    SpatialLocationDecorator sld = new SpatialLocationDecorator(sl);
    sld.setLocation(loc);
    return sld;
  }
}
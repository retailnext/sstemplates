package com.carbonfive.gis;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.*;

/**
 * Encapsulates a location in Longitude and Latitude, with an optional associate ZCTA code.
 * <img src="http://jwocky.gsfc.nasa.gov/imglib1/latlon.gif"/><br/><br/>
 * Latitude is measured from the equator, with positive values going north and negative values going south.
 * Longitude is measured from the Prime Meridian (which is the longitude that runs through Greenwich, England),
 * with positive values going east and negative values going west.
 * So, for example, 65 degrees west longitude, 45 degrees north latitude is -65 degrees longitude, +45 degrees latitude.
 *
 * @link http://williams.best.vwh.net/avform.htm
 */
public class SpatialLocation
  implements Location, Serializable
{
  private static final Logger log = Logger.getLogger(SpatialLocation.class.getName());

  static final double EARTH_RADIUS = 6378.14; // in kilometers

  private double longitude;  // stored in radians
  private double latitude;   // stored in radians
  private String zipCode;

  public SpatialLocation()
  {
    this.longitude = 0.0;
    this.latitude  = 0.0;
    this.zipCode   = null;
  }

  public SpatialLocation(double longitude, double latitude)
  {
    setLongitude(longitude);
    setLatitude(latitude);
  }

  public double getLongitude() { return this.longitude; }
  public double getLatitude()  { return this.latitude;  }

  public double getLongitudeAsDegrees() { return StrictMath.toDegrees(this.longitude); }
  public double getLatitudeAsDegrees()  { return StrictMath.toDegrees(this.latitude);  }

  public void setLongitude(double d)
  {
    if (d < -1 * StrictMath.PI) throw new SpatialLocationException("Longitude cannot be less than -pi (-180)");
    if (d > StrictMath.PI)      throw new SpatialLocationException("Longitude cannot be greater than pi (180)");
    this.longitude = d;
  }

  public void setLatitude(double d)
  {
    if (d < -1 * (StrictMath.PI / 2)) throw new SpatialLocationException("Latitude cannot be less than -pi/2 (-90)");
    if (d > StrictMath.PI / 2)        throw new SpatialLocationException("Latitude cannot be greater than pi/2 (90)");
    this.latitude = d;     
  }

  public void setLongitudeAsDegrees(double d)
  {
    setLongitude(StrictMath.toRadians(d));
  }

  public void setLatitudeAsDegrees(double d)
  {
    setLatitude(StrictMath.toRadians(d));
  }

  public String getZipCode()         { return this.zipCode; }
  public void   setZipCode(String s) { this.zipCode = s;    }

  // These methods are convenience methods use to make long trigonometric calculations
  //  more readable in other methods (distance, heading, etc)
  private static final double sin(double a)             { return StrictMath.sin(a);      }
  private static final double asin(double a)            { return StrictMath.asin(a);     }
  private static final double cos(double a)             { return StrictMath.cos(a);      }
  private static final double acos(double a)            { return StrictMath.acos(a);     }
  private static final double pow(double a, double b)   { return StrictMath.pow(a, b);   }
  private static final double sqrt(double a)            { return StrictMath.sqrt(a);     }
  private static final double atan2(double y, double x) { return StrictMath.atan2(y, x); }

  /**
   * Returns distance (in km) from two locations, based on longitude and latitude. <br/>
   * Formula: <br/><br/>
   * <code>
   * d = R<sub>E</sub> cos<sup>-1</sup>( cos(La<sub>1</sub>)cos(Lo<sub>1</sub>)cos(La<sub>2</sub>)cos(Lo<sub>2</sub>) +
   *                                     cos(La<sub>1</sub>)sin(Lo<sub>1</sub>)cos(La<sub>2</sub>)sin(Lo<sub>2</sub>) +
   *                                     sin(La<sub>1</sub>)sin(La<sub>2</sub>) )<br/><br/>
   * <pre>
   * where d   = distance (in km)
   *       R<sub>E</sub>  = Earth radius (in km)
   *       La<sub>n</sub> = Latitude of location n (in radians)
   *       Lo<sub>n</sub> = Longitude of location n (in radians)
   * </pre>
   * </code>
   * 
   * @param loc Location of point to calculate distance with
   * @return Distance (in km) from two locations
   * @see #getDistance2(SpatialLocation)
   */
  public double getDistance(SpatialLocation loc)
  {
    double La1 = this.getLatitude(); double Lo1 = this.getLongitude();
    double La2 = loc.getLatitude();  double Lo2 = loc.getLongitude();

    // avoid double precision issues
    if (La1 == La2 && Lo1 == Lo2) return 0;

    return acos( ( cos(La1)*cos(Lo1)*cos(La2)*cos(Lo2) ) +
                 ( cos(La1)*sin(Lo1)*cos(La2)*sin(Lo2) ) +
                 ( sin(La1)*sin(La2) )
               ) * EARTH_RADIUS;
  }

  /**
   * Returns distance (in km) from two locations, based on longitude and latidue, and is less
   * suseptible to rounding errors for short distances than getDistance(). <br/>
   * Note that we have found no difference in the results of this method and the getDistance() method.
   * Formula: <br/><br/>
   * 
   * <code>
   * d = 2R<sub>E</sub> sin<sup>-1</sup> &#8730;( sin((La<sub>1</sub>-La<sub>2</sub>)/2)<sup>2</sup> +
   *     cos(La<sub>1</sub>)cos(La<sub>2</sub>)sin((Lo<sub>1</sub>-Lo<sub>2</sub>)/2)<sup>2</sup> )<br/><br/>
   * <pre>
   * where d   = distance (in km)
   *       R<sub>E</sub>  = Earth radius (in km)
   *       La<sub>n</sub> = Latitude of location n (in radians)
   *       Lo<sub>n</sub> = Longitude of location n (in radians)
   * </pre>
   * </code>
   * 
   * @param loc Location of point to calculate distance with
   * @return Distance (in km) from two locations
   * @see #getDistance(SpatialLocation)
   */
  public double getDistance2(SpatialLocation loc)
  {
    double La1 = this.getLatitude(); double Lo1 = this.getLongitude();
    double La2 = loc.getLatitude();  double Lo2 = loc.getLongitude();

    return 2 * EARTH_RADIUS *
           asin( sqrt( pow( sin((La1-La2)/2), 2 ) +
                       cos(La1)*cos(La2)* pow( sin((Lo1-Lo2)/2 ), 2 ) ) );
  }

  /**
   * Returns <b>initial</b> heading (in radians East of North) toward given SpatialLocation. <br/>
   * Formula: <br/><br/>
   *
   * <code>
   * tc = tan2<sup>-1</sup>( sin(Lo<sub>2</sub>-Lo<sub>1</sub>)cos(La<sub>2</sub>),
   *                         cos(La<sub>1</sub>)sin(La<sub>2</sub>) -
   *                         sin(La<sub>1</sub>)cos(La<sub>2</sub>)*cos(Lo<sub>2</sub>-Lo<sub>1</sub>) ) % 2&#960; <br/><br/>
   * <pre>
   * where tc  = true course toward given SpatialLocation (in radians E of N)
   *       La<sub>n</sub> = Latitude of location n (in radians)
   *       Lo<sub>n</sub> = Longitude of location n (in radians)
   * </pre>
   * </code>
   *
   * The formula gives the *initial* heading for a great-circle route from 
   * point A to point B. The heading will change in the course of the trip. 
   *
   * @param loc The destination location
   * @return The initial heading, or true course, from this location to the destination, in radians
   */
  public double getHeading(SpatialLocation loc)
  {
    double La1 = this.getLatitude(); double Lo1 = this.getLongitude();
    double La2 = loc.getLatitude();  double Lo2 = loc.getLongitude();

    return atan2( sin(Lo2-Lo1)*cos(La1), cos(La1)*sin(La2) - sin(La1)*cos(La2)*cos(Lo2-Lo1) ) % (2*StrictMath.PI);
  }

  boolean isInRange(SpatialLocation loc, double longRange, double latRange)
  {
    return ( ( loc.getLongitude() >= this.getLongitude() - longRange ) &&
             ( loc.getLongitude() <= this.getLongitude() + longRange ) &&
             ( loc.getLatitude()  >= this.getLatitude()  - latRange  ) &&
             ( loc.getLatitude()  <= this.getLatitude()  + latRange  ) );
  }

  /**
   * Return a list of SpatialLocations within a specific radius (in km) of this
   * SpatialLocation.
   *
   * @param locations The list of possible locations
   * @param radius The search radius (in km)
   * @return The list of locations within this radius, ordered by increasing distance
   */
  public List getLocationsWithinRadius(Collection locations, double radius)
  {
    List            nearby = getLocationsWithinSquare(locations, radius);
    List            within = new ArrayList();
    SpatialLocation loc    = null;
    for (Iterator i = nearby.iterator(); i.hasNext(); )
    {
      loc = (SpatialLocation) i.next();
      if (this.getDistance(loc) <= radius)
      {
        log.fine(this.zipCode + " -> " + loc.zipCode + " = " + this.getDistance(loc));
        within.add(loc);
      }
    }

    Collections.sort(within, new DistanceComparator(this));

    return within;
  }

  List getLocationsWithinSquare(Collection locations, double radius)
  {
    double longRange = getLongitudeRange(radius);
    double latRange  = getLatitudeRange(radius);

    List            nearby = new ArrayList();
    SpatialLocation loc    = null;
    for (Iterator i = locations.iterator(); i.hasNext(); )
    {
      loc = (SpatialLocation) i.next();
      if (this.isInRange(loc, longRange, latRange))
      {
        nearby.add(loc);
      }
    }

    return nearby;
  }

  private static final double MAX_RADIUS = 4096; // will cover entire US
  /**
   * Return a list the closest n SpatialLocations to this SpatialLocations.
   *
   * @param locations The list of possible locations
   * @param n The number of locations to return
   * @return The list of n closest locations, ordered by increasing distance
   */
  public List getLocationsNearby(Collection locations, int n)
  {
    if (n < 0) throw new IllegalArgumentException("Must pass a positive number, n");
    if (n == 0) return new ArrayList();
    if (locations == null) return new ArrayList();

    if (n >= locations.size()) return new ArrayList(locations);

    List   nearby = null;
    double radius = 1.0;
    while (radius <= MAX_RADIUS)
    {
      nearby = getLocationsWithinRadius(locations, radius);
      if (nearby.size() >= n)                return nearby.subList(0, n);
      if (nearby.size() >= locations.size()) return nearby;
      radius *= 2;
    }
    
    log.warning("There is a location outside the MAX_RADIUS range. locations.size()=" + locations.size() + ", nearby.size()=" + nearby.size());
    return nearby;
  }

  /**
   * @param  radius in km
   * @return Longitude in radians
   */
  double getLongitudeRange(double radius)
  {
    return StrictMath.asin(radius / EARTH_RADIUS) / StrictMath.cos(getLatitude());
  }

  /**
   * @param  radius in km
   * @return Latitude in radians
   */
  double getLatitudeRange(double radius)
  {
    return StrictMath.asin(radius / EARTH_RADIUS);
  }

  private static final NumberFormat degreeFormat = new DecimalFormat("###.000000");
  public String toString()
  {
    StringBuffer buf = new StringBuffer("SpatialLocation[")
      .append(degreeFormat.format(getLongitudeAsDegrees())).append("Long, ")
      .append(degreeFormat.format(getLatitudeAsDegrees())).append("Lat");
    if (zipCode != null)
    {
      buf.append(", ").append(zipCode);
    }
    buf.append("]");
    return buf.toString();
  }
}
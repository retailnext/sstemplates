package com.carbonfive.gis;

import java.util.*;
import java.util.logging.*;

class DistanceComparator
  implements Comparator
{
  private SpatialLocation source = null;

  public DistanceComparator(SpatialLocation source)
  {
    this.source = source;
  }

  public int compare(Object obj1, Object obj2)
  {
    if (obj1 == null || obj2 == null) throw new NullPointerException("Can't compare NULL SpatialLocations");

    if (! (obj1 instanceof SpatialLocation)) throw new IllegalArgumentException("Object is not a SpatialLocation: " + obj1);
    if (! (obj2 instanceof SpatialLocation)) throw new IllegalArgumentException("Object is not a SpatialLocation: " + obj2);

    SpatialLocation loc1 = (SpatialLocation) obj1;
    SpatialLocation loc2 = (SpatialLocation) obj2;

    double distance1 = source.getDistance(loc1);
    double distance2 = source.getDistance(loc2);

    if (distance1 >  distance2) return 1;
    if (distance1 <  distance2) return -1;
    if (distance1 == distance2)
    {
      // this stuff is to make it "consistent with equals"
      if (loc1.equals(loc2)) return 0;
      else
      {
        double heading1 = source.getHeading(loc1);
        double heading2 = source.getHeading(loc2);
        if (heading1 >  heading2) return 1;
        if (heading1 <  heading2) return -1;
        if (heading1 == heading2)
        {
          if (loc1.getZipCode() == null) return 1;
          if (loc2.getZipCode() == null) return -1;
          return loc1.getZipCode().compareTo(loc2.getZipCode());
        }
      }
    }

    throw new IllegalStateException("Distances are screwy: " + distance1 + ", " + distance2);
  }
}
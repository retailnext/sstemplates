package com.carbonfive.gis;

class SpatialLocationDecorator
  extends SpatialLocation
{
  private Location location;

  SpatialLocationDecorator(SpatialLocation sl)
  {
    super(sl.getLongitude(), sl.getLatitude());
    setZipCode(sl.getZipCode());
    this.location = null;
  }

  Location getLocation()           { return this.location; }
  void     setLocation(Location l) { this.location = l;    }
}
package com.carbonfive.gis;

public class SpatialLocationException
  extends RuntimeException
{
  public SpatialLocationException() { super(); }
  public SpatialLocationException(String msg) { super(msg); }
  public SpatialLocationException(Throwable cause) { super(cause); }
  public SpatialLocationException(String msg, Throwable cause) { super(msg, cause); }
}
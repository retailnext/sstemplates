package com.carbonfive.gis;

/**
 * A location with a ZIP Code.  Object needs to implement this interface in
 * order to be used in the Calculator.
 */
public interface Location
{
  public String getZipCode();
}
package com.carbonfive.gis;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.logging.*;

/**
 * A lookup for SpatialLocations based on Zip Code.  This class uses the ZCTA census
 * 2000 data.  ZCTA stands for Zip Code Tabulation Area, and is a new system used by
 * the US government to replace Zip Codes during censuses.  ZCTA codes are very 
 * similar to Zip Codes (still used by the US Post Office), and are often
 * interchangeable.  For more information about ZCTA, see the census 2000 ZCTA site: <br/>
 * <a href="http://www.census.gov/geo/www/gazetteer/places2k.html">http://www.census.gov/geo/www/gazetteer/places2k.html</a>
 */
public class ZipCodeManager
{
  private static final Logger log = Logger.getLogger(ZipCodeManager.class.getName());

  private static final boolean isUnitedStates = true;

  private static final String zipCodeFileProperty    = "carbonfive.zipcode.file";
  private static final String zipCodeFileREProperty  = "carbonfive.zipcode.file.RE";
  private static final String defaultZipCodeFileName = "zcta5.txt";
//  private static final String defaultZipCodeFileName = "zipcodes.txt";
//  private static final String defaultZipCodeFileName = "zipcodes1999.txt";
  private static final String defaultZipCodeFileRE   = "^[A-Z]{2}(\\w{5}).+([\\s-]\\d+\\.\\d+)\\s*(-?\\d+\\.\\d+)$";
//  private static final String defaultZipCodeFileRE   = "^(\\d{5})\\s+(-?\\d{1,3}\\.\\d+)\\s+(-?\\d{1,3}\\.\\d+)";
//  private static final String defaultZipCodeFileRE   = "^(\\d{5})\\s+([-\\+]\\d+\\.\\d+)\\s+([-\\+]\\d+\\.\\d+).*$";
  private static HashMap zipCodeMap = null;

  /**
   * Reload all ZCTA codes from the data file.
   */
  public static synchronized void loadZipCodes()
  {
    try
    {
      zipCodeMap = null;
      loadZipCodeMap();
    }
    catch (Exception e)
    {
      throw new LoadZipCodesException("Exception loading zip codes from file", e);
    }
  }

  /**
   * Get a SpatialLocation based on ZCTA code.
   *
   * @param zipcode The ZCTA code (similar to a Zip Code)
   * @return The coresponding SpatialLocation, or null if it does not exist
   */
  public static SpatialLocation getLocation(String zipcode)
  {
    return (SpatialLocation) getZipCodeMap().get(zipcode);
  }

  /**
   * Get a collection of all SpatialLocations.
   *
   * @return The collection of all SpatialLocations
   */
  public static Collection getAllLocations()
  {
    return Collections.unmodifiableCollection(zipCodeMap.values());
  }

  private static synchronized HashMap getZipCodeMap()
  {
    if (zipCodeMap == null)
    {
      try
      {
        loadZipCodeMap();
      }
      catch (Exception e)
      {
        throw new LoadZipCodesException("Exception loading zip codes from file" + e, e);
      }
    }
    return zipCodeMap;
  }

  private static synchronized void loadZipCodeMap()
    throws IOException, FileNotFoundException
  {
    zipCodeMap = new HashMap();

    String         zipCodeFileName   = getZipCodeFileName();
    String         zipCodeFileRE     = getZipCodeFileRE();
    BufferedReader zipCodeFileReader = getZipCodeFileReader(zipCodeFileName);

    log.info("Loading zip codes using file: " + zipCodeFileName);

    String          zipCode   = null;
    double          longitude = 0.0;
    double          latitude  = 0.0;
    SpatialLocation location  = null;

    log.info("RE: " + zipCodeFileRE);
    Pattern p = Pattern.compile(zipCodeFileRE);
    Matcher m = null;

    String line = zipCodeFileReader.readLine();
    while (line != null)
    {
      m = p.matcher(line);
      if (m.matches())
      {
        zipCode   = m.group(1);
        longitude = Double.parseDouble(m.group(3));
        latitude  = Double.parseDouble(m.group(2));

        if (isUnitedStates)
        {
          if (longitude > 0)
          {
            log.finer("Can't have a positive longitude in the USA.  Changing sign.");
            longitude = -1 * longitude;
          }
          if (latitude < 0)
          {
            log.finer("Can't have a negative latitude in the USA.  Changing sign.");
            latitude = -1 * latitude;
          }
        }

        location  = new SpatialLocation();
        location.setLongitudeAsDegrees(longitude);
        location.setLatitudeAsDegrees(latitude);
        location.setZipCode(zipCode);

        if (zipCodeMap.containsKey(zipCode))
        {
          log.fine("Duplicate zip code (skipping): " + zipCode);
//          throw new LoadZipCodesException("Duplicate ZIP code: " + zipCode);
        }

        zipCodeMap.put(zipCode, location);
      }
      else
      {
        // skip
        log.warning("Bad zip code line: " + line);
      }

      line = zipCodeFileReader.readLine();
    }

    log.info("Loaded zip code map with " + zipCodeMap.size() + " entries.");
  }

  private static String getZipCodeFileName()
  {
    return System.getProperty(zipCodeFileProperty, defaultZipCodeFileName);
  }

  private static String getZipCodeFileRE()
  {
    return System.getProperty(zipCodeFileREProperty, defaultZipCodeFileRE);
  }

  private static BufferedReader getZipCodeFileReader(String zipCodeFileName)
    throws FileNotFoundException
  {
    BufferedReader zipCodeFileReader = null;

    File f = new File(zipCodeFileName);
    if (f.exists()) zipCodeFileReader = new BufferedReader(new FileReader(f));
    else
    {
      ClassLoader cl = ZipCodeManager.class.getClassLoader();
      InputStream is = cl.getResourceAsStream(zipCodeFileName);
      if (is != null) zipCodeFileReader = new BufferedReader(new InputStreamReader(is));
      else
      {
        throw new FileNotFoundException("Cannot find Zip Code Manager file: " + zipCodeFileName);
      }
    }
    return zipCodeFileReader;
  }
}
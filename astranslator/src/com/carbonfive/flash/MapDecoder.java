package com.carbonfive.flash;

import java.util.*;
import java.beans.*;

/**
 * Decodes an ActionScript object to a Java map.
 */
public class MapDecoder
  implements ActionScriptDecoder
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MapDecoder.class);
  
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    Map map = null;
    try
    {
      ClassLoader classLoader = this.getClass().getClassLoader();

      if (desiredClass.isInterface()) map = new HashMap();
      else                            map = (Map) Beans.instantiate( classLoader, desiredClass.getName() );
    }
    catch (Exception e)
    {
      log.error("Exception creating appropriate Map class", e);
      return null;
    }

    Object        key          = null;
    Object        value        = null;
    Object        decodedValue = null;
    for (Iterator keys = ((Map) encodedObject).keySet().iterator(); keys.hasNext(); )
    {
      key   = keys.next();
      value = ((Map) encodedObject).get( key );

      decodedValue = DecoderFactory.getInstance().getDecoder( value, value.getClass() ).decodeObject( value, value.getClass() );
      map.put( key, decodedValue );
    }
    
    return map;
  }
}
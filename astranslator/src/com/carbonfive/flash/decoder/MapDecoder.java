package com.carbonfive.flash.decoder;

import java.util.*;
import java.beans.*;
import org.apache.commons.logging.*;

/**
 * Decodes an ActionScript object to a Java map.
 */
public class MapDecoder
  extends ActionScriptDecoder
{
  private static final Log log = LogFactory.getLog(MapDecoder.class);

  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    Map map = null;
    try
    {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      if (desiredClass.isInterface()) map = new HashMap();
      else                            map = (Map) Beans.instantiate( classLoader, desiredClass.getName() );
    }
    catch (Exception e)
    {
      log.error("Exception creating appropriate Map class", e);
      return null;
    }
    return map;
  }

  Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    Map map = (Map) shell;
    if (map == null) return null;

    ActionScriptDecoder decoder = null;
    Object key               = null;
    Object value             = null;
    Object decodedValue      = null;
    Class  decodedValueClass = null;
    for (Iterator keys = ((Map) encodedObject).keySet().iterator(); keys.hasNext(); )
    {
      key   = keys.next();
      value = ((Map) encodedObject).get( key );

      if (value == null) { map.put(key, null); continue; }

      decodedValueClass = DecoderFactory.decideClassToTranslateInto(value);

      decoder = DecoderFactory.getInstance().getDecoder(value, decodedValueClass);
      decodedValue = decoder.decodeObject(value, decodedValueClass);
      map.put( key, decodedValue );
    }
    
    return map;
  }
}
package com.carbonfive.flash;

import java.util.*;
import java.beans.*;

public class MapDecoder
  implements ActionScriptDecoder
{
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    Map map = null;
    try
    {
      ClassLoader classLoader = this.getClass().getClassLoader();
      Object      mapBean     = Beans.instantiate( classLoader, desiredClass.getName() );

      if (desiredClass.isInterface()) map = new HashMap();
      else                            map = (Map) mapBean;
    }
    catch (Exception e)
    {
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
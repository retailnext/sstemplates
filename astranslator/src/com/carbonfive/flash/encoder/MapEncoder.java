package com.carbonfive.flash.encoder;

import flashgateway.io.ASObject;
import java.util.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java map to an ActionScript object.
 */
public class MapEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    Map      asMap         = (Map) decodedObject;
    ASObject encodedObject = new ASObject();
    encodedObject.setType( decodedObject.getClass().getName() );

    Object key          = null;
    Object value        = null;
    Object encodedValue = null;
    for (Iterator i = asMap.keySet().iterator(); i.hasNext(); )
    {
      key = i.next();
      value = asMap.get(key);

      encodedValue = EncoderFactory.getInstance().getEncoder( value ).encodeObject( value );
      encodedObject.put( key, encodedValue );
    }

    return encodedObject;
  }
}
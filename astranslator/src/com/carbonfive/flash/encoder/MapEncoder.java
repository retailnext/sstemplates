package com.carbonfive.flash.encoder;

import flashgateway.io.ASObject;
import java.util.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java map to an ActionScript object.
 */
public class MapEncoder
  extends ActionScriptEncoder
{
  public Object encodeShell(Object decodedObject)
  {
    return new ASObject();
  }

  public Object encodeObject(Object shell, Object decodedObject)
  {
    Map      asMap         = (Map) decodedObject;
    ASObject encodedObject = (ASObject) shell;
    encodedObject.setType( decodedObject.getClass().getName() );

    Object              key          = null;
    Object              value        = null;
    Object              encodedValue = null;
    ActionScriptEncoder encoder      = null;
    for (Iterator i = asMap.keySet().iterator(); i.hasNext(); )
    {
      key = i.next();
      value = asMap.get(key);

      encoder = EncoderFactory.getInstance().getEncoder( value );
      encodedValue = encoder.encodeObject(value);
      encodedObject.put( key, encodedValue );
    }

    return encodedObject;
  }
}
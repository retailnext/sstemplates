package com.carbonfive.flash.encoder;

import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java number (any type) to an ActionScript number (a double).
 */
public class NumberEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    Number asNumber = (Number) decodedObject;
    return new Double( asNumber.doubleValue() );
  }
}
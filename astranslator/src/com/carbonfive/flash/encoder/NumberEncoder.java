package com.carbonfive.flash.encoder;

import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java number (any type) to an ActionScript number (a double).
 */
public class NumberEncoder
  implements ActionScriptEncoder
{
  public Object encodeShell(Object decodedObject)
  {
    return new Double(((Number) decodedObject).doubleValue());
  }

  public Object encodeObject(Object shell, Object decodedObject)
  {
    return shell;
  }
}
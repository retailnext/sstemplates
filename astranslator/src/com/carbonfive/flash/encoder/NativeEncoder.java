package com.carbonfive.flash.encoder;

import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java native object to an ActionScript native object.
 */
public class NativeEncoder
  implements ActionScriptEncoder
{
  public Object encodeShell(Object decodedObject)
  {
    return decodedObject;
  }

  public Object encodeObject(Object shell, Object decodedObject)
  {
    return decodedObject;
  }
}
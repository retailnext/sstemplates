package com.carbonfive.flash.decoder;

/**
 * Decodes an ActionScript native object to a Java native object.
 */
public class NativeDecoder
  implements ActionScriptDecoder
{
  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    return encodedObject;
  }

  public Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    return encodedObject;
  }
}
package com.carbonfive.flash.decoder;

/**
 * Decode an ActionScript object (of some type) to a Java object (of some type).
 */
public abstract class ActionScriptDecoder
{
  public abstract Object decodeShell(Object encodedObject, Class desiredClass);
  abstract Object decodeObject(Object shell, Object encodedObject, Class desiredClass);

  public Object decodeObject(Object encodedObject, Class desiredClass)
  {
    return decodeObject( decodeShell(encodedObject, desiredClass), encodedObject, desiredClass );
  }
}
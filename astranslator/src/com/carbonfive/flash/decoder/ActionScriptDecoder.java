package com.carbonfive.flash.decoder;

/**
 * Decode an ActionScript object (of some type) to a Java object (of some type).
 */
public interface ActionScriptDecoder
{
  public Object decodeShell(Object encodedObject, Class desiredClass);
  public Object decodeObject(Object shell, Object encodedObject, Class desiredClass);
}
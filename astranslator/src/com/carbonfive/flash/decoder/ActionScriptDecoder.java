package com.carbonfive.flash.decoder;

/**
 * Decode an ActionScript object (of some type) to a Java object (of some type).
 */
public interface ActionScriptDecoder
{
  public Object decodeObject( Object encodedObject, Class desiredClass );
}
package com.carbonfive.flash.encoder;

/**
 * Encode a Java object (of some type) to an ActionScript object (of some type).
 */
public interface ActionScriptEncoder
{
  public Object encodeShell( Object decodedObject );
  public Object encodeObject( Object shell, Object decodedObject );
}
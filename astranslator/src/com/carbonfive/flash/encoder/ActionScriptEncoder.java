package com.carbonfive.flash.encoder;

/**
 * Encode a Java object (of some type) to an ActionScript object (of some type).
 */
public abstract class ActionScriptEncoder
{
  public abstract Object encodeShell( Object decodedObject );
  public abstract Object encodeObject( Object shell, Object decodedObject );

  public Object encodeObject(Object decodedObject)
  {
    return encodeObject( encodeShell(decodedObject), decodedObject );
  }
}
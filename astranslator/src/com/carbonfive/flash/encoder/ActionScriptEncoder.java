package com.carbonfive.flash.encoder;

import com.carbonfive.flash.*;

/**
 * Encode a Java object (of some type) to an ActionScript object (of some type).
 */
public abstract class ActionScriptEncoder
{
  public abstract Object encodeShell( Context ctx, Object decodedObject );
  public abstract Object encodeObject( Context ctx, Object shell, Object decodedObject );

  public Object encodeObject(Context ctx, Object decodedObject)
  {
    return encodeObject( ctx, encodeShell(ctx, decodedObject), decodedObject );
  }
}
package com.carbonfive.flash.encoder;

import com.carbonfive.flash.*;

/**
 * Encodes a Java native object to an ActionScript native object.
 */
public class NativeEncoder
  extends ActionScriptEncoder
{
  public Object encodeShell(Context ctx, Object decodedObject)
  {
    return decodedObject;
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    return decodedObject;
  }
}
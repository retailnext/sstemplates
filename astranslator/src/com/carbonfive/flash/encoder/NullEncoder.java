package com.carbonfive.flash.encoder;

import com.carbonfive.flash.*;

/**
 * Encodes a Java native object to null.
 */
public class NullEncoder
  extends ActionScriptEncoder
{
  public Object encodeShell(Context ctx, Object decodedObject)
  {
    return null;
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    return null;
  }
}
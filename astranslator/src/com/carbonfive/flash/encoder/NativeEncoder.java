package com.carbonfive.flash.encoder;

import com.carbonfive.flash.*;
import org.apache.commons.logging.*;

/**
 * Encodes a Java native object to an ActionScript native object.
 */
public class NativeEncoder
  extends ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(NativeEncoder.class);

  /**
   * Passes decodedObject straight through.
   */
  public Object encodeShell(Context ctx, Object decodedObject)
  {
    if (decodedObject == null) return null;
    return decodedObject;
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    return shell;
  }
}
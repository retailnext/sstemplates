package com.carbonfive.flash.encoder;

import java.util.*;
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
   * Passes decodedObject straight through.  The only exception is that this method
   * transforms any object that extends Date into a regular Date object, as that is
   * all that Flash can handle.  Actually, passing the extended object will work but
   * is wasteful.
   */
  public Object encodeShell(Context ctx, Object decodedObject)
  {
    if (decodedObject == null) return null;

    if ((decodedObject instanceof Date) && ! decodedObject.getClass().equals(Date.class))
    {
      return new Date(((Date) decodedObject).getTime());
    }
    
    return decodedObject;
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    return shell;
  }
}
package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.util.*;

/**
 * Encodes a Java native object to an ActionScript native object.
 */
public class NativeEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    return decodedObject;
  }
}
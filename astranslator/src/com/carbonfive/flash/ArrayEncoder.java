package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.util.*;

/**
 * Encodes a Java array to an ActionScript list.
 */
public class ArrayEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    List serverObjectAsList = Arrays.asList( (Object[]) decodedObject );
    ActionScriptEncoder encoder   = EncoderFactory.getInstance().getEncoder( serverObjectAsList );
    
    return encoder.encodeObject( serverObjectAsList );
  }
}
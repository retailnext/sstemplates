package com.carbonfive.flash.encoder;

import java.util.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java array to an ActionScript list.
 */
public class ArrayEncoder
  implements ActionScriptEncoder
{
  public Object encodeShell(Object decodedObject)
  {
    List serverObjectAsList = Arrays.asList((Object[]) decodedObject);
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder( serverObjectAsList );
    return encoder.encodeShell(serverObjectAsList);
  }

  public Object encodeObject( Object shell, Object decodedObject )
  {
    List serverObjectAsList = Arrays.asList((Object[]) decodedObject);
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder( serverObjectAsList );
    return encoder.encodeObject( shell, serverObjectAsList );
  }
}
package com.carbonfive.flash.encoder;

import java.util.*;
import com.carbonfive.flash.*;

/**
 * Encodes a Java array to an ActionScript list.
 */
public class ArrayEncoder
  extends ActionScriptEncoder
{
  public Object encodeShell(Context ctx, Object decodedObject)
  {
    List serverObjectAsList = Arrays.asList((Object[]) decodedObject);
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(ctx, serverObjectAsList);
    return encoder.encodeShell(ctx, serverObjectAsList);
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    List serverObjectAsList = Arrays.asList((Object[]) decodedObject);
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(ctx, serverObjectAsList);
    return encoder.encodeObject(ctx, shell, serverObjectAsList);
  }
}
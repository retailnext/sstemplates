package com.carbonfive.flash.encoder;

import java.util.*;
import java.lang.reflect.*;
import com.carbonfive.flash.*;
import org.apache.commons.logging.*;

/**
 * Encodes a Java array to an ActionScript list.
 */
public class ArrayEncoder
  extends ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(ArrayEncoder.class);

  public Object encodeShell(Context ctx, Object decodedObject)
  {
    List serverObjectAsList = asList(decodedObject);
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(ctx, serverObjectAsList);
    return encoder.encodeShell(ctx, serverObjectAsList);
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    List serverObjectAsList = asList(decodedObject);
    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(ctx, serverObjectAsList);
    return encoder.encodeObject(ctx, shell, serverObjectAsList);
  }

  private List asList(Object array)
  {
    if (! array.getClass().isArray()) throw new IllegalStateException("Not an array: " + array);

    List list = new ArrayList();
    for (int i = 0; i < Array.getLength(array); i++ )
    {
      list.add(Array.get(array, i));
    }

    return list;
  }
}
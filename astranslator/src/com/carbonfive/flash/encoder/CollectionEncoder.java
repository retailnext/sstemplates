package com.carbonfive.flash.encoder;

import java.util.*;
import com.carbonfive.flash.*;

/**
 * Encodes a Java collection to an ActionScript list.
 */
public class CollectionEncoder
  extends ActionScriptEncoder
{
  public Object encodeShell(Context ctx, Object decodedObject)
  {
    return new ArrayList();
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    Collection asCollection = (Collection) decodedObject;

    List                list    = (List) shell;
    ActionScriptEncoder encoder = null;
    Object              decoded = null;
    Object              encoded = null;
    for (Iterator i = asCollection.iterator(); i.hasNext(); )
    {
      decoded = i.next();
      encoder = EncoderFactory.getInstance().getEncoder(ctx, decoded);
      encoded = encoder.encodeObject(ctx, decoded);
      list.add(encoded);
    }
    return list;
  }
}

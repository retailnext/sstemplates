package com.carbonfive.flash.encoder;

import java.util.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java collection to an ActionScript list.
 */
public class CollectionEncoder
  implements ActionScriptEncoder
{
  public Object encodeShell(Object decodedObject)
  {
    return new ArrayList();
  }

  public Object encodeObject(Object shell, Object decodedObject)
  {
    Collection asCollection = (Collection) decodedObject;

    List                list    = (List) shell;
    ActionScriptEncoder encoder = null;
    Object              decoded = null;
    Object              encoded = null;
    for (Iterator i = asCollection.iterator(); i.hasNext(); )
    {
      decoded = i.next();
      encoder = EncoderFactory.getInstance().getEncoder( decoded );
      encoded = encoder.encodeObject( encoder.encodeShell(decoded), decoded );
      list.add( encoded );
    }
    return list;
  }
}

package com.carbonfive.flash.encoder;

import java.util.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java collection to an ActionScript list.
 */
public class CollectionEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    Collection asCollection = (Collection) decodedObject;

    ArrayList           list    = new ArrayList();
    ActionScriptEncoder encoder = null;
    Object              decoded = null;
    Object              encoded = null;
    for (Iterator i = asCollection.iterator(); i.hasNext(); )
    {
      decoded = i.next();
      encoder = EncoderFactory.getInstance().getEncoder( decoded );
      encoded = encoder.encodeObject( decoded );
      list.add( encoded );
    }
    return list;
  }
}

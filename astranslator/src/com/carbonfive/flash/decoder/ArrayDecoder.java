package com.carbonfive.flash.decoder;

import java.util.*;
import java.lang.reflect.*;

/**
 * Decodes an ActionScript list to a Java array.
 */
public class ArrayDecoder
  extends ActionScriptDecoder
{
  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    Class  arrayElementClass = desiredClass.getComponentType();
    int    size              = ( (List) encodedObject ).size();
    Object decodedArray      = Array.newInstance(arrayElementClass, size);
    return decodedArray;
  }

  protected Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    Class  arrayElementClass = desiredClass.getComponentType();
    Object decodedArray      = shell;
    Object encodedValue      = null;
    Object decodedValue      = null;

    ActionScriptDecoder decoder = null;
    int n = 0;
    for ( Iterator i = ((List) encodedObject).iterator(); i.hasNext(); )
    {
      encodedValue = i.next();
      decoder      = DecoderFactory.getInstance().getDecoder( encodedValue, arrayElementClass );
      decodedValue = decoder.decodeObject(encodedValue, arrayElementClass);
      Array.set(decodedArray, n++, decodedValue);
    }

    return decodedArray;
  }
}
package com.carbonfive.flash;

import java.util.*;
import java.lang.reflect.*;

public class ArrayDecoder
  implements ActionScriptDecoder
{
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    Class  arrayElementClass = desiredClass.getComponentType();
    int    size              = ( (List) encodedObject ).size();
    Object decodedArray      = Array.newInstance(arrayElementClass, size);
    Object encodedValue      = null;
    Object decodedValue      = null;
    int    n                 = 0;
    for ( Iterator i = ((List) encodedObject).iterator(); i.hasNext(); )
    {
      encodedValue      = i.next();
      decodedValue      = DecoderFactory.getInstance().getDecoder( encodedValue, arrayElementClass )
                                                      .decodeObject( encodedValue, arrayElementClass );
      Array.set(decodedArray, n++, decodedValue);
    }

    return decodedArray;
  }
}
package com.carbonfive.flash;

import java.util.*;
import java.beans.*;

public class CollectionDecoder
  implements ActionScriptDecoder
{
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    Collection    decodedCollection = createCollection(desiredClass);
    Object        decodedObject     = null;
    Object        obj               = null;
    for (Iterator i = ( (Collection) encodedObject ).iterator(); i.hasNext(); )
    {
      obj = i.next();
      decodedObject = DecoderFactory.getInstance().getDecoder( obj, obj.getClass() ).decodeObject( obj, obj.getClass()  );
      decodedCollection.add( decodedObject );
    }

    return decodedCollection;
  }

  /**
   * Create a new collection that is compatible with the Class passed in.
   */
  private Collection createCollection( Class clazz )
  {
    Collection col = null;
    
    try
    {
      boolean isList = List.class.isAssignableFrom( clazz );
      boolean isSet  = Set.class.isAssignableFrom( clazz );

      ClassLoader classLoader = this.getClass().getClassLoader();
      Object      colBean     = Beans.instantiate( classLoader, clazz.getName() );

      boolean isInterface = clazz.isInterface();

      if ( isList )
      {
        if ( isInterface ) col = new ArrayList();
        else               col = (List) colBean;
      }
      else if ( isSet )
      {
        if ( isInterface ) col = new HashSet();
        else               col = (Set) colBean;
      }
      else
      {
        return null;
      }
    }
    catch (Exception e)
    {
      return null;
    }
    
    return col;
  }
}
package com.carbonfive.flash;

import java.util.*;
import java.beans.*;

public class CollectionDecoder
  implements ActionScriptDecoder
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CollectionDecoder.class);
  
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
      boolean isList        = List.class.isAssignableFrom( clazz );
      boolean isSet         = Set.class.isAssignableFrom( clazz );
      boolean isCollection  = Collection.class.isAssignableFrom( clazz );

      ClassLoader classLoader = this.getClass().getClassLoader();

      boolean isInterface = clazz.isInterface();

      if ( isList )
      {
        if ( isInterface ) col = new ArrayList();
        else               col = (List) Beans.instantiate( classLoader, clazz.getName() );
      }
      else if ( isSet )
      {
        if ( isInterface ) col = new HashSet();
        else               col = (Set) Beans.instantiate( classLoader, clazz.getName() );
      }
      else if ( isCollection )
      {
        col = (Collection) new ArrayList();
      }
      else
      {
        return null;
      }
    }
    catch (Exception e)
    {
      log.error("Exception trying to create Collection", e);
      return null;
    }
    
    return col;
  }
}
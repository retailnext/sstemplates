package com.carbonfive.flash.decoder;

import java.util.*;
import java.beans.*;
import org.apache.log4j.*;

/**
 * Decodes an ActionScript list to a Java collection (list or set).
 */
public class CollectionDecoder
  implements ActionScriptDecoder
{
  private static final Logger log = Logger.getLogger(CollectionDecoder.class);
  
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    Collection    decodedCollection = createCollection(desiredClass);
    Object        decodedObject     = null;
    Object        obj               = null;

    // forceClass allows us to heuristically guess that a Collection full of
    // Doubles that map VERY closely to Integers is really a Collection full
    // of Integers.
    Class forceClass = null;
    if (isFullOfIntegers((Collection) encodedObject)) forceClass = Integer.class;

    Class desiredObjClass = null;
    for (Iterator i = ( (Collection) encodedObject ).iterator(); i.hasNext(); )
    {
      obj = i.next();

      desiredObjClass = ( forceClass == null ? obj.getClass() : forceClass );

      decodedObject = DecoderFactory.getInstance().getDecoder( obj, desiredObjClass ).decodeObject( obj, desiredObjClass );
      decodedCollection.add( decodedObject );
    }

    return decodedCollection;
  }

  private boolean isFullOfIntegers(Collection c)
  {
    Object  obj = null;
    Double  dbl = null;
    for (Iterator i = c.iterator(); i.hasNext(); )
    {
      obj = i.next();
      if (! (obj instanceof Double)) return false;
      dbl = (Double) obj;
      if (! isReallyAnInteger(dbl)) return false;
    }
    return true;
  }

  private boolean isReallyAnInteger(Double d)
  {
    return d.equals(new Double(d.intValue()));
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
        col = new ArrayList();
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
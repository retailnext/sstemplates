package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.util.*;
import java.beans.*;

public class CollectionTranslator
  extends AbstractTranslator
{
  
  public CollectionTranslator(ASTranslator astranslator)
  {
    super(astranslator);
  }

  public Object translateForClient( Object serverObject )
  {
    Collection serverObjectAsCollection = ( Collection ) serverObject;

    ArrayList    list       = new ArrayList();
    Object       translated = null;
    for (Iterator i = serverObjectAsCollection.iterator(); i.hasNext(); )
    {
      translated = getASTranslator().toActionScript( i.next() );
      list.add( translated );
    }
    return list;
  }

//------------------------------------------------------------------------------

  public Object translateForServer( Object clientObject, Class clazz )
  {
    Collection    serverCollection  = createServerCollection(clazz);
    Object        translated        = null;
    Object        obj               = null;
    for (Iterator items = ( (Collection) clientObject ).iterator(); items.hasNext(); )
    {
      obj = items.next();
      translated = getASTranslator().fromActionScript( obj, obj.getClass()  );
      serverCollection.add( translated );
    }

    return serverCollection;
  }

  private Collection createServerCollection( Class clazz )
  {
    Collection  serverCollection = null;
    
    try
    {
      boolean isList = List.class.isAssignableFrom( clazz );
      boolean isSet = Set.class.isAssignableFrom( clazz );

      ClassLoader translatorClassLoader = ASTranslator.class.getClassLoader();
      Object clientCollection = Beans.instantiate( translatorClassLoader, clazz.getName() );

      boolean isInterface = clazz.isInterface();

      if ( isList )
      {
        if ( isInterface ) serverCollection = new ArrayList();
        else               serverCollection = (List) clientCollection;
      }
      else if ( isSet )
      {
        if ( isInterface ) serverCollection = new HashSet();
        else               serverCollection = (Set) clientCollection;
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
    
    return serverCollection;
  }

}
package com.carbonfive.flash;

import java.util.*;
import java.beans.*;
import flashgateway.io.ASObject;

public class MapTranslator
  extends AbstractTranslator
{

  public MapTranslator(ASTranslator astranslator)
  {
    super(astranslator);
  }

  public Object translateToActionScript( Object serverObject )
  {
    Class serverObjectClass = serverObject.getClass();
    Map serverObjectAsMap = (Map) serverObject;

    ASObject actionScriptMap = new ASObject();
    actionScriptMap.setType( serverObjectClass.getName() );

    Object        translated = null;
    Object        key    = null;
    Object        value  = null;
    for (Iterator i = serverObjectAsMap.keySet().iterator(); i.hasNext(); )
    {
      key = i.next();
      value = serverObjectAsMap.get(key);

      translated = getASTranslator().toActionScript( value );
      actionScriptMap.put( key, translated );
    }

    return actionScriptMap;
  }

//------------------------------------------------------------------------------

  public Object translateFromActionScript( Object clientObject, Class clazz  )
  {
    Map map = null;
    try
    {
      ClassLoader translatorClassLoader = ASTranslator.class.getClassLoader();
      Object obj = Beans.instantiate( translatorClassLoader, clazz.getName() );

      if (clazz.isInterface()) map = new HashMap();
      else                     map = (Map) obj;
    }
    catch (Exception e)
    {
      return null;
    }

    Object        translated = null;
    Object        key    = null;
    Object        value  = null;
    for (Iterator keys = ((Map) clientObject).keySet().iterator(); keys.hasNext(); )
    {
      key = keys.next();
      value = ( (Map) clientObject ).get( key );

      translated = getASTranslator().fromActionScript( value, value.getClass()  );

      map.put( key, translated );
    }
    
    return map;
  }

}
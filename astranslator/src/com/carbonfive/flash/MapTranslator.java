package com.carbonfive.flash;

import java.util.*;
import java.beans.*;
import flashgateway.io.ASObject;

public class MapTranslator
  extends AbstractTranslator
{

  public MapTranslator(ASTranslator ast, Object obj, Class c)
  {
    super(ast, obj, c);
  }

  public Object translateToActionScript( )
  {
    Class serverObjectClass = getObject().getClass();
    Map serverObjectAsMap = (Map) getObject();

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

  public Object translateFromActionScript( )
  {
    Map map = null;
    try
    {
      ClassLoader translatorClassLoader = ASTranslator.class.getClassLoader();
      Object obj = Beans.instantiate( translatorClassLoader, getDesiredClass().getName() );

      if (getDesiredClass().isInterface()) map = new HashMap();
      else                                 map = (Map) obj;
    }
    catch (Exception e)
    {
      return null;
    }

    Object        translated = null;
    Object        key    = null;
    Object        value  = null;
    for (Iterator keys = ((Map) getObject()).keySet().iterator(); keys.hasNext(); )
    {
      key = keys.next();
      value = ( (Map) getObject() ).get( key );

      translated = getASTranslator().fromActionScript( value, value.getClass()  );

      map.put( key, translated );
    }
    
    return map;
  }

}
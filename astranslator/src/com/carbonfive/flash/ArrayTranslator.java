package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.util.*;

public class ArrayTranslator
  extends AbstractTranslator
{

  public ArrayTranslator(ASTranslator astranslator)
  {
    super(astranslator);
  }
  
  public Object translateForClient( Object serverObject )
  {
    List serverObjectAsList = Arrays.asList( (Object[]) serverObject );
    Translator translator   = TranslatorFactory.getInstance().getTranslator( getASTranslator(), serverObjectAsList );
    
    return translator.translateForClient( serverObjectAsList );
  }

//------------------------------------------------------------------------------

  public Object translateForServer( Object clientObject, Class clazz  )
  {

    Class         desiredBeanClass  = clientObject.getClass().getComponentType();
    int           size              = ( (List) clientObject ).size();
    Object[]      serverArray       = new Object[size];
    Object        translated        = null;
    int           n                 = 0;
    for ( Iterator i = ( (List) clientObject).iterator(); i.hasNext(); )
    {
      translated        = getASTranslator().fromActionScript( i.next(), desiredBeanClass );
      serverArray[n++]  = translated;
    }

    return serverArray;
  }

}
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
  
  public Object translateToActionScript( Object serverObject )
  {
    List serverObjectAsList = Arrays.asList( (Object[]) serverObject );
    Translator translator   = TranslatorFactory.getInstance().getServerTranslator( getASTranslator(), serverObjectAsList );
    
    return translator.translateToActionScript( serverObjectAsList );
  }

//------------------------------------------------------------------------------

  public Object translateFromActionScript( Object clientObject, Class clazz  )
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
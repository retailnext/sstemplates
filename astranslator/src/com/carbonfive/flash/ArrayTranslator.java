package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.util.*;

public class ArrayTranslator
  extends AbstractTranslator
{

  public ArrayTranslator(ASTranslator ast, Object obj, Class c)
  {
    super(ast, obj, c);
  }
  
  public Object translateToActionScript( )
  {
    List serverObjectAsList = Arrays.asList( (Object[]) getObject() );
    Translator translator   = TranslatorFactory.getInstance().getTranslatorToActionScript( getASTranslator(), serverObjectAsList );
    
    return translator.translateToActionScript( );
  }

//------------------------------------------------------------------------------

  public Object translateFromActionScript( )
  {

    Class         desiredBeanClass  = getObject().getClass().getComponentType();
    int           size              = ( (List) getObject() ).size();
    Object[]      serverArray       = new Object[size];
    Object        translated        = null;
    int           n                 = 0;
    for ( Iterator i = ( (List) getObject()).iterator(); i.hasNext(); )
    {
      translated        = getASTranslator().fromActionScript( i.next(), getDesiredClass() );
      serverArray[n++]  = translated;
    }

    return serverArray;
  }

}
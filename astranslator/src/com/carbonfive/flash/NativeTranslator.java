package com.carbonfive.flash;

import flashgateway.io.ASObject;

public class NativeTranslator
  extends AbstractTranslator
{
  
  public NativeTranslator(ASTranslator ast, Object obj, Class c)
  {
    super(ast, obj, c);
  }

  public Object translateToActionScript( )
  {
    return getObject();
  }

//------------------------------------------------------------------------------

  public Object translateFromActionScript( )
  {
    return getObject();
  }

}
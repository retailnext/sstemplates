package com.carbonfive.flash;

import flashgateway.io.ASObject;

public class NativeTranslator
  extends AbstractTranslator
{
  
  public NativeTranslator(ASTranslator astranslator)
  {
    super(astranslator);
  }

  public Object translateToActionScript( Object serverObject )
  {
    return serverObject;
  }

//------------------------------------------------------------------------------

  public Object translateFromActionScript( Object clientObject, Class clazz )
  {
    return clientObject;
  }

}
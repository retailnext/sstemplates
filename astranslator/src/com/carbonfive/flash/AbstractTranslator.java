package com.carbonfive.flash;

public abstract class AbstractTranslator
  implements Translator
{
  private ASTranslator astranslator = null;
  private Object       object       = null;
  private Class        desiredClass = null;
  
  protected AbstractTranslator(ASTranslator ast, Object obj, Class c)
  {
    astranslator = ast;
    object       = obj;
    desiredClass = c;
  }
  
  protected ASTranslator getASTranslator()
  {
    return astranslator;
  }

  protected Object getObject()
  {
    return object;
  }

  protected Class getDesiredClass()
  {
    return desiredClass;
  }
}
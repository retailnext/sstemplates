package com.carbonfive.flash;

public abstract class AbstractTranslator
  implements Translator
{
  private ASTranslator astranslator = null;
  
  public AbstractTranslator(ASTranslator ast)
  {
    astranslator = ast;
  }
  
  protected ASTranslator getASTranslator()
  {
    return astranslator;
  }
}
package com.carbonfive.flash;

public interface Translator
{
  public Object translateToActionScript( Object serverObject );
  public Object translateFromActionScript( Object clientObject, Class clazz );
}
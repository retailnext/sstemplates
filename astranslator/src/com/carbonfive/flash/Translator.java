package com.carbonfive.flash;

public interface Translator
{
  public Object translateForClient( Object serverObject );
  public Object translateForServer( Object clientObject, Class clazz );
}
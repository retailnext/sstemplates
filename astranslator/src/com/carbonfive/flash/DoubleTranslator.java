package com.carbonfive.flash;

import flashgateway.io.ASObject;

public class DoubleTranslator
  extends AbstractTranslator
{
  
  public DoubleTranslator(ASTranslator astranslator)
  {
    super(astranslator);
  }

  public Object translateForClient( Object serverObject )
  {
    Number serverObjectAsNumber = (Number) serverObject;
    return new Double( serverObjectAsNumber.doubleValue() );
  }

//------------------------------------------------------------------------------

  public Object translateForServer( Object clientObject, Class clazz )
  {
    Double dbl = (Double) clientObject;

    boolean isShort   = ( Short.class.equals( clazz )   || Short.TYPE.equals( clazz ) );
    boolean isInteger = ( Integer.class.equals( clazz ) || Integer.TYPE.equals( clazz ) );
    boolean isLong    = ( Long.class.equals( clazz )    || Long.TYPE.equals( clazz ) );
    boolean isFloat   = ( Float.class.equals( clazz )   || Float.TYPE.equals( clazz ) );
    boolean isDouble  = ( Double.class.equals( clazz )  || Double.TYPE.equals( clazz ) );

    Object result = null;
    if ( isShort )
    {
      result = new Short(dbl.shortValue());
    }
    else if ( isInteger )
    {
      result = new Integer(dbl.intValue());
    }
    else if ( isLong )
    {
      result = new Long(dbl.longValue());
    }
    else if ( isFloat )
    {
      result = new Float(dbl.floatValue());
    }
    else if ( isDouble )
    {
      result = dbl;
    }

    return result;
  }


}
package com.carbonfive.flash;

import java.math.*;
import flashgateway.io.ASObject;

public class NumberTranslator
  extends AbstractTranslator
{
  
  public NumberTranslator(ASTranslator ast, Object obj, Class c)
  {
    super(ast, obj, c);
  }

  public Object translateToActionScript( )
  {
    Number serverObjectAsNumber = (Number) getObject();
    return new Double( serverObjectAsNumber.doubleValue() );
  }

//------------------------------------------------------------------------------

  public Object translateFromActionScript( )
  {
    Double dbl = (Double) getObject();
    boolean isByte        = ( Byte.class.equals( getDesiredClass() )        || Byte.TYPE.equals( getDesiredClass() ) );
    boolean isShort       = ( Short.class.equals( getDesiredClass() )       || Short.TYPE.equals( getDesiredClass() ) );
    boolean isInteger     = ( Integer.class.equals( getDesiredClass() )     || Integer.TYPE.equals( getDesiredClass() ) );
    boolean isLong        = ( Long.class.equals( getDesiredClass() )        || Long.TYPE.equals( getDesiredClass() ) );
    boolean isFloat       = ( Float.class.equals( getDesiredClass() )       || Float.TYPE.equals( getDesiredClass() ) );
    boolean isDouble      = ( Double.class.equals( getDesiredClass() )      || Double.TYPE.equals( getDesiredClass() ) );
    boolean isBigDecimal  = ( BigDecimal.class.equals( getDesiredClass() ) );

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
    else if ( isBigDecimal )
    {
      result = new BigDecimal(dbl.doubleValue());
    }

    return result;
  }


}
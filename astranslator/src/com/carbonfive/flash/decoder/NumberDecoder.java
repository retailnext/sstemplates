package com.carbonfive.flash.decoder;

import java.math.*;

/**
 * Decode an ActionScript number (a double) to a Java number (of any type).
 */
public class NumberDecoder
  extends ActionScriptDecoder
{
  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    Double dbl = (Double) encodedObject;
    boolean isByte        = ( Byte.class.equals( desiredClass )        || Byte.TYPE.equals( desiredClass ) );
    boolean isShort       = ( Short.class.equals( desiredClass )       || Short.TYPE.equals( desiredClass ) );
    boolean isInteger     = ( Integer.class.equals( desiredClass )     || Integer.TYPE.equals( desiredClass ) );
    boolean isLong        = ( Long.class.equals( desiredClass )        || Long.TYPE.equals( desiredClass ) );
    boolean isFloat       = ( Float.class.equals( desiredClass )       || Float.TYPE.equals( desiredClass ) );
    boolean isDouble      = ( Double.class.equals( desiredClass )      || Double.TYPE.equals( desiredClass ) );
    boolean isBigDecimal  = ( BigDecimal.class.equals( desiredClass ) );

    Object result = null;
    if      ( isByte )       result = new Byte(dbl.byteValue());
    else if ( isShort )      result = new Short(dbl.shortValue());
    else if ( isInteger )    result = new Integer(dbl.intValue());
    else if ( isLong )       result = new Long(dbl.longValue());
    else if ( isFloat )      result = new Float(dbl.floatValue());
    else if ( isDouble )     result = dbl;
    else if ( isBigDecimal ) result = new BigDecimal(dbl.doubleValue());

    return result;
  }

  Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    return shell;
  }
}
package com.carbonfive.flash.decoder;

import java.util.*;
import java.math.*;
import org.apache.commons.logging.*;

/**
 * Decodes an ActionScript native object to a Java native object.
 */
public class NativeDecoder
  extends ActionScriptDecoder
{
  private static final Log log = LogFactory.getLog(NativeDecoder.class);

  /**
   * This method attempts to create a Java object from an ActionScript object that is
   * supposedly (according to the Macromedia AMF rules) alread the appropriate Java
   * object.  But, it seems some users don't match their ActionScript objects with their
   * Java objects perfectly, and so we get Strings coming in through AMF instead of Doubles
   * (for example).  So we will perform some rudimentary conversion logic and exception
   * handling on Strings.
   *
   * @param encodedObject The ActionScript native object
   * @param desiredClass The class we want to translate in to
   * @return The Java object decoded from the ActionScript native object
   */
  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    if (encodedObject == null) return null;

    if ((encodedObject instanceof String) && ! String.class.equals(desiredClass))
    {
      log.info("Converting ActionScript string object to non-string Java object: " + encodedObject +
               " --> " + desiredClass.getName());

      boolean isBoolean    = Boolean.class.equals(desiredClass)   || Boolean.TYPE.equals(desiredClass);
      boolean isChar       = Character.class.equals(desiredClass) || Character.TYPE.equals(desiredClass);
      boolean isByte       = Byte.class.equals(desiredClass)      || Byte.TYPE.equals(desiredClass);
      boolean isShort      = Short.class.equals(desiredClass)     || Short.TYPE.equals(desiredClass);
      boolean isInteger    = Integer.class.equals(desiredClass)   || Integer.TYPE.equals(desiredClass);
      boolean isLong       = Long.class.equals(desiredClass)      || Long.TYPE.equals(desiredClass);
      boolean isFloat      = Float.class.equals(desiredClass)     || Float.TYPE.equals(desiredClass);
      boolean isDouble     = Double.class.equals(desiredClass)    || Double.TYPE.equals(desiredClass);
      boolean isBigDecimal = BigDecimal.class.isAssignableFrom(desiredClass); // BigDecimal is not final
      boolean isDate       = Date.class.isAssignableFrom(desiredClass); // Date is not final

      try
      {
        Object result = null;
        if      ( isBoolean )    result = Boolean.valueOf((String) encodedObject);
        else if ( isChar )       result = getChar((String) encodedObject);
        else if ( isByte )       result = Byte.valueOf((String) encodedObject);
        else if ( isShort )      result = Short.valueOf((String) encodedObject);
        else if ( isInteger )    result = Integer.valueOf((String) encodedObject);
        else if ( isLong )       result = Long.valueOf((String) encodedObject);
        else if ( isFloat )      result = Float.valueOf((String) encodedObject);
        else if ( isDouble )     result = Double.valueOf((String) encodedObject);
        else if ( isBigDecimal ) result = new BigDecimal((String) encodedObject);
        else if ( isDate )       result = new Date(Long.parseLong((String) encodedObject));
        return result;
      }
      catch (NumberFormatException e)
      {
        NumberFormatException nfe = new NumberFormatException("Error converting encoded String object to a number: " + encodedObject);
        nfe.setStackTrace(e.getStackTrace());
        throw nfe;
      }
    }

    return encodedObject;
  }

  Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    return shell;
  }

  private Character getChar(String str)
  {
    if (str == null) return null;
    if (str.length() == 0) return null;
    Character c = new Character(str.charAt(0));
    if (str.length() > 1) log.warn("Converting multi-character string to char: " + str + " --> " + c);
    return c;
  }
}
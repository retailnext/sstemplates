package com.carbonfive.flash.decoder;

import java.util.*;
import java.lang.reflect.*;
import org.apache.commons.logging.*;
import com.carbonfive.flash.*;

public class DateDecoder
  extends ActionScriptDecoder
{
  private static final Log log = LogFactory.getLog(DateDecoder.class);

  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    Date date = (Date) encodedObject;
    if (! Date.class.equals(desiredClass))
    {
      try
      {
        Constructor constructor = desiredClass.getConstructor(new Class[] { Long.TYPE });
        Object desiredDate = constructor.newInstance(new Object[] { new Long(date.getTime()) });
        return desiredDate;
      }
      catch (Exception e)
      {
        throw new ASTranslationException("Classes that extend java.util.Date must have a constructor that takes a single 'long' parameter: " + desiredClass.getName(), e);
      }
    }

    return date;
  }

  Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    return shell;
  }
}

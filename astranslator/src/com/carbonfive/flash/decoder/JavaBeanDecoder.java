package com.carbonfive.flash.decoder;

import java.lang.reflect.*;
import java.beans.*;
import flashgateway.io.ASObject;
import org.apache.commons.beanutils.*;
import org.apache.commons.logging.*;
import com.carbonfive.flash.*;

/**
 * Decodes an ActionScript object to a Java object.
 */
public class JavaBeanDecoder
  extends ActionScriptDecoder
{
  private static final Log log = LogFactory.getLog(JavaBeanDecoder.class);

  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    ASObject aso  = (ASObject) encodedObject;
    String   type = aso.getType();

    try
    {
      Object bean = Beans.instantiate(ASTranslator.class.getClassLoader(), type);
      return bean;
    }
    catch (Exception e) // ClassNotFoundException, IOException, IntrospectionException
    {
      log.warn("Cannot create bean: " + type);
      return null;
    }
  }

  Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
  {
    ASObject aso  = (ASObject) encodedObject;
    Object   bean = shell;
    if (bean == null) return null;

    PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);

    ActionScriptDecoder decoder = null;
    Object decodedObject = null;
    String name   = null;
    Method write  = null;
    Object value  = null;
    Class  wClass = null;
    for (int i = 0; i < pds.length; i++)
    {
      name  = pds[i].getName();

      wClass = pds[i].getPropertyType();
      write  = pds[i].getWriteMethod();

      if (write == null)
      {
        continue;
      }

      // get property value from ASObject
      value = aso.get(name);
      if (value == null)
      {
        continue;
      }

      try
      {
        decoder = DecoderFactory.getInstance().getDecoder( value, wClass );
        decodedObject = decoder.decodeObject(value, wClass);
        write.invoke(bean, new Object[] { decodedObject });
      }
      catch (Exception e) // Method.invoke() stuff
      {
        continue;
      }
    }
    
    return bean;
  }
}

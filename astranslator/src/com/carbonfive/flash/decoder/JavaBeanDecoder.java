package com.carbonfive.flash.decoder;

import flashgateway.io.*;
import org.apache.commons.beanutils.*;
import org.apache.commons.logging.*;

import java.beans.*;
import java.lang.reflect.*;

/**
 * Decodes an ActionScript object to a Java object.
 */
public class JavaBeanDecoder
  extends ActionScriptDecoder
{
  private static final Log log = LogFactory.getLog(JavaBeanDecoder.class);

  public Object decodeShell(Object encodedObject, Class desiredClass)
  {
    try
    {
      Class classToTranslateInto = desiredClass;
      if (desiredClass.isInterface()) classToTranslateInto = DecoderFactory.decideClassToTranslateInto(encodedObject);

      Object bean = Beans.instantiate(Thread.currentThread().getContextClassLoader(), classToTranslateInto.getName());
      return bean;
    }
    catch (Exception e) // ClassNotFoundException, IOException, IntrospectionException
    {
      log.warn("Cannot create bean: " + desiredClass.getName());
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
    Class  propertyType = null;
    for (int i = 0; i < pds.length; i++)
    {
      name  = pds[i].getName();

      propertyType = pds[i].getPropertyType();
      write = pds[i].getWriteMethod();

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

      Class asoType = DecoderFactory.decideClassToTranslateInto(value);
      if (propertyType.isAssignableFrom(asoType)) propertyType = asoType;

      try
      {
        decoder = DecoderFactory.getInstance().getDecoder( value, propertyType );
        decodedObject = decoder.decodeObject(value, propertyType);
        write.invoke(bean, new Object[] { decodedObject });
      }
      catch (Exception e) // Method.invoke() stuff
      {
        warn("Unable to invoke method: " + bean.getClass().getName() + "." + write.getName(), e);
        continue;
      }
    }
    
    return bean;
  }

  private void warn(String message, Exception e)
  {
    if (log.isDebugEnabled()) log.debug(message, e);
    else                      log.warn(message);
  }
}

package com.carbonfive.flash;

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;
import flashgateway.io.ASObject;

public class JavaBeanDecoder
  implements ActionScriptDecoder
{
  public Object decodeObject( Object encodedObject, Class desiredClass )
  {
    ASObject aso  = (ASObject) encodedObject;
    String   type = aso.getType();

    Object   bean = null;
    BeanInfo info = null;
    try
    {
      bean = Beans.instantiate(ASTranslator.class.getClassLoader(), type);
      info = Introspector.getBeanInfo(bean.getClass(), Object.class);
    }
    catch (Exception e) // ClassNotFoundException, IOException, IntrospectionException
    {
      return null;
    }

    PropertyDescriptor[] pds = info.getPropertyDescriptors();

    String        name       = null;
    Method        write      = null;
    Object        value      = null;
    Class         wClass     = null;
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
        Object decodedObject = DecoderFactory.getInstance().getDecoder( value, wClass ).decodeObject( value, wClass  );
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

package com.carbonfive.flash;

import flashgateway.io.ASObject;
import java.lang.reflect.*;
import java.beans.*;
import java.util.*;

public class JavaBeanTranslator
  extends AbstractTranslator
{

  public JavaBeanTranslator(ASTranslator astranslator)
  {
    super(astranslator);
  }

  public Object translateForClient( Object serverObject )
  {
      Class clazz = serverObject.getClass();
      String serverObjectType = clazz.getName();

      ASObject actionScriptObject = new ASObject();
      actionScriptObject.setType( serverObjectType );
      actionScriptObject = populate( serverObject, actionScriptObject );


      return actionScriptObject;
  }

//------------------------------------------------------------------------------

  public Object translateForServer( Object clientObject, Class clazz )
  {
    ASObject aso = (ASObject) clientObject;
    String type = aso.getType();

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
        Object translated = getASTranslator().fromActionScript( value, wClass  );
        write.invoke(bean, new Object[] { translated });
      }
      catch (Exception e) // Method.invoke() stuff
      {
        continue;
      }
    }
    
    return bean;
  }

//------------------------------------------------------------------------------

  private ASObject populate( Object fromServerObject, ASObject toActionScriptObject )
  {
      Object               translated     = null;
      String               attributeName  = null;
      Object               attributeValue = null;
      Method               getter         = null;
      PropertyDescriptor[] attributes = getAttributes( fromServerObject );
      for (int i = 0; i < attributes.length; i++)
      {
        attributeName  = attributes[i].getName();
        getter  = attributes[i].getReadMethod();

        if ( getter == null )  continue;

        try
        {
          attributeValue = getter.invoke( fromServerObject, null );
        }
        catch ( Exception failedToInvoke )
        {
          continue;
        }

        translated = getASTranslator().toActionScript( attributeValue );
        toActionScriptObject.put( attributeName, translated );
      }

      return toActionScriptObject;
  }

//------------------------------------------------------------------------------

  private PropertyDescriptor[] getAttributes( Object serverObject )
  {
      BeanInfo beanInformation = null;
      try
      {
        // introspect the bean, but don't go down to Object level
        Class noIntrospectionOfObjects = Object.class;
        beanInformation = Introspector.getBeanInfo( serverObject.getClass(),
                                                    noIntrospectionOfObjects );
      }
      catch (IntrospectionException ie)
      {
        return null;
      }

      PropertyDescriptor[] attributes = beanInformation.getPropertyDescriptors();

      return attributes;
  }

}
package com.carbonfive.flash;

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;
import flashgateway.io.ASObject;

/**
 * Encodes a Java object to an ActionScript object.
 */
public class JavaBeanEncoder
  implements ActionScriptEncoder
{
  public Object encodeObject( Object decodedObject )
  {
    ASObject encodedObject = new ASObject();
    encodedObject.setType( decodedObject.getClass().getName() );
    encodedObject = populate( decodedObject, encodedObject );

    return encodedObject;
  }

//------------------------------------------------------------------------------

  private ASObject populate( Object decoded, ASObject encoded )
  {
    Object               encodedAttributeValue = null;
    String               attributeName         = null;
    Object               attributeValue        = null;
    Method               getter                = null;
    PropertyDescriptor[] attributes            = getAttributes( decoded );
    for (int i = 0; i < attributes.length; i++)
    {
      attributeName  = attributes[i].getName();
      getter  = attributes[i].getReadMethod();

      if ( getter == null )  continue;

      try
      {
        attributeValue = getter.invoke( decoded, null );
      }
      catch ( Exception failedToInvoke )
      {
        continue;
      }

      encodedAttributeValue = EncoderFactory.getInstance().getEncoder( attributeValue ).encodeObject( attributeValue );
      encoded.put( attributeName, encodedAttributeValue );
    }

    return encoded;
  }

//------------------------------------------------------------------------------

  private PropertyDescriptor[] getAttributes( Object object )
  {
    BeanInfo beanInformation = null;
    try
    {
      // introspect the bean, but don't go down to Object level
      Class noIntrospectionOfObjects = Object.class;
      beanInformation = Introspector.getBeanInfo( object.getClass(),
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

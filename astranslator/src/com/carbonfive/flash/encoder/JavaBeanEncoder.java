package com.carbonfive.flash.encoder;

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;
import flashgateway.io.ASObject;
import org.apache.commons.beanutils.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java object to an ActionScript object.
 */
public class JavaBeanEncoder
  implements ActionScriptEncoder
{
  private static Set objectAttributes = new HashSet(Arrays.asList(PropertyUtils.getPropertyDescriptors(Object.class)));

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
    PropertyDescriptor[] attributes            = PropertyUtils.getPropertyDescriptors(decoded);
    for (int i = 0; i < attributes.length; i++)
    {
      if (objectAttributes.contains(attributes[i])) continue;
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
}

package com.carbonfive.flash.encoder;

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;
import flashgateway.io.ASObject;
import org.apache.commons.beanutils.*;
import org.apache.commons.logging.*;
import com.carbonfive.flash.encoder.*;

/**
 * Encodes a Java object to an ActionScript object.
 */
public class JavaBeanEncoder
  implements ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(JavaBeanEncoder.class);

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
      if (isDangerous(decoded, attributes[i])) continue;

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

  private boolean isDangerous(Object obj, PropertyDescriptor pd)
  {
    return dangerous.contains(obj.getClass().getName() + "." + pd.getName());
  }

  private static Set dangerous = new HashSet(Arrays.asList(new String[]
  {
    "java.io.File.parentFile", "java.io.File.canonicalFile", "java.io.File.absoluteFile"
  }));
}

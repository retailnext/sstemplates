package com.carbonfive.flash.encoder;

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;
import flashgateway.io.ASObject;
import org.apache.commons.beanutils.*;
import org.apache.commons.logging.*;
import com.carbonfive.flash.*;

/**
 * Encodes a Java object to an ActionScript object.
 */
public class JavaBeanEncoder
  extends ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(JavaBeanEncoder.class);

  private static Set objectAttributes = new HashSet(Arrays.asList(PropertyUtils.getPropertyDescriptors(Object.class)));

  public Object encodeShell(Context ctx, Object decodedObject)
  {
    return new ASObject();
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    ASObject encodedObject = (ASObject) shell;
    encodedObject.setType( decodedObject.getClass().getName() );
    encodedObject = populate(ctx, decodedObject, encodedObject);

    return encodedObject;
  }

//------------------------------------------------------------------------------

  private ASObject populate(Context ctx, Object decoded, ASObject encoded)
  {
    Object               encodedAttributeValue = null;
    String               attributeName         = null;
    Object               attributeValue        = null;
    Method               getter                = null;
    ActionScriptEncoder  encoder               = null;
    PropertyDescriptor[] attributes            = PropertyUtils.getPropertyDescriptors(decoded);
    for (int i = 0; i < attributes.length; i++)
    {
      if (objectAttributes.contains(attributes[i])) continue;

      if (ctx.getFilter().doIgnoreProperty(decoded.getClass(), attributes[i].getName())) continue;

      attributeName  = attributes[i].getName();
      getter  = attributes[i].getReadMethod();

      if ( getter == null )  continue;

      if (log.isDebugEnabled()) log.debug("Encoding " + attributeName);

      try
      {
        attributeValue = getter.invoke( decoded, null );
      }
      catch ( Exception failedToInvoke )
      {
        continue;
      }

      encoder = EncoderFactory.getInstance().getEncoder(ctx, attributeValue);
      encodedAttributeValue = encoder.encodeObject(ctx, attributeValue);
      encoded.put(attributeName, encodedAttributeValue);
    }

    return encoded;
  }
}

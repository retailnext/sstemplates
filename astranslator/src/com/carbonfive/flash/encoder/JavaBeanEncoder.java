package com.carbonfive.flash.encoder;

import com.carbonfive.flash.*;
import flashgateway.io.*;
import org.apache.commons.beanutils.*;
import org.apache.commons.logging.*;

import java.beans.*;
import java.lang.reflect.*;

/**
 * Encodes a Java object to an ActionScript object.
 */
public class JavaBeanEncoder
  extends ActionScriptEncoder
{
  private static final Log log = LogFactory.getLog(JavaBeanEncoder.class);

  public Object encodeShell(Context ctx, Object decodedObject)
  {
    return new ASObject();
  }

  public Object encodeObject(Context ctx, Object shell, Object decodedObject)
  {
    ASObject encodedObject = (ASObject) shell;
    encodedObject.setType( decodedObject.getClass().getName() );

    ctx.getLoopFinder().stepIn();
    encodedObject = populate(ctx, decodedObject, encodedObject);
    ctx.getLoopFinder().stepOut();

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
      if (ctx.getFilter().doIgnoreProperty(decoded.getClass(), attributes[i].getName())) continue;

      attributeName  = attributes[i].getName();
      getter  = attributes[i].getReadMethod();

      if ( getter == null ) continue;

      if ( ctx.getFilter().doIgnoreClass(getter.getReturnType()) ) continue;

      try
      {
        attributeValue = getter.invoke( decoded, null );
      }
      catch ( Exception failedToInvoke )
      {
        warn("Failed to invoke getter: " + decoded.getClass().getName() + "." + getter.getName(), failedToInvoke);
        continue;
      }

      encoder = EncoderFactory.getInstance().getEncoder(ctx, attributeValue);
      encodedAttributeValue = encoder.encodeObject(ctx, attributeValue);
      if (encodedAttributeValue != null) encoded.put(attributeName, encodedAttributeValue);
    }

    return encoded;
  }

  private void warn(String message, Exception e)
  {
    if (log.isDebugEnabled()) log.debug(message, e);
    else                      log.warn(message);
  }
}

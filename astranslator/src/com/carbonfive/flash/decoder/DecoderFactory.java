package com.carbonfive.flash.decoder;

import java.util.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.encoder.*;
import com.carbonfive.flash.*;
import org.apache.commons.logging.*;

public class DecoderFactory
{
  private static final Log log = LogFactory.getLog(DecoderFactory.class);
  
  private static DecoderFactory instance;

  private DecoderFactory() { }

  public static synchronized DecoderFactory getInstance()
  {
    if (instance == null) { instance = new DecoderFactory(); }
    return instance;
  }

  private static final NativeDecoder     nativeDecoder     = new NativeDecoder();
  private static final NumberDecoder     numberDecoder     = new NumberDecoder();
  private static final DateDecoder       dateDecoder       = new DateDecoder();
  private static final ArrayDecoder      arrayDecoder      = new ArrayDecoder();
  private static final MapDecoder        mapDecoder        = new MapDecoder();
  private static final CollectionDecoder collectionDecoder = new CollectionDecoder();
  private static final JavaBeanDecoder   javaBeanDecoder   = new JavaBeanDecoder();

  public ActionScriptDecoder getDecoder(Object encodedObject, Class desiredClass)
  {
    boolean isNativeObject = ( EncoderFactory.isActionScriptNative( encodedObject ) ||
                             ( encodedObject instanceof ASObject && ASObject.class.isAssignableFrom(desiredClass) ) );
    boolean isNumber       = ( encodedObject instanceof Number );
    boolean isDate         = ( encodedObject instanceof Date );
    boolean isArray        = ( encodedObject instanceof ArrayList && desiredClass.isArray() );
    boolean isCollection   = ( encodedObject instanceof ArrayList && Collection.class.isAssignableFrom(desiredClass) );
    boolean isMap          = ( encodedObject instanceof Map       && Map.class.isAssignableFrom(desiredClass) &&
                             ! ASObject.class.isAssignableFrom(desiredClass) );
    boolean isJavaBean     = ( encodedObject instanceof ASObject  && ! Map.class.isAssignableFrom(desiredClass) );

    ActionScriptDecoder decoder = null;

    if      ( isNativeObject ) decoder = new CachingDecoder(nativeDecoder);
    else if ( isNumber       ) decoder = new CachingDecoder(numberDecoder);
    else if ( isDate         ) decoder = new CachingDecoder(dateDecoder);
    else if ( isArray        ) decoder = new CachingDecoder(arrayDecoder);
    else if ( isCollection   ) decoder = new CachingDecoder(collectionDecoder);
    else if ( isMap          ) decoder = new CachingDecoder(mapDecoder);
    else if ( isJavaBean     ) decoder = new CachingDecoder(javaBeanDecoder);
    else
    {
      log.warn("Cannot determine decoder.  Using NativeDecoder: " + encodedObject.getClass().getName() + ", " +
               desiredClass.getName());
      decoder = new CachingDecoder(nativeDecoder);
    }

    return decoder;
  }

  public static Class decideClassToTranslateInto( Object aso ) throws ASTranslationException
  {
    Class asoClass = null;

    if (aso instanceof ASObject)
    {
      String classOfActionScriptObject = ( (ASObject) aso).getType();

      if (classOfActionScriptObject == null) return Map.class;

      try
      {
        asoClass = Thread.currentThread().getContextClassLoader().loadClass(classOfActionScriptObject);
      }
      catch ( ClassNotFoundException cnfe )
      {
        throw new ASTranslationException( "Unable to find Server-Side Class to match type indicated by ActionScript Object: " + classOfActionScriptObject, cnfe );
      }
    }
    else
    {
      asoClass = aso.getClass();
    }

    return asoClass;
  }
}
package com.carbonfive.flash.decoder;

import java.util.*;
import flashgateway.io.ASObject;
import com.carbonfive.flash.encoder.*;

public class DecoderFactory
{
  private static DecoderFactory instance;

  private DecoderFactory() { }

  public static synchronized DecoderFactory getInstance()
  {
    if (instance == null) { instance = new DecoderFactory(); }
    return instance;
  }

  private static final NativeDecoder     nativeDecoder     = new NativeDecoder();
  private static final NumberDecoder     numberDecoder     = new NumberDecoder();
  private static final ArrayDecoder      arrayDecoder      = new ArrayDecoder();
  private static final MapDecoder        mapDecoder        = new MapDecoder();
  private static final CollectionDecoder collectionDecoder = new CollectionDecoder();
  private static final JavaBeanDecoder   javaBeanDecoder   = new JavaBeanDecoder();

  public ActionScriptDecoder getDecoder(Object encodedObject, Class desiredClass)
  {
    boolean isNativeObject = ( EncoderFactory.isActionScriptNative( encodedObject ) );
    boolean isNumberObject = ( encodedObject instanceof Number );
    boolean isArray        = ( encodedObject instanceof ArrayList && desiredClass.isArray() );
    boolean isCollection   = ( encodedObject instanceof ArrayList && Collection.class.isAssignableFrom(desiredClass) );
    boolean isMap          = ( encodedObject instanceof Map       && ! (encodedObject instanceof ASObject) ) ||
                             ( encodedObject instanceof ASObject  && ((ASObject) encodedObject).getType() == null );
    boolean isJavaBean     = ( encodedObject instanceof ASObject  && ((ASObject) encodedObject).getType() != null );

    ActionScriptDecoder decoder = null;

    if      ( isNativeObject ) decoder = new CachingDecoder(nativeDecoder);
    else if ( isNumberObject ) decoder = new CachingDecoder(numberDecoder);
    else if ( isArray )        decoder = new CachingDecoder(arrayDecoder);
    else if ( isCollection )   decoder = new CachingDecoder(collectionDecoder);
    else if ( isMap )          decoder = new CachingDecoder(mapDecoder);
    else if ( isJavaBean )     decoder = new CachingDecoder(javaBeanDecoder);
    else                       decoder = new CachingDecoder(nativeDecoder);

    return decoder;
  }
}
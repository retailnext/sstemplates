package com.carbonfive.flash.encoder;

import java.util.*;
import java.io.*;
import com.carbonfive.flash.encoder.*;

public class EncoderFactory
{
  private static EncoderFactory instance;

  private EncoderFactory() { }

  public static synchronized EncoderFactory getInstance()
  {
    if (instance == null) { instance = new EncoderFactory(); }
    return instance;
  }

  private static final NativeEncoder     nativeEncoder     = new NativeEncoder();
  private static final NumberEncoder     numberEncoder     = new NumberEncoder();
  private static final ArrayEncoder      arrayEncoder      = new ArrayEncoder();
  private static final MapEncoder        mapEncoder        = new MapEncoder();
  private static final CollectionEncoder collectionEncoder = new CollectionEncoder();
  private static final JavaBeanEncoder   javaBeanEncoder   = new JavaBeanEncoder();

  public ActionScriptEncoder getEncoder(Object decodedObject)
  {
    if (decodedObject == null) return new NativeEncoder();

    Class clazz = decodedObject.getClass();

    boolean isNativeObject = isActionScriptNative( decodedObject );
    boolean isNumberObject = Number.class.isAssignableFrom( clazz );
    boolean isArray        = clazz.isArray();
    boolean isCollection   = Collection.class.isAssignableFrom(clazz);
    boolean isMap          = Map.class.isAssignableFrom(clazz);
    boolean isJavaBean     = Serializable.class.isAssignableFrom(clazz);

    ActionScriptEncoder encoder = null;

    if      ( isNativeObject ) encoder = new CachingEncoder(nativeEncoder);
    else if ( isNumberObject ) encoder = new CachingEncoder(numberEncoder);
    else if ( isArray )        encoder = new CachingEncoder(arrayEncoder);
    else if ( isMap )          encoder = new CachingEncoder(mapEncoder);
    else if ( isCollection )   encoder = new CachingEncoder(collectionEncoder);
    else if ( isJavaBean )     encoder = new CachingEncoder(javaBeanEncoder);
    else                       encoder = new CachingEncoder(nativeEncoder);

    return encoder;
  }

  public static boolean isActionScriptNative(Object obj)
  {
    if (obj == null)                         return false;
    if (obj instanceof Boolean)              return true;
    if (obj instanceof Date)                 return true;
    if (obj instanceof String)               return true;
    if (obj instanceof org.w3c.dom.Document) return true;
    if (obj instanceof java.sql.ResultSet)   return true;
    return false;
  }
}
package com.carbonfive.flash.encoder;

import java.util.*;
import java.io.*;
import com.carbonfive.flash.*;
import org.apache.commons.logging.*;

public class EncoderFactory
{
  private static final Log log = LogFactory.getLog(EncoderFactory.class);

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
  private static final NullEncoder       nullEncoder       = new NullEncoder();

  public ActionScriptEncoder getEncoder(Context ctx, Object decodedObject)
  {
    if (decodedObject == null) return nullEncoder;

    Class clazz = decodedObject.getClass();

    LoopFinder loopFinder = ctx.getLoopFinder();
    loopFinder.add(clazz);
    if (loopFinder.isLoop())
    {
      throw new InfiniteLoopException(loopFinder);
    }

    if (ctx.getFilter().doIgnoreClass(clazz)) return nullEncoder;

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

  /**
   * Determines whether a Java object is handles natively by the Flash
   * Remoting gateway.  This information is gathered from the Macromedia's
   * <a href="http://livedocs.macromedia.com/frdocs/Using_Flash_Remoting_MX/UseASData4.jsp">
   * Data Conversion Chart</a>.
   */
  public static boolean isActionScriptNative(Object obj)
  {
    if (obj == null)                                                      return false;
    if (obj instanceof Boolean)                                           return true;
    if (obj instanceof Date)                                              return true;
    if (obj instanceof String)                                            return true;
    if (obj instanceof Character)                                         return true;
    if (obj instanceof java.sql.ResultSet)                                return true;
    if (instanceOf(obj.getClass(), "org.w3c.dom.Document"))               return true;
    if (instanceOf(obj.getClass(), "flashgateway.io.ASXMLString"))        return true;
    if (instanceOf(obj.getClass(), "flashgateway.sql.PageableResultSet")) return true;

    return false;
  }

  static boolean instanceOf(Class clazz, String className)
  {
    if (clazz == null) return false;
    if (className.equals(clazz.getName())) return true;
    if (clazz.getSuperclass() == null) return false;
    return instanceOf(clazz.getSuperclass(), className);
  }
}
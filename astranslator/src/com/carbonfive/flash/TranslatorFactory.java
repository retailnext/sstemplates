package com.carbonfive.flash;

import java.util.*;
import java.io.Serializable;
import org.apache.log4j.*;
import flashgateway.io.ASObject;

public class TranslatorFactory
{
  private static TranslatorFactory instance = null;

//------------------------------------------------------------------------------

  private TranslatorFactory()
  {
  }

//------------------------------------------------------------------------------

  public synchronized static TranslatorFactory getInstance()
  {
    if ( instance == null )
    {
      instance = new TranslatorFactory();
    }

    return instance;
  }

//------------------------------------------------------------------------------

  public Translator getTranslator( ASTranslator astranslator, Object serverObject )
  {

    Translator translator = null;
    Class clazz = serverObject.getClass();

    boolean isPrimitiveActionScriptObject = isActionScriptPrimitive( clazz );
    boolean isNumericClass                = Number.class.isAssignableFrom( clazz );
    boolean isArray                       = clazz.isArray();
    boolean isCollection                  = Collection.class.isAssignableFrom(clazz);
    boolean isMap                         = Map.class.isAssignableFrom(clazz);
    boolean isJavaBean                    = Serializable.class.isAssignableFrom(clazz);

    if ( isPrimitiveActionScriptObject )    // pass through these classes
    {
      translator = new NativeTranslator(astranslator);
    }
    
    else if ( isNumericClass )              // all numbers become doubles
    {
      translator = new DoubleTranslator(astranslator);
    }

    else if ( isArray )                     // create ArrayList
    {
      translator = new ArrayTranslator(astranslator);
    }

    else if ( isMap )                       // create ASObject
    {
      translator = new MapTranslator(astranslator);
    }

    else if ( isCollection )
    {
      translator = new CollectionTranslator(astranslator);
    }

    else if ( isJavaBean )
    {
      translator = new JavaBeanTranslator(astranslator);
    }

    return translator;
  }

//------------------------------------------------------------------------------

  public Translator getClientTranslator( ASTranslator astranslator, Object clientObject )
  {

    Translator translator = null;
    Class clazz = clientObject.getClass();

    boolean isNativeObject = ( clientObject instanceof String ) ||
                             ( clientObject instanceof Date )   ||
                             ( clientObject instanceof Boolean );
    boolean isNumberObject = ( clientObject instanceof Double );
    boolean isArrayList    = ( clientObject instanceof ArrayList && clazz.isArray() );
    boolean isCollection   = ( clientObject instanceof ArrayList && !clazz.isArray() );
    boolean isMap          = ( clientObject instanceof Map       && !(clientObject instanceof ASObject) ) ||
                             ( clientObject instanceof ASObject  && ((ASObject) clientObject).getType() == null );
    boolean isJavaBean     = ( clientObject instanceof ASObject );

    if ( isNativeObject )
    {
      translator = new NativeTranslator(astranslator);
    }
    else if ( isNumberObject )
    {
      translator = new DoubleTranslator(astranslator);
    }
    else if ( isArrayList )
    {
      translator = new ArrayTranslator(astranslator);
    }
    else if ( isCollection )
    {
      translator = new CollectionTranslator(astranslator);
    }
    else if ( isMap )
    {
      translator = new MapTranslator(astranslator);
    }
    else if ( isJavaBean )
    {
      translator = new JavaBeanTranslator(astranslator);
    }

    return translator;
  }


//------------------------------------------------------------------------------

  private static HashSet actionScriptPrimitives = new HashSet();
  static
  {
    actionScriptPrimitives.add(Boolean.class.getName());
    actionScriptPrimitives.add(Boolean.TYPE.getName());
    actionScriptPrimitives.add(String.class.getName());
    actionScriptPrimitives.add(Date.class.getName());
  }

//------------------------------------------------------------------------------

  private static boolean isActionScriptPrimitive(Class clazz)
  {
    if (clazz == null) return false;
    return actionScriptPrimitives.contains( clazz.getName() );
  }

//------------------------------------------------------------------------------

}
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

  synchronized static TranslatorFactory getInstance()
  {
    if ( instance == null )
    {
      instance = new TranslatorFactory();
    }

    return instance;
  }

//------------------------------------------------------------------------------

  Translator getTranslatorToActionScript( ASTranslator astranslator, Object serverObject )
  {

    Translator translator = null;
    Class clazz = serverObject.getClass();

    boolean isNativeObject                = isActionScriptNative( serverObject );
    boolean isNumberObject                = Number.class.isAssignableFrom( clazz );
    boolean isArray                       = clazz.isArray();
    boolean isCollection                  = Collection.class.isAssignableFrom(clazz);
    boolean isMap                         = Map.class.isAssignableFrom(clazz);
    boolean isJavaBean                    = Serializable.class.isAssignableFrom(clazz);

    if ( isNativeObject )                   // pass through these classes
    {
      translator = new NativeTranslator(astranslator);
    }
    
    else if ( isNumberObject )              // all numbers become doubles
    {
      translator = new NumberTranslator(astranslator);
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

  Translator getTranslatorFromActionScript( ASTranslator astranslator, Object clientObject )
  {

    Translator translator = null;

    boolean isNativeObject = ( isActionScriptNative( clientObject ) );
    boolean isNumberObject = ( clientObject instanceof Number );
    boolean isArrayList    = ( clientObject instanceof ArrayList && clientObject.getClass().isArray() );
    boolean isCollection   = ( clientObject instanceof ArrayList && !clientObject.getClass().isArray() );
    boolean isMap          = ( clientObject instanceof Map       && !(clientObject instanceof ASObject) ) ||
                             ( clientObject instanceof ASObject  && ((ASObject) clientObject).getType() == null );
    boolean isJavaBean     = ( clientObject instanceof ASObject );

    if ( isNativeObject )
    {
      translator = new NativeTranslator(astranslator);
    }
    else if ( isNumberObject )
    {
      translator = new NumberTranslator(astranslator);
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

  private static boolean isActionScriptNative(Object obj)
  {
    if (obj == null)                         return false;
    if (obj instanceof Boolean)              return true;
    if (obj instanceof Date)                 return true;
    if (obj instanceof String)               return true;
    if (obj instanceof org.w3c.dom.Document) return true;
    if (obj instanceof java.sql.ResultSet)   return true;
    return false;
  }

//------------------------------------------------------------------------------

}
package com.carbonfive.flash;

import flashgateway.io.ASObject;
import com.carbonfive.flash.decoder.*;
import com.carbonfive.flash.encoder.*;
import org.apache.commons.logging.*;


 /**
  * ASTranslator provides the ability to translate between ASObjects used by
  * Macromedia Flash Remoting and Java objects in your application. 
  * <a href="package-summary.html#documentation">See the project documentation</a> for details.
  * <p>
  * $Id$
  */
public class ASTranslator
{
   private static final Log log = LogFactory.getLog(ASTranslator.class);

  /**
   * Given an Object, toActionScript creates a corresponding ASObject
   * or Collection of ASObjects that maps the source object's JavaBean
   * properties to ASObject fields, Collections and Sets to
   * ArrayLists, and all Numbers to Doubles while maintaining object
   * references (including circular references).
   * <p> 
   * These mappings are consistent with Flash Remoting's rules for
   * converting Objects to ASObjects. They just add the behavior of
   * using JavaBean-style introspection to determine property
   * names. Additionally, toActionScript sets the "type" field of all
   * ASObjects created to be the class name of the source
   * JavaBean. This enables two-way translation between ASObjects and
   * JavaBeans.
   *
   * @param serverObject  an Object to translate to ASObjects or 
   *                      corresponding primitive or Collection classes
   * @return              an Object that may be an ASObject or nested 
   *                      Collections of ASObjects or null if translation fails
   */
  public Object toActionScript( Object serverObject )
  {
    if (serverObject == null) return null;

    CachingManager.getEncoderCache(); // create the cache here

    ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder( serverObject );
    Object              result  = encoder.encodeObject(encoder.encodeShell(serverObject), serverObject);

    CachingManager.removeEncoderCache(); // remove it here

    return result;
  }


  /**
   * Given an Object that is either an ASObject or Collection of
   * ASObjects, fromActionScript creates a corresponding JavaBean or
   * Collection of JavaBeans.  
   * <p> 
   * The "type" field of an ASObject identifies the class name of
   * the JavaBean to create. If the type field is null, an ASObject
   * becomes a HashMap. The interface of the JavaBean is more specific
   * that the relatively loosely-typed ASObject and therefore drives
   * the translation of ASObject fields.

   *
   * @param asObject an Object that is usually an ASObject but may also be
   *                           a Collection or primitive
   * @return    an Object value that is either a JavaBean or Collection
   *            of JavaBeans or null if translation fails
   */
  public Object fromActionScript( Object asObject )
  {
    try
    {
      Class desiredBeanClass = decideClassToTranslateInto( asObject );
      return fromActionScript( asObject, desiredBeanClass );
    }
    catch( ASTranslationException aste )
    {
      return null;
    }
  }

////////////////////////////////////////////////////////////////////////////////
//
// p r o t e c t e d
//
////////////////////////////////////////////////////////////////////////////////

  /**
   * Translate an object to another object of type clazz
   * obj types should be ASObject, Boolean, String, Double, Date, ArrayList
   */
  protected Object fromActionScript( Object actionScriptObject, Class desiredBeanClass )
  {
    if (actionScriptObject == null) return null;

    CachingManager.getDecoderCache();

    ActionScriptDecoder decoder = DecoderFactory.getInstance().getDecoder( actionScriptObject, desiredBeanClass );
    Object              result  = decoder.decodeObject( decoder.decodeShell(actionScriptObject, desiredBeanClass),
                                                        actionScriptObject, desiredBeanClass );

    CachingManager.removeEncoderCache();

    return result;
  }

////////////////////////////////////////////////////////////////////////////////
//
// p r i v a t e
//
////////////////////////////////////////////////////////////////////////////////

  private Class decideClassToTranslateInto( Object aso ) throws ASTranslationException
  {
    Class asoClass = null;

    if (aso instanceof ASObject)
    {
      String classOfActionScriptObject = ( (ASObject) aso).getType();
      try
      {
        asoClass = Class.forName( classOfActionScriptObject );
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
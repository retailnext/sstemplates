package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;

/**
 * TranslationFilter holds classes and properties that should be ignored during a translation to Action Script.
 * Encoders ask TranslationFilter whether they should ignore these classes and properties as they encode Java
 * objects.
 * <p>
 * The TranslationFilter understands class hierarchy, so if <code>ignoreClass("java.lang.Number")</code> is called,
 * a subsequent call to <code>doIgnoreClass(Double.class)</code> will return true.  Similarly with
 * <code>doIgnoreProperty()</code>.
 */
public class TranslationFilter
  implements Serializable
{
  private static final Log log = LogFactory.getLog(TranslationFilter.class);
  
  private Set ignoreClasses    = new HashSet();
  private Map ignoreProperties = new HashMap();

  /**
   * Generated a base TranslationFilter object.  This TranslationFilter includes the following dangerous properties:
   * <ul>
   * <li>File.parentFile</li>
   * <li>File.canonicalFile</li>
   * <li>File.absoluteFile</li>
   * </ul>
   * These properties will result in an infinite loop during translation.
   * @return the base TranslationFilter
   */
  public static TranslationFilter getBaseFilter()
  {
    TranslationFilter filter = new TranslationFilter();
    filter.ignoreProperty(File.class, "parentFile");
    filter.ignoreProperty(File.class, "canonicalFile");
    filter.ignoreProperty(File.class, "absoluteFile");

    return filter;
  }

  private TranslationFilter()
  {
  }

  /**
   * Ignore a specific class, and all subclasses.
   * @param klass the class to ignore
   */
  public void ignoreClass(Class klass)
  {
    ignoreClasses.add(klass);
  }

  /**
   * Ignore a specific properties, including in all subclasses.
   * <p>
   * The property is described by its name, as defined by the Jakarata Commons BeanUtils project.<br/>
   * See <a target="_top" href="http://jakarta.apache.org/commons/beanutils.html">Jakarata Commons BeanUtils</a>
   * @param klass the class with the property to ignore
   * @param property the name of the property to ignore
   */
  public void ignoreProperty(Class klass, String property)
  {
    Set properties = (Set) ignoreProperties.get(klass);
    if (properties == null) properties = new HashSet();
    properties.add(property);
    ignoreProperties.put(klass, properties);
  }

  /**
   * Whether or not to ignore the specified class.
   * @param klass the class in question
   * @return true or false
   */
  public boolean doIgnoreClass(Class klass)
  {
    if (klass == null) return false;

    boolean doIgnore = ignoreClasses.contains(klass);

    if (Object.class.equals(klass)) return doIgnore;

    if (doIgnore) return true;
    return doIgnoreClass(klass.getSuperclass());
  }

  /**
   * Whether or not to ignore the specified property.
   * @param klass the class of the property in question
   * @param property the property in question
   * @return true or false
   */
  public boolean doIgnoreProperty(Class klass, String property)
  {
    if (klass == null) return false;

    boolean doIgnore = false;
    if (ignoreProperties.containsKey(klass)) doIgnore = ((Set) ignoreProperties.get(klass)).contains(property);

    if (Object.class.equals(klass)) return doIgnore;

    if (doIgnore) return true;
    return doIgnoreProperty(klass.getSuperclass(), property);
  }
}

package com.carbonfive.flash;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;

public class TranslationFilter
  implements Serializable
{
  private static final Log log = LogFactory.getLog(TranslationFilter.class);
  
  private Set ignoreClasses    = new HashSet();
  private Map ignoreProperties = new HashMap();

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

  public void ignoreClass(Class klass)
  {
    ignoreClasses.add(klass);
  }

  public void ignoreProperty(Class klass, String property)
  {
    Set properties = (Set) ignoreProperties.get(klass);
    if (properties == null) properties = new HashSet();
    properties.add(property);
    ignoreProperties.put(klass, properties);
  }

  public boolean doIgnoreClass(Class klass)
  {
    return ignoreClasses.contains(klass);
  }

  public boolean doIgnoreProperty(Class klass, String property)
  {
    if (! ignoreProperties.containsKey(klass)) return false;
    return ((Set) ignoreProperties.get(klass)).contains(property);
  }
}

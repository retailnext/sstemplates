package com.carbonfive.flash;

import java.io.*;
import org.apache.commons.logging.*;

public class ASTranslatorFactory
  implements Serializable
{
  private static final Log log = LogFactory.getLog(ASTranslatorFactory.class);

  public static final String FACTORY_PROPERTY = ASTranslatorFactory.class.getName();

  public static ASTranslatorFactory getInstance() throws ASTranslationException
  {
    String factoryClassName = System.getProperty(FACTORY_PROPERTY);
    if (factoryClassName == null || "".equals(factoryClassName)) return new ASTranslatorFactory();

    try
    {
      Class factoryClass = Thread.currentThread().getContextClassLoader().loadClass(factoryClassName);

      if (! ASTranslatorFactory.class.isAssignableFrom(factoryClass))
        throw new ASTranslationException("Custom ASTranslatorFactory must extend ASTranslatorFactory class: " +
                                         factoryClass.getName());

      return (ASTranslatorFactory) factoryClass.newInstance();
    }
    catch (RuntimeException re)
    {
      throw re;
    }
    catch (Exception e)
    {
      throw new ASTranslationException("Error creating custom ASTranslatorFactory", e);
    }
  }

  protected ASTranslatorFactory()
  {

  }

  public ASTranslator getASTranslator()
  {
    return new ASTranslator();
  }
}

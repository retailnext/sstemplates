package com.carbonfive.flash;

import java.io.*;
import org.apache.commons.logging.*;

public class ASTranslatorFactory
  implements Serializable
{
  private static final Log log = LogFactory.getLog(ASTranslatorFactory.class);

  public static final String FACTORY_PROPERTY = ASTranslatorFactory.class.getName();

  public static ASTranslatorFactory getInstance() throws Exception
  {
    String factoryClassName = System.getProperty(FACTORY_PROPERTY);
    if (factoryClassName == null || "".equals(factoryClassName)) return new ASTranslatorFactory();

    Class factoryClass = Thread.currentThread().getContextClassLoader().loadClass(factoryClassName);

    if (! ASTranslatorFactory.class.isAssignableFrom(factoryClass))
      throw new ASTranslationException("Custom ASTranslatorFactory must extend ASTranslatorFactory class: " +
                                       factoryClass.getName());

    return (ASTranslatorFactory) factoryClass.newInstance();
  }

  protected ASTranslatorFactory()
  {

  }

  public ASTranslator getASTranslator()
  {
    return new ASTranslator();
  }
}

package com.carbonfive.sstemplates;

import org.apache.commons.logging.*;

public class SsTemplateException
  extends Exception
{
  private static final Log log = LogFactory.getLog(SsTemplateException.class);

  public SsTemplateException()
  {
    super();
  }

  public SsTemplateException(String message)
  {
    super(message);
  }

  public SsTemplateException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public SsTemplateException(Throwable cause)
  {
    super(cause);
  }
}

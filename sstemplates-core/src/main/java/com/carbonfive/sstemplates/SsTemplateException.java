package com.carbonfive.sstemplates;

public class SsTemplateException
  extends Exception
{
  private static final long serialVersionUID = 1L;

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

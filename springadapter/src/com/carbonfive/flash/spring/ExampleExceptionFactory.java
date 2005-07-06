package com.carbonfive.flash.spring;

import flashgateway.*;

/**
 * An example implementation of RemotingExceptionFactory
 *
 */
public class ExampleExceptionFactory
    implements java.io.Serializable, RemotingExceptionFactory
{
  public static String CUSTOM_CODE = "Server.Custom";

  public void throwException(Throwable t) throws Exception
  {
    if (t instanceof SecurityException)
    {
      GatewayException e = new GatewayException(t.getMessage());
      e.setRootCause(t);
      e.setCode(GatewayException.SERVER_AUTHORIZATION);
      throw e;
    }
    else if (t instanceof IndexOutOfBoundsException)
    {
      GatewayException e = new GatewayException(t.getMessage());
      e.setRootCause(t);
      e.setCode(CUSTOM_CODE);
      throw e;
    }

    if (t instanceof Exception) throw (Exception) t;
    else if (t instanceof Error) throw (Error) t;
  }
}

package com.carbonfive.flash.spring;

/**
 * Interface for optional exception handling service to SpringBeanAdapter.
 * SpringBeanAdapter will look up an implementation of this interface under
 * the bean name "remotingExceptionFactory" and use it to create exceptions
 * if it exists. This is most useful for creating GatewayException instances
 * with custom message and code.
 */
public interface RemotingExceptionFactory
{
  public static final String BEAN_NAME = "remotingExceptionFactory";

  public void throwException(Throwable t) throws Exception;
}

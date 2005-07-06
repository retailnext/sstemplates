package com.carbonfive.flash.spring;

import java.util.*;
import java.lang.reflect.*;
import org.springframework.context.*;
import org.springframework.web.context.support.*;
import flashgateway.adapter.java.*;
import flashgateway.action.*;
import flashgateway.*;

/**
 * An adapter for locating Flash Remoting services in a Spring
 * application context. This adapter only works with versions of
 * Flash Remoting that allow registering custom adapters, such as
 * the version provided with Macromedia Flex.
 *
 * 
 */
public class SpringBeanAdapter
    extends JavaAdapter
{
  private static final String PREFIX = "spring://";

  private RemotingExceptionFactory exceptionFactory;

  public boolean supportsService(ActionContext context, String serviceName,
                                 String functionName, List parameters,
                                 String type)
      throws Exception
  {
    return serviceName.startsWith(PREFIX);
  }

  public Object invokeFunction(ActionContext context, String serviceName,
                               String functionName, List parameters)
      throws Exception
  {
    ApplicationContext application = getApplicationContext(context);

    if (exceptionFactory == null &&
        application.containsBean(RemotingExceptionFactory.BEAN_NAME))
    {
      exceptionFactory = (RemotingExceptionFactory)
          application.getBean(RemotingExceptionFactory.BEAN_NAME);
    }

    String beanName = serviceName.substring(PREFIX.length());

    if (!application.containsBean(beanName))
      throw new GatewayException("Unable to locate service named '" + beanName +
                                 "' " + "in Spring application context.");

    Object service = application.getBean(beanName);
    Method method = getMethod(parameters, serviceName,
                              functionName, service.getClass());
    try
    {
      return method.invoke(service, parameters.toArray());
    }
    catch (Throwable t)
    {
      if(t instanceof InvocationTargetException)
          t = ((InvocationTargetException) t).getTargetException();
      if(t instanceof ExceptionInInitializerError)
        t = ((ExceptionInInitializerError) t).getException();

      if (exceptionFactory == null)
      {
        if (t instanceof Exception) throw (Exception) t;
        else if (t instanceof Error) throw (Error) t;
      }
      exceptionFactory.throwException(t);
    }
    return null;
  }

  private ApplicationContext getApplicationContext(ActionContext context)
  {
    return WebApplicationContextUtils
        .getWebApplicationContext(context.getServlet().getServletContext());
  }

}

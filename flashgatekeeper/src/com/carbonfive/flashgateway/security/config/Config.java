package com.carbonfive.flashgateway.security.config;

import java.util.*;

/**
 * Root configuration class.
 */
public class Config
{
  private Map services = new HashMap();

  public Map getServices()
  {
    return services;
  }

  public void addService(Service service)
  {
    getServices().put(service.getName(), service);
  }

  public Service getService(String serviceName)
  {
    if (services.containsKey(serviceName)) return (Service) services.get(serviceName);

    String name = null;
    for (Iterator i = services.keySet().iterator(); i.hasNext(); )
    {
      name = (String) i.next();
      if (serviceName.startsWith(name)) return (Service) services.get(name);
    }
    return null;
  }

  public String toString()
  {
    return "Config[services:\n" + getServices() + "]";
  }
}

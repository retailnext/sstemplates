package com.carbonfive.flashgateway.security.config;

import java.util.*;

public class Config
{
  private List services = new ArrayList();

  public List getServices()
  {
    return services;
  }

  public ServiceConfig getService(String serviceName)
  {
    ServiceConfig service = null;
    for (Iterator i = this.getServices().iterator(); i.hasNext(); )
    {
      service = (ServiceConfig) i.next();
      if (serviceName.startsWith(service.getName())) return service;
    }
    return null;
  }
}

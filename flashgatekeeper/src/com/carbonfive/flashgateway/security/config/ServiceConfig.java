package com.carbonfive.flashgateway.security.config;

import java.util.*;

public class ServiceConfig
{
  private String                 name;
  private List                   methods = new ArrayList();
  private AccessConstraintConfig constraint;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List getMethods()
  {
    return methods;
  }

  public MethodConfig getMethod(String methodName)
  {
    MethodConfig method = null;
    for (Iterator i = this.getMethods().iterator(); i.hasNext(); )
    {
      method = (MethodConfig) i.next();
      if (methodName.equals(method.getName())) return method;
    }
    return null;
  }
}

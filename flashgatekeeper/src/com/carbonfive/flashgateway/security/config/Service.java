package com.carbonfive.flashgateway.security.config;

import java.util.*;

/**
 * Service configuration class.
 */
public class Service
{
  private String                 name;
  private Map                    methods = new HashMap();
  private AccessConstraint constraint;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Map getMethods()
  {
    return methods;
  }

  public void addMethod(Method method)
  {
    getMethods().put(method.getName(), method);
  }

  public Method getMethod(String methodName)
  {
    if (methods.containsKey(methodName)) return (Method) methods.get(methodName);

    if (methods.containsKey("*")) return (Method) methods.get("*");

    return null;
  }

  public String toString()
  {
    return "\n  Service[name:    " + getName()
         + "\n          methods: " + getMethods()
         + "\n  ]\n";
  }
}

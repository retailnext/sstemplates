package com.carbonfive.flashgateway.security;

import java.util.*;
import javax.servlet.http.*;
import com.carbonfive.flashgateway.security.config.*;

public class Gatekeeper
{
  private Config config;

  public Config getConfig()
  {
    return config;
  }

  public void setConfig(Config config)
  {
    this.config = config;
  }

  public boolean canInvoke(HttpServletRequest request, String serviceName, String methodName)
  {
    ServiceConfig service = config.getService(serviceName);
    if (service == null) return false;

    if (service.getMethods().isEmpty()) return true;

    MethodConfig method = service.getMethod(methodName);
    if (method == null) return false;

    if (method.getConstraint() == null) return true;

    return userIsInRole(request, method.getConstraint());
  }

  private boolean userIsInRole(HttpServletRequest request, AccessConstraintConfig constraint)
  {
    for (Iterator i = constraint.getRoleNames().iterator(); i.hasNext(); )
    {
      if (request.isUserInRole((String) i.next())) return true;
    }

    return false;
  }
}

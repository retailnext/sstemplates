package com.carbonfive.flashgateway.security.config;

import java.util.*;

public class MethodConfig
{
  private String                 name;
  private AccessConstraintConfig constraint;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public AccessConstraintConfig getConstraint()
  {
    return constraint;
  }

  public void setConstraint(AccessConstraintConfig constraint)
  {
    this.constraint = constraint;
  }
}

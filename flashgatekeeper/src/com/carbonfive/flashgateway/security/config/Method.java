package com.carbonfive.flashgateway.security.config;

import java.util.*;

/**
 * Method configuration class.
 */
public class Method
{
  private String                 name;
  private AccessConstraint constraint;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public AccessConstraint getConstraint()
  {
    return constraint;
  }

  public void setConstraint(AccessConstraint constraint)
  {
    this.constraint = constraint;
  }

  public String toString()
  {
    return "Method[name: " + getName() + ", constraint: " + getConstraint() + "]\n";
  }
}

package com.carbonfive.flashgateway.security.config;

import java.util.*;

/**
 * Access constraint configuration class.
 */
public class AccessConstraint
{
  private List roleNames = new ArrayList();

  public List getRoleNames() { return roleNames; }

  public void addRoleName(String name)
  {
    getRoleNames().add(name);
  }

  public String toString()
  {
    return "Constraint[roles: " + getRoleNames() + "]";
  }
}

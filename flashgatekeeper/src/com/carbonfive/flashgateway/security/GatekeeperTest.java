package com.carbonfive.flashgateway.security;

import java.io.*;
import javax.servlet.http.*;
import junit.framework.*;
import org.apache.cactus.*;
import org.apache.commons.logging.*;
import com.carbonfive.flashgateway.security.*;
import com.carbonfive.flashgateway.security.config.*;

public class GatekeeperTest
  extends TestCase
{
  private static final Log log = LogFactory.getLog(GatekeeperTest.class);

  private SettableRoleRequest request;

  public GatekeeperTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(GatekeeperTest.class);
    return suite;
  }

  protected void setUp() throws Exception
  {
    request = new SettableRoleRequest();
  }

  protected void tearDown() throws Exception{}

  public void testMethodAccess()
  {
    Gatekeeper gatekeeper = new Gatekeeper();
    Config config = new Config();
    gatekeeper.setConfig(config);

    ServiceConfig service = new ServiceConfig();
    service.setName("testService");
    config.getServices().add(service);

    assertTrue(gatekeeper.canInvoke(request, "testService", "anyMethod"));

    MethodConfig method = new MethodConfig();
    method.setName("testMethod");
    service.getMethods().add(method);

    assertTrue(! gatekeeper.canInvoke(request, "testService", "anyMethod"));
    assertTrue(gatekeeper.canInvoke(request, "testService", "testMethod"));

    method.setConstraint(new AccessConstraintConfig());
    method.getConstraint().getRoleNames().add("testRole");

    assertTrue(! gatekeeper.canInvoke(request, "testService", "testMethod"));

    request.setRoleName("testRole");

    assertTrue(gatekeeper.canInvoke(request, "testService", "testMethod"));
  }

  private class SettableRoleRequest
   extends HttpServletRequestWrapper
  {
    private String roleName;

    SettableRoleRequest()
    {
      this(null);
    }

    SettableRoleRequest(HttpServletRequest request)
    {
      super(request);
    }

    public boolean isUserInRole(String roleName)
    {
      return roleName.equals(getRoleName());
    }

    public String getRoleName()
    {
      return roleName;
    }

    public void setRoleName(String roleName)
    {
      this.roleName = roleName;
    }
  }

}
package com.carbonfive.flashgateway.security;

import java.security.*;
import javax.servlet.http.*;
import junit.framework.*;
import org.apache.commons.logging.*;
import com.carbonfive.flashgateway.security.config.*;

public class GatekeeperTest
  extends TestCase
{
  private static final Log log = LogFactory.getLog(GatekeeperTest.class);

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
  }

  protected void tearDown() throws Exception{}

  public void testMethodAccess()
  {
    SettableRoleRequest request = new SettableRoleRequest();

    Gatekeeper gatekeeper = new Gatekeeper();
    Config config = new Config();
    gatekeeper.setConfig(config);

    Service service = new Service();
    service.setName("com");
    config.addService(service);

    assertTrue(! gatekeeper.canInvoke(request, "com.testService", "anyMethod"));

    Method anyMethod = new Method();
    anyMethod.setName("*");
    service.addMethod(anyMethod);

    assertTrue(gatekeeper.canInvoke(request, "com.testService", "anyMethod"));

    service.setName("com.testService");

    assertTrue(gatekeeper.canInvoke(request, "com.testService", "anyMethod"));

    service.getMethods().clear();

    Method method = new Method();
    method.setName("testMethod");
    service.addMethod(method);

    assertTrue(! gatekeeper.canInvoke(request, "com.testService", "anyMethod"));
    assertTrue(gatekeeper.canInvoke(request, "com.testService", "testMethod"));

    method.setConstraint(new AccessConstraint());
    method.getConstraint().getRoleNames().add("oneRole");
    method.getConstraint().getRoleNames().add("twoRole");

    assertTrue(! gatekeeper.canInvoke(request, "com.testService", "testMethod"));

    request.setRoleName("oneRole");

    assertTrue(gatekeeper.canInvoke(request, "com.testService", "testMethod"));

    request.setRoleName("twoRole");

    assertTrue(gatekeeper.canInvoke(request, "com.testService", "testMethod"));
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

    public Principal getUserPrincipal()
    {
      return null;
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
package com.carbonfive.flashgateway.security;

import java.io.*;
import java.util.*;
import java.util.Date;
import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.commons.logging.*;
import com.carbonfive.flashgateway.security.*;
import flashgateway.action.message.*;
import flashgateway.*;
import flashgateway.io.*;

/**
 * GatekeeperFilter is a standard Servlet 2.3 Filter that is designed
 * to inspect the AMF messsage sent by a Flash MX client when trying
 * to invoke a service in the servlet container through Macromedia
 * Flash Remoting MX for J2EE. GatekeeperFilter uses classes in the Flash
 * Remoting distribution to parse AMF messages.
 * <p>
 * GatekeeperFilter only allows AMF messages that are trying to invoke
 * a configured list of services to get to the Flash Remoting gateway.
 * Map this filter in your web.xml to the same URL as the Flash Remoting
 * GatewayServlet.
 * <p>
 * If it encounters an AMF request that it not allowed, it logs a warning
 * with the full details of the service request and returns a <b>403 Forbidden</b>
 * status to the client.
 * <p>
 * Configure GatekeeperFilter with a properties file that lists the services
 * that is should allow through. It looks for the properties file in its
 * classpath.
 *
 * <pre class="code">
 * &lt;filter>
 *   &lt;filter-name>GatekeeperFilter&lt;/filter-name>
 *   &lt;filter-class>com.carbonfive.flashgateway.security.GatekeeperFilter&lt;/filter-class>
 *   &lt;init-param>
 *     &lt;param-name>properties-file&lt;/param-name>
 *     &lt;param-value>remotingservices.properties&lt;/param-value>
 *   &lt;/init-param>
 * &lt;/filter>
 *
 * &lt;filter-mapping>
 *   &lt;filter-name>GatekeeperFilter&lt;/filter-name>
 *     &lt;url-pattern>/gateway&lt;/url-pattern>
 * &lt;/filter-mapping>
 * </pre>
 *
 * Put each allowed service on a new line in the properties file.
 * You may list a package name instead of a class or a JNDI context
 * instead of an object in JNDI. GatekeeperFilter only allows services
 * to be invoked whose name starts with one of the values in the
 * properties file.
 * <p>
 * A sample <i>remotingservices.properties</i> properties file:
 * <pre class="code">
 * com.carbonfive.flashservices
 * com.carbonfive.util.SampleFlashService
 * java\:comp/env/ejb
 * </pre>
 * This properties file allows only services in or below the package
 * <tt>com.carbonfive.flashservices</tt>, the service implementation
 * <tt>com.carbonfive.util.SampleFlashService</tt> and any EJB
 * services in JNDI under <tt>java:comp/env/ejb</tt>.
 *
 * @web.filter name="GatekeeperFilter"
 * @web.filter-mapping url-pattern="/gateway"
 * @web.filter-init-param name="properties-file"
 *                        value="remotingservices.properties"
 */
public class GatekeeperFilter
  implements Filter
{
  private static Log log = LogFactory.getLog(GatekeeperFilter.class.getName());

  Collection serviceNames;

  public void init(FilterConfig config)
      throws ServletException
  {
    String file  = config.getInitParameter("properties-file");
    if (file == null) throw new ServletException("init-param \"properties-file\" is required");
    try
    {
      Properties services = new Properties();
      services.load(getClass().getClassLoader().getResource(file).openStream());

      serviceNames = getServiceNames(services);
      log.info("Loaded permitted services from " + file + ": " + serviceNames);
    }
    catch (Exception e)
    {
      log.error("error trying to load service names from " + file, e);
      throw new ServletException("error trying to load service names from " + file, e);
    }
  }

  private Collection getServiceNames(Properties properties)
  {
    HashSet names = new HashSet();
    for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); )
    {
      names.add(e.nextElement());
    }
    return names;
  }

  private boolean isNamedService(String name)
  {
    if (name == null) return false;

    for (Iterator i = serviceNames.iterator(); i.hasNext(); )
    {
      if (name.startsWith((String) i.next())) return true;
    }
    return false;
  }

  public void destroy() { }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {

    BufferedHttpRequestWrapper wrapped = new BufferedHttpRequestWrapper((HttpServletRequest) request);
    BufferedServletInputStream buffered = wrapped.getBufferedInputStream();
    buffered.mark(wrapped.getContentLength() + 1);

    try
    {
      MessageDeserializer des = new MessageDeserializer(GatewayConstants.SERVER_J2EE);
      des.setInputStream(buffered);
      ActionMessage requestMessage = des.readMessage();
      for (Iterator bodies = requestMessage.getBodies().iterator(); bodies.hasNext();)
      {
        MessageBody requestBody = (MessageBody) bodies.next();
        String serviceName = requestBody.getTargetURI();
        if (log.isDebugEnabled()) log.debug("Service invocation: " + serviceName);
        if (!isNamedService(serviceName))
        {
          String msg = serviceName + " is not a permitted service.\n"
                       + "Request Details: " + getRequestDetails(request);
          log.warn(msg);
          ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, msg);
          return;
        }
		  }
    }
    catch (IOException e)
    {
      log.error("Error inspecting AMF request", e);
    }

    // Reset and move on as if nothing happened
    buffered.reset();
    chain.doFilter(wrapped, response);
  }

  private String getRequestDetails(ServletRequest request)
  {
    StringBuffer msg = new StringBuffer();
    msg.append("\n=============================================");
    msg.append("\nRequest Received at " + new Date());
    msg.append("\n characterEncoding = " + request.getCharacterEncoding());
    msg.append("\n     contentLength = " + request.getContentLength());
    msg.append("\n       contentType = " + request.getContentType());
    msg.append("\n            locale = " + request.getLocale());
    msg.append("\n           locales = ");

    boolean first = true;
    for (Enumeration locales = request.getLocales(); locales.hasMoreElements(); )
    {
      Locale locale = (Locale) locales.nextElement();
      if (first) first = false;
      else       msg.append(", ");
      msg.append(locale.toString());
    }

    for (Enumeration names = request.getParameterNames(); names.hasMoreElements(); )
    {
      String name = (String) names.nextElement();
      msg.append("         parameter = " + name + " = ");
      String values[] = request.getParameterValues(name);
      for (int i = 0; i < values.length; i++)
      {
        if (i > 0) msg.append(", ");
        msg.append(values[i]);
      }
    }
    msg.append("\n          protocol = " + request.getProtocol());
    msg.append("\n        remoteAddr = " + request.getRemoteAddr());
    msg.append("\n        remoteHost = " + request.getRemoteHost());
    msg.append("\n            scheme = " + request.getScheme());
    msg.append("\n        serverName = " + request.getServerName());
    msg.append("\n        serverPort = " + request.getServerPort());
    msg.append("\n          isSecure = " + request.isSecure());

    // Render the HTTP servlet request properties
    if (request instanceof HttpServletRequest) {
      msg.append("\n---------------------------------------------");
      HttpServletRequest hrequest = (HttpServletRequest) request;
      msg.append("\n       contextPath = " + hrequest.getContextPath());
      Cookie cookies[] = hrequest.getCookies();
      if (cookies == null)
        cookies = new Cookie[0];
      for (int i = 0; i < cookies.length; i++) {
        msg.append("\n            cookie = " + cookies[i].getName() +
                   " = " + cookies[i].getValue());
      }
      for (Enumeration names = hrequest.getHeaderNames(); names.hasMoreElements(); )
      {
        String name = (String) names.nextElement();
        String value = hrequest.getHeader(name);
        msg.append("\n            header = " + name + " = " + value);
      }
      msg.append("\n            method = " + hrequest.getMethod());
      msg.append("\n          pathInfo = " + hrequest.getPathInfo());
      msg.append("\n       queryString = " + hrequest.getQueryString());
      msg.append("\n        remoteUser = " + hrequest.getRemoteUser());
      msg.append("\nrequestedSessionId = " + hrequest.getRequestedSessionId());
      msg.append("\n        requestURI = " + hrequest.getRequestURI());
      msg.append("\n       servletPath = " + hrequest.getServletPath());
    }
    msg.append("\n=============================================");

    return msg.toString();
  }

}

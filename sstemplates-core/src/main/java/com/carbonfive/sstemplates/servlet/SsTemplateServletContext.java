package com.carbonfive.sstemplates.servlet;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.carbonfive.sstemplates.*;

/**
 * This class acts as an EL VariableResolver, but does not support the pageContext implicit object.
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateServletContext
    extends SsTemplateContextImpl
{
  private static final Logger log = Logger.getLogger(SsTemplateServletContext.class.getName());

  private HttpServletRequest    request         = null;
  private ServletContext        servletContext  = null;
  private Map                   implicitObjects = new HashMap();

  public SsTemplateServletContext(HttpServletRequest request, ServletContext context,
                                  SsTemplateProcessor processor, File templateDir)
  {
    super(processor, templateDir);
    this.setRequest(request);
    this.setServletContext(context);
  }


  public File findFileInTemplateDirectory(String path)
  {
    String file = ( path == null ? "" : path );
    if ( !file.startsWith("/") ) return super.findFileInTemplateDirectory(path);

    return new File(servletContext.getRealPath(file));
  }

  public Object resolveVariable( String name )
  {
    if ( "requestScope".equals( name ) )
      return getRequestScope();

    if ( "sessionScope".equals( name ) )
      return getSessionScope();

    if ( "applicationScope".equals( name ) )
      return getApplicationScope();

    if ( "param".equals( name ) )
      return getParam();

    if ( "paramValues".equals( name ) )
      return getParamValues();

    if ( "header".equals( name ) )
      return getHeader();

    if ( "headerValues".equals( name ) )
      return getHeaderValues();

    if ( "cookie".equals( name ) )
      return getCookie();

    if ( "initParam".equals( name ) )
      return getInitParam();

    // otherwise, try to find the name in page, request, session, then application scope
    if ( getRequest().getAttribute(name) != null )
      return getRequest().getAttribute(name);

    if (( getRequest().getSession(false) != null ) && ( getRequest().getSession(false).getAttribute(name) != null ))
      return getRequest().getSession().getAttribute( name );

    if (getServletContext().getAttribute(name) != null)
      return getServletContext().getAttribute(name);

    return super.resolveVariable(name);
  }

  private Map getRequestScope()
  {
    Map map = (Map) implicitObjects.get("requestScope");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getRequest().getAttributeNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getRequest().getAttribute(s) );
      }
      implicitObjects.put( "requestScope", map );
    }
    return map;
  }

  private Map getSessionScope()
  {
    Map map = (Map) implicitObjects.get("sessionScope");
    if ( map == null )
    {
      map = new HashMap();
      if ( getRequest().getSession(false) != null )
      {
        for (Enumeration e = getRequest().getSession().getAttributeNames(); e.hasMoreElements();)
        {
          String s = (String) e.nextElement();
          map.put( s, getRequest().getSession().getAttribute(s) );
        }
      }
      implicitObjects.put( "sessionScope", map );
    }
    return map;
  }

  private Map getApplicationScope()
  {
    Map map = (Map) implicitObjects.get("applicationScope");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getServletContext().getAttributeNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getServletContext().getAttribute(s) );
      }
      implicitObjects.put( "applicationScope", map );
    }
    return map;
  }

  private Map getParam()
  {
    Map map = (Map) implicitObjects.get("param");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getRequest().getParameterNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getRequest().getParameter(s) );
      }
      implicitObjects.put( "param", map );
    }
    return map;
  }

  private Map getParamValues()
  {
    Map map = (Map) implicitObjects.get("paramValues");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getRequest().getParameterNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getRequest().getParameterValues(s) );
      }
      implicitObjects.put( "paramValues", map );
    }
    return map;
  }

  private Map getHeader()
  {
    Map map = (Map) implicitObjects.get("header");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getRequest().getHeaderNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getRequest().getHeader(s) );
      }
      implicitObjects.put( "header", map );
    }
    return map;
  }

  private Map getHeaderValues()
  {
    Map map = (Map) implicitObjects.get("headerValues");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getRequest().getHeaderNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getRequest().getHeaders(s) );
      }
      implicitObjects.put( "headerValues", map );
    }
    return map;
  }

  private Map getCookie()
  {
    Map map = (Map) implicitObjects.get("cookies");
    if ( map == null )
    {
      map = new HashMap();
      Cookie[] cookies = getRequest().getCookies();
      for ( int i=0; i < cookies.length; i++ )
      {
        if ( ! map.containsKey(cookies[i].getName()) )
          map.put( cookies[i].getName(), cookies[i] );
      }
      implicitObjects.put( "cookies", map );
    }
    return map;
  }

  private Map getInitParam()
  {
    Map map = (Map) implicitObjects.get("initParam");
    if ( map == null )
    {
      map = new HashMap();
      for (Enumeration e = getServletContext().getInitParameterNames(); e.hasMoreElements();)
      {
        String s = (String) e.nextElement();
        map.put( s, getServletContext().getInitParameter(s) );
      }
      implicitObjects.put( "initParam", map );
    }
    return map;
  }

  public HttpServletRequest getRequest()
  {
    return request;
  }

  public void setRequest(HttpServletRequest request)
  {
    this.request = request;
  }

  public ServletContext getServletContext()
  {
    return servletContext;
  }

  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

}

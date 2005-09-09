package com.carbonfive.sstemplates.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.mock.web.*;
import com.carbonfive.sstemplates.*;
import com.carbonfive.sstemplates.tags.*;
import junit.framework.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public abstract class SsTemplateServletTestBase extends TestCase
{
  private SsTemplateServlet servlet;
  private ServletContext servletContext;

  protected void setUp() throws Exception
  {
    File contextBase = new File(System.getProperty("project.root"), "build/test");
    servletContext = new MockServletContext("file://" + contextBase.getAbsolutePath());
    ServletConfig config = new MockServletConfig(servletContext);

    servlet = new SsTemplateServlet();
    servlet.init(config);
  }

  protected void tearDown() throws Exception
  {
  }

  public SsTemplateServlet getServlet()
  {
    return servlet;
  }

  public ServletContext getServletContext()
  {
    return servletContext;
  }

  private String getPathFromServlet(HttpServletRequest request) throws Exception
  {
    return servlet.getTemplateFile(request).toString();
  }

  protected SsTemplateServletContext getHssfTemplateContext(HttpServletRequest request) throws ServletException
  {
    return servlet.createTemplateContext(request);
  }

  protected WorkbookTag getRenderTree(HttpServletRequest request) throws Exception
  {
    return servlet.getProcessor().retrieveRenderTree(getPathFromServlet(request));
  }

  protected SsTemplateContext renderWorkbook(HttpServletRequest request) throws Exception
  {
    return renderWorkbook(request, null);
  }

  protected SsTemplateContext renderWorkbook(HttpServletRequest request, Map attributes)
          throws Exception
  {
    WorkbookTag renderTree = getRenderTree(request);
    SsTemplateServletContext templateContext = getHssfTemplateContext(request);
    if ( attributes != null )
    {
      for (Iterator it = attributes.keySet().iterator(); it.hasNext();)
      {
        String key = (String) it.next();
        templateContext.getRequest().setAttribute(key,attributes.get(key));
      }
    }
    renderTree.render(templateContext);
    return templateContext;
  }


}

package com.carbonfive.sstemplates.servlet;

import java.util.*;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.*;
import com.carbonfive.sstemplates.*;
import com.carbonfive.sstemplates.tags.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public abstract class SsTemplateServletTestBase
{
  private SsTemplateServlet servlet;
  private ServletContext servletContext;

  @BeforeEach
  protected void setUp() throws Exception
  {
    servletContext = new MockServletContext("/test");
    ServletConfig config = new MockServletConfig(servletContext);

    servlet = new SsTemplateServlet();
    servlet.init(config);
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

  protected SsTemplateContext renderWorkbook(HttpServletRequest request, Map<String, Object> attributes)
          throws Exception
  {
    WorkbookTag renderTree = getRenderTree(request);
    SsTemplateServletContext templateContext = getHssfTemplateContext(request);
    if ( attributes != null )
    {
      for (Iterator<String> it = attributes.keySet().iterator(); it.hasNext();)
      {
        String key = (String) it.next();
        request.setAttribute(key,attributes.get(key));
      }
    }
    renderTree.render(templateContext);
    return templateContext;
  }


}

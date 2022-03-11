package com.carbonfive.sstemplates.examples;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SimpleServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  public void service(HttpServletRequest request,  HttpServletResponse response)
      throws IOException, ServletException
  {
    request.setAttribute("stringValue", "Ralph");
    request.setAttribute("listValue", new String[] { "Sue", "Amy", "Donna" });
    
    request.getRequestDispatcher("/WEB-INF/test.templates/names.sst").forward(request, response);
  }
}

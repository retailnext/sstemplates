package com.carbonfive.sstemplates.examples;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

public class SimpleServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
  {
    request.setAttribute("stringValue", "Ralph");
    request.setAttribute("listValue", new String[] { "Sue", "Amy", "Donna" });
    
    request.getRequestDispatcher("/WEB-INF/test.templates/names.sst").forward(request, response);
  }
}

package com.carbonfive.flashgateway.security;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * BufferedHttpRequestWrapper is an implementation of a HttpServletRequestWrapper
 * that returns a buffered InputStream from getInputStream() and adds a method
 * getBufferedInputStream() that returns the same InputStream.
 */
public class BufferedHttpRequestWrapper
  extends HttpServletRequestWrapper
{

  private BufferedServletInputStream buffered;

  public BufferedHttpRequestWrapper(HttpServletRequest request)
  {
     super(request);
  }

  public ServletInputStream getInputStream()
    throws IOException
  {
    return getBufferedInputStream();
  }

  public BufferedServletInputStream getBufferedInputStream()
    throws IOException
  {

    if (buffered == null)
    {
      buffered = new BufferedServletInputStream(getRequest().getInputStream(), getRequest().getContentLength());
    }
    return buffered;
  }
}

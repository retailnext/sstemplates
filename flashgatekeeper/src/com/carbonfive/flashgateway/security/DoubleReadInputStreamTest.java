package com.carbonfive.flashgateway.security;

import java.io.*;
import junit.framework.*;
import org.apache.cactus.*;
import org.apache.commons.logging.*;
import com.carbonfive.flashgateway.security.*;

public class DoubleReadInputStreamTest
  extends ServletTestCase
{
  private static final Log log = LogFactory.getLog(DoubleReadInputStreamTest.class);

  public DoubleReadInputStreamTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(DoubleReadInputStreamTest.class);
    return suite;
  }

  protected void setUp() throws Exception {}

  protected void tearDown() throws Exception{}

  public void testStringDoubleRead() throws Exception
  {
    String testString = "Stream me.";
    InputStream src = new StringBufferInputStream(testString);
    InputStream in = new BufferedInputStream(src, testString.getBytes().length);
    in.mark(testString.getBytes().length + 1);

    int b = 0;
    String firstTime = "";
    while( (b = in.read()) != -1)
    {
      firstTime += (char) b;
    }
    assertEquals(testString, firstTime);

    in.reset();

    String secondTime = "";
    while( (b = in.read()) != -1)
    {
      secondTime += (char) b;
    }

    assertEquals(firstTime, secondTime);
  }

  public void beginRequestDoubleRead(WebRequest request)
  {
    request.addParameter("blah","blah", "POST");
  }

  public void testRequestDoubleRead() throws Exception
  {
    log.info("Request content length: " + request.getContentLength());
    InputStream in = new BufferedInputStream(request.getInputStream(), request.getContentLength());
    in.mark(request.getContentLength() + 1);

    int b = 0;
    String firstTime = "";
    while( (b = in.read()) != -1)
    {
      firstTime += (char) b;
    }

    in.reset();

    String secondTime = "";
    while( (b = in.read()) != -1)
    {
      secondTime += (char) b;
    }

    assertEquals(firstTime, secondTime);
  }

  public void beginBufferedRequestDoubleRead(WebRequest request)
  {
    request.addParameter("blah","blah", "POST");
  }

  public void testBufferedRequestDoubleRead()
    throws Exception
  {
    BufferedHttpRequestWrapper wrapped = new BufferedHttpRequestWrapper(request);
    BufferedServletInputStream buffered = wrapped.getBufferedInputStream();
    buffered.mark(wrapped.getContentLength() + 1);


    int b = 0;
    String firstTime = "";
    while( (b = buffered.read()) != -1)
    {
      firstTime += (char) b;
    }

    buffered.reset();

    InputStream in = wrapped.getInputStream();
    String secondTime = "";
    while( (b = in.read()) != -1)
    {
      secondTime += (char) b;
    }

    assertEquals(firstTime, secondTime);

  }
}
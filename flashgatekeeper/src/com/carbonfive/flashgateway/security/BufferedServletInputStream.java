package com.carbonfive.flashgateway.security;

import java.io.*;
import javax.servlet.*;
import org.apache.commons.logging.*;

/**
 * BufferedServletInputStream is an implementation of ServletInputStream that
 * is backed by a BufferedInputStream to provide buffering support.
 */
public class BufferedServletInputStream
  extends ServletInputStream
{
  private static final Log log = LogFactory.getLog(BufferedServletInputStream.class.getName());

  private BufferedInputStream buffered;

  public BufferedServletInputStream(InputStream in, int length)
    throws IOException
  {
    if (length > 0) buffered = new BufferedInputStream(in, length);
    else            buffered = new BufferedInputStream(in);
  }

  public synchronized int read() throws IOException
  {
    return buffered.read();
  }

  public synchronized int read(byte b[], int off, int len)
      throws IOException
  {
    return buffered.read(b, off, len);
  }

  public synchronized long skip(long n) throws IOException
  {
    return buffered.skip(n);
  }

  public synchronized int available() throws IOException
  {
    return buffered.available();
  }

  public synchronized void mark(int readlimit)
  {
    buffered.mark(readlimit);
  }

  public synchronized void reset() throws IOException
  {
    buffered.reset();
  }

  public boolean markSupported()
  {
    return buffered.markSupported();
  }

  public void close() throws IOException
  {
    buffered.close();
  }
}

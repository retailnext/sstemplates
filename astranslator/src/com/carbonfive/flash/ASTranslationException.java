package com.carbonfive.flash;

import java.io.*;

public class ASTranslationException extends Exception
{

  private Throwable rootCause = null;
  
//------------------------------------------------------------------------------
  
  public ASTranslationException()
  {
    super();
  }
  
//------------------------------------------------------------------------------
  
  public ASTranslationException( String message )
  {
    super( message );
  }

//------------------------------------------------------------------------------

  public ASTranslationException( String message, Throwable rootCause )
  {
    super( message );
    this.rootCause = rootCause;
  }

//------------------------------------------------------------------------------

  public ASTranslationException( Throwable rootCause )
  {
    super( rootCause.getLocalizedMessage() );
    this.rootCause = rootCause;
  }

//------------------------------------------------------------------------------

  public Throwable getRootCause()
  {
    return rootCause;
  }

//------------------------------------------------------------------------------

  public void printStackTrace(PrintWriter writer)
  {
    super.printStackTrace(writer);
    if (rootCause != null)
    {
      writer.println();
      writer.println("-- Nested Exception is:");
      writer.println();
      rootCause.printStackTrace(writer);
    }
    writer.flush();
  }

//------------------------------------------------------------------------------

  public void printStackTrace(PrintStream stream)
  {
    printStackTrace(new PrintWriter(stream));
  }
  
//------------------------------------------------------------------------------

  public void printStackTrace()
  {
    printStackTrace(System.out); 
  }

//------------------------------------------------------------------------------

  public String getMessage()
  {
    if( rootCause == null )
       return super.getMessage();
  
    StringWriter stringWriter = null;
    PrintWriter printWriter = null;
  
    stringWriter = new StringWriter();
    printWriter = new PrintWriter( stringWriter );
    rootCause.printStackTrace( printWriter );
    printWriter.close();
    return stringWriter.toString();
  }

}

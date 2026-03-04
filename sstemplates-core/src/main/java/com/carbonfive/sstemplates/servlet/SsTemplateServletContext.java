package com.carbonfive.sstemplates.servlet;

import java.io.*;
import com.carbonfive.sstemplates.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import javax.el.ExpressionFactory;

/**
 * This class acts as an EL VariableResolver, but does not support the pageContext implicit object.
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateServletContext
    extends SsTemplateContextImpl
{
  private final ServletContext servletContext;
  private final String canonicalDocRoot;

  public SsTemplateServletContext(HttpServletRequest request, ServletContext context,
                                  SsTemplateProcessor processor, File templateDir, ExpressionFactory expressionFactory)
  {
    super(processor, templateDir, expressionFactory, new SsTemplateServletVariableMapper(request, context, expressionFactory));
    this.servletContext = context;
    String root = context.getRealPath("/");
    try
    {
      this.canonicalDocRoot = ( root != null ) ? new File(root).getCanonicalPath() : null;
    }
    catch ( IOException e )
    {
      throw new RuntimeException("Cannot resolve document root", e);
    }
  }

  public File findFileInTemplateDirectory(String path)
  {
    String file = ( path == null ? "" : path );
    if ( !file.startsWith("/") ) return super.findFileInTemplateDirectory(path);

    String realPath = servletContext.getRealPath(file);
    if ( realPath == null )
    {
      return super.findFileInTemplateDirectory(path);
    }

    File resolved = new File(realPath);
    if ( canonicalDocRoot != null )
    {
      try
      {
        PathValidation.assertWithinDirectory(resolved, canonicalDocRoot);
      }
      catch ( IOException e )
      {
        throw new IllegalArgumentException("Invalid path: " + path, e);
      }
    }
    return resolved;
  }

}

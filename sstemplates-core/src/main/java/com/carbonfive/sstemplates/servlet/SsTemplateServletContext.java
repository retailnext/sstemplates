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

  public SsTemplateServletContext(HttpServletRequest request, ServletContext context,
                                  SsTemplateProcessor processor, File templateDir, ExpressionFactory expressionFactory)
  {
    super(processor, templateDir, expressionFactory, new SsTemplateServletVariableMapper(request, context, expressionFactory));
    this.servletContext = context;
  }

  public File findFileInTemplateDirectory(String path)
  {
    String file = ( path == null ? "" : path );
    if ( !file.startsWith("/") ) return super.findFileInTemplateDirectory(path);

    return new File(servletContext.getRealPath(file));
  }

}

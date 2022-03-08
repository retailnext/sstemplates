package com.carbonfive.sstemplates.servlet;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;
import com.carbonfive.sstemplates.tags.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class SsTemplateServlet extends HttpServlet
{
  private static final Logger log = Logger.getLogger(SsTemplateServlet.class.getName());

  private static final String HSSF_DIRECTORY_PARAM_KEY = "templatesDirectory";
  private static final String CUSTOM_TAGS_PARAM_KEY = "customTags";

  private String          templateDirectory = "/";
  private ServletContext  context = null;
  private ServletSsTemplateProcessor processor;

  public void init( ServletConfig config ) throws ServletException
  {
    Collection customTags = null;
    if ( config.getInitParameter(CUSTOM_TAGS_PARAM_KEY) != null )
      customTags = getCustomTags(config.getInitParameter(CUSTOM_TAGS_PARAM_KEY));

    this.context = config.getServletContext();
    try { this.processor = (ServletSsTemplateProcessor) ServletSsTemplateProcessor.getInstance(customTags); }
    catch (SsTemplateException he) { throw new ServletException(he); }

    log.info("Spreadsheet Templates servlet initialized.");
  }

  private Collection getCustomTags(String names)
    throws ServletException
  {
    List tags = new ArrayList();

    for (StringTokenizer tok = new StringTokenizer(names, ", "); tok.hasMoreTokens();)
    {
      String name = tok.nextToken();
      try
      {
        Class clazz = Class.forName(name);

        if (!SsTemplateTag.class.isAssignableFrom(clazz))
          throw new ServletException("Custom tags must implement SsTemplateTag");

        tags.add(clazz);
      }
      catch (ClassNotFoundException e)
      {
        throw new ServletException(e);
      }
    }

    return tags;
  }

  public void doGet( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
  {
    File templateFile = getTemplateFile(request);

    if (! templateFile.exists())
    {
      response.sendError( HttpServletResponse.SC_NOT_FOUND, "Could not find template " + templateFile.getName() );
      return;
    }

    HSSFWorkbook workbook = null;
    try { workbook = processor.process(request, context, templateFile.getParentFile(), templateFile); }
    catch (SsTemplateException he) { throw new ServletException(he); }

    response.setContentType("application/vnd-ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + filenameWithXLSExtension(templateFile));

    if ( workbook != null )
      workbook.write(response.getOutputStream());
    response.getOutputStream().close();
  }

  public void doPost( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
  {
    doGet( request, response );
  }

  private String filenameWithXLSExtension(File file)
  {
    String filename =  file.getName();
    int dotIndex = filename.lastIndexOf('.');
    if ( dotIndex >= 0 )
      filename = filename.substring( 0, dotIndex ) + ".xls";
    return filename;
  }

  public File getTemplateFile( HttpServletRequest request )
  {
    String path = request.getRealPath(request.getServletPath());
    if ( path == null )
      path = request.getServletPath();
    return new File(path);
  }

  File getTemplateDirectory(HttpServletRequest request)
  {
    return getTemplateFile(request).getParentFile();
  }

  public SsTemplateProcessor getProcessor()
  {
    return processor;
  }

  public SsTemplateServletContext createTemplateContext(HttpServletRequest request)
  {
    return processor.createTemplateContext(request, context, getTemplateDirectory(request));
  }
}

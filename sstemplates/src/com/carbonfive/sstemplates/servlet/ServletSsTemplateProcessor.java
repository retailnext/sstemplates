package com.carbonfive.sstemplates.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.logging.*;
import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.tags.*;
import com.carbonfive.sstemplates.*;

public class ServletSsTemplateProcessor
  extends SsTemplateProcessor
{
  private static final Log log = LogFactory.getLog(ServletSsTemplateProcessor.class);

  private static Map processorCache = Collections.synchronizedMap(new HashMap());

  public static SsTemplateProcessor getInstance(Collection customTags)
    throws SsTemplateException
  {
    ServletSsTemplateProcessor processor = (ServletSsTemplateProcessor) processorCache.get(customTags);

    if (processor == null)
    {
      processor = new ServletSsTemplateProcessor(customTags);
      processorCache.put(customTags, processor);
    }

    return processor;
  }

  protected ServletSsTemplateProcessor(Collection customTags)
    throws SsTemplateException
  {
    super(customTags);
  }

  public HSSFWorkbook process(HttpServletRequest request, ServletContext context, File templateDir, File templateFile)
    throws SsTemplateException
  {
    WorkbookTag renderTree = retrieveRenderTree(templateFile.getAbsolutePath());
    SsTemplateContext templateContext = createTemplateContext(request, context, templateDir);

    renderTree.render(templateContext);

    return templateContext.getWorkbook();
  }

  protected SsTemplateServletContext createTemplateContext(HttpServletRequest request, ServletContext context, File templateDir)
  {
    return new SsTemplateServletContext(request, context, this, templateDir);
  }
}

package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;

import java.io.*;
import com.carbonfive.sstemplates.*;

public class WorkbookTag extends BaseTag
{
  private String template = null;
  private String bgcolor = null;

  public String getTagName()
  {
    return "workbook";
  }

  public void render( SsTemplateContext context )
    throws SsTemplateException
  {
    HSSFWorkbook workbook = null;
    try
    {
      if (( template != null ) && ( template.length() > 0 ))
        workbook = loadTemplateWorkbook(context);
      else
        workbook = new HSSFWorkbook();

      context.setWorkbook(workbook);

      if (bgcolor != null)
      {
        short triplet[];
        String parsedColor = (String) parseExpression(bgcolor, String.class, context);
        if (parsedColor.startsWith("#")) triplet = StyleTag.parseColor(parsedColor);
        //else if (colorMap.containsKey(parsedValue)) triplet = ((HSSFColor)colorMap.get(parsedValue)).getTriplet();
        else throw new SsTemplateException("Can't parse background color '" + parsedColor + "'");

        context.setBackgroundColor(triplet);
      }

      renderChildren(context);
    }
    catch ( IOException ioe )
    {
      throw new SsTemplateException( "Error reading workbook template", ioe );
    }
  }


  HSSFWorkbook loadTemplateWorkbook( SsTemplateContext context )
    throws IOException, SsTemplateException
  {
    if ( template == null ) return null;

    String templateName = (String) parseExpression(template,String.class,context);
    File file = context.findFileInTemplateDirectory(templateName);

    if (( ! file.exists() ) || ( file.isDirectory() ))
      throw new IOException( "Could not find template workbook: " + templateName );

    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
    return new HSSFWorkbook(fs);
  }

  public String getTemplate()
  {
    return template;
  }

  public void setTemplate(String template)
  {
    this.template = template;
  }

  public String getBgcolor()
  {
    return bgcolor;
  }

  public void setBgcolor(String bgcolor)
  {
    this.bgcolor = bgcolor;
  }
}

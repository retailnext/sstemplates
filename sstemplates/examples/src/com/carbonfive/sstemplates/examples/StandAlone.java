package com.carbonfive.sstemplates.examples;

import java.util.*;
import java.io.*;
import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

public class StandAlone
{
  public static void main(String args[]) throws Exception
  {
    File template = new File(System.getProperty("project.root"), "examples/templates/standalone.sst");

    Map context = new HashMap();
    context.put("stringValue", "Ralph");
    context.put("listValue", new String[] { "Sue", "Amy", "Donna" });

    SsTemplateProcessor processor = SsTemplateProcessor.getInstance();
    HSSFWorkbook workbook = processor.process(template, context);

    File xls = new File(System.getProperty("project.root"), "standalone.xls");
    OutputStream out = new FileOutputStream(xls);
    try
    {
      workbook.write(out);
    }
    finally
    {
      out.close();
    }
  }
}

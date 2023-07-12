package com.carbonfive.sstemplates.examples;

import java.net.URL;
import java.util.*;
import java.io.*;
import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

public class StandAlone
{
  public static void main(String args[]) throws Exception
  {
    URL templateResource = StandAlone.class.getClassLoader().getResource("standalone.sst");
    if(templateResource == null) {
      return;
    }
    File template = new File(templateResource.toURI());

    Map<String, Object> context = new HashMap<>();
    context.put("stringValue", "Ralph");
    context.put("listValue", new String[] { "Sue", "Amy", "Donna" });

    SsTemplateProcessor processor = SsTemplateProcessor.getInstance();
    HSSFWorkbook workbook = processor.process(template, context);

    File xls = new File(System.getProperty("project.root"), "standalone.xls");
    try (OutputStream out = new FileOutputStream(xls)) {
      workbook.write(out);
    }
  }
}

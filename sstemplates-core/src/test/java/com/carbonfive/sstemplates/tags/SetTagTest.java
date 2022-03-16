package com.carbonfive.sstemplates.tags;

import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class SetTagTest extends TagTestBase
{
  public SetTagTest( String name )
  {
    super(name);
  }

  public void testSetters() throws Exception
  {
    Map<String, Object> attrs = new HashMap<String, Object>();
    attrs.put("fooAttr", new FooBean());
    SsTemplateContext templateContext = renderWorkbook("set.sst", attrs);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals("0", row.getCell(0).getStringCellValue());
    assertEquals("1", row.getCell(1).getStringCellValue());
    assertEquals("2", row.getCell(2).getStringCellValue());
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }

  public static class FooBean
  {
    private String bar;

    public String getBar()
    {
      return bar;
    }

    public void setBar(String bar)
    {
      this.bar = bar;
    }
  }
}

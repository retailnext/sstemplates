package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class IfTagTest extends TagTestBase
{
  public IfTagTest( String name )
  {
    super(name);
  }

  public void testIf() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("if.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertNotNull(row.getCell(0));
    assertEquals("Here I Am 0", row.getCell(0).getStringCellValue());
    assertNotNull(row.getCell(1));
    assertEquals("Here I Am 2", row.getCell(1).getStringCellValue());
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }
}

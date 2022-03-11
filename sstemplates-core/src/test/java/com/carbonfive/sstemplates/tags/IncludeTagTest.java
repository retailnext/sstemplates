package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class IncludeTagTest extends TagTestBase
{
  public IncludeTagTest( String name )
  {
    super(name);
  }

  public void testIncludesFile() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("include.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "Cell should exist", "Here I Am", row.getCell(0).getStringCellValue() );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }
}

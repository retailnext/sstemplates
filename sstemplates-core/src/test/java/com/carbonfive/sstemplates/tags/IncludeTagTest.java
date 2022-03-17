package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class IncludeTagTest extends TagTestBase
{

  @Test
  public void testIncludesFile() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("include.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "Here I Am", row.getCell(0).getStringCellValue(), "Cell should exist" );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }
}

package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class WhileTagTest extends TagTestBase
{
  @Test
  public void testWhile() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("while.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "b", row.getCell(0).getStringCellValue(), "No a's, 3 b's 0" );
    assertEquals( "b", row.getCell(1).getStringCellValue(), "No a's, 3 b's 1" );
    assertEquals( "b", row.getCell(2).getStringCellValue(), "No a's, 3 b's 2" );
    assertNull( row.getCell(3), "Cell should not exist" );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }
}

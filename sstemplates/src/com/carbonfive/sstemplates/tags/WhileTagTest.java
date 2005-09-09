package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class WhileTagTest extends TagTestBase
{
  public WhileTagTest( String name )
  {
    super(name);
  }

  public void testWhile() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("while.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "No a's, 3 b's 0", "b", row.getCell((short) 0).getStringCellValue() );
    assertEquals( "No a's, 3 b's 1", "b", row.getCell((short) 1).getStringCellValue() );
    assertEquals( "No a's, 3 b's 2", "b", row.getCell((short) 2).getStringCellValue() );
    assertNull( "Cell should not exist", row.getCell((short) 3) );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }
}

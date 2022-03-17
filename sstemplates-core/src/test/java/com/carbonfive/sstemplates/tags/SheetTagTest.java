package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import com.carbonfive.sstemplates.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class SheetTagTest extends TagTestBase
{
  @Test
  public void testCreateSheet() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("sheet_test1.sst?sheet1=alpha&sheet2=beta&sheet3=gamma&sheet4=delta");

    HSSFWorkbook workbook = templateContext.getWorkbook();
    assertEquals( "alpha", workbook.getSheetName(0), "Sheet 1 name should be alpha" );
    assertEquals( "beta", workbook.getSheetName(1), "Sheet 2 name should be beta" );
    assertEquals( "gamma", workbook.getSheetName(2), "Sheet 3 name should be gamma" );
    assertEquals( "delta", workbook.getSheetName(3), "Sheet 4 name should be delta" );
  }

  @Test
  public void testRowReset() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("sheet_test4.sst");

    HSSFWorkbook workbook = templateContext.getWorkbook();
    assertEquals( "value", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue(), "Sheet 1 should have cell" );
    assertEquals( "value", workbook.getSheetAt(1).getRow(0).getCell(0).getStringCellValue(), "Sheet 2 should have cell" );
  }

  @Test
  public void testSheetContext() throws Exception
  {
    WorkbookTag renderTree = getRenderTree("sheet_test2.sst");

    SheetTag sheetTag = (SheetTag) renderTree.getChildTags().iterator().next();
    TestContextTag testTag = new TestContextTag(this);
    sheetTag.addChildTag(testTag);

    renderTree.render(getHssfTemplateContext());
    assertTrue( testTag.hasTagBeenRendered(), "Sheet tag rendered children" );
  }

  @Test
  public void testDefinesStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("sheet_test3.sst?style=fred");
    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0).getCellStyle();

    assertNotNull( cellStyle, "Style defined in sheet is not null" );
    assertEquals( BorderStyle.THIN, cellStyle.getBorderTop(), "Style should have thin top border" );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getSheet() == null )
      throw new SsTemplateException( "Context has no current sheet" );
  }
}

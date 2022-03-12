package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class SheetTagTest extends TagTestBase
{
  public SheetTagTest( String name )
  {
    super(name);
  }

  public void testCreateSheet() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("sheet_test1.sst?sheet1=alpha&sheet2=beta&sheet3=gamma&sheet4=delta");

    HSSFWorkbook workbook = templateContext.getWorkbook();
    assertEquals( "Sheet 1 name should be alpha", "alpha", workbook.getSheetName(0));
    assertEquals( "Sheet 2 name should be beta", "beta", workbook.getSheetName(1));
    assertEquals( "Sheet 3 name should be gamma", "gamma", workbook.getSheetName(2));
    assertEquals( "Sheet 4 name should be delta", "delta", workbook.getSheetName(3));
  }

  public void testRowReset() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("sheet_test4.sst");

    HSSFWorkbook workbook = templateContext.getWorkbook();
    assertEquals( "Sheet 1 should have cell", "value", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    assertEquals( "Sheet 2 should have cell", "value", workbook.getSheetAt(1).getRow(0).getCell(0).getStringCellValue());
  }

  public void testSheetContext() throws Exception
  {
    WorkbookTag renderTree = getRenderTree("sheet_test2.sst");

    SheetTag sheetTag = (SheetTag) renderTree.getChildTags().iterator().next();
    TestContextTag testTag = new TestContextTag(this);
    sheetTag.addChildTag(testTag);

    renderTree.render(getHssfTemplateContext());
    assertTrue( "Sheet tag rendered children", testTag.hasTagBeenRendered());
  }

  public void testDefinesStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("sheet_test3.sst?style=fred");
    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0).getCellStyle();

    assertNotNull( "Style defined in sheet is not null", cellStyle );
    assertEquals( "Style should have thin top border", BorderStyle.THIN, cellStyle.getBorderTopEnum() );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getSheet() == null )
      throw new SsTemplateException( "Context has no current sheet" );
  }
}

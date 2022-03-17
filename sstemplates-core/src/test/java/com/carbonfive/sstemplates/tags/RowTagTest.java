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
public class RowTagTest extends TagTestBase
{
  @Test
  public void testCreateRow() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("row_test1.sst?row1=1&row2=4&row3=8");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    assertNotNull(sheet.getRow(1), "Row should exist at explicit index 1");
    assertNotNull(sheet.getRow(4), "Row should exist at explicit index 4");
    assertNotNull(sheet.getRow(8), "Row should exist at explicit index 8");
    assertNotNull(sheet.getRow(9), "Row should exist at contextual index 9");
  }

  @Test
  public void testSheetContext() throws Exception
  {
    WorkbookTag renderTree = getRenderTree("row_test1.sst");

    SheetTag sheetTag = (SheetTag) renderTree.getChildTags().iterator().next();
    RowTag rowTag = (RowTag) sheetTag.getChildTags().iterator().next();
    TestContextTag testTag = new TestContextTag(this);
    rowTag.addChildTag(testTag);

    renderTree.render(getHssfTemplateContext());
    assertTrue(testTag.hasTagBeenRendered(), "Row tag rendered children");
  }

  @Test
  public void testDefinesStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("row_test2.sst?style=fred");
    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0).getCellStyle();

    assertNotNull(cellStyle, "Style defined in row is not null");
    assertEquals(BorderStyle.THIN, cellStyle.getBorderTop(), "Style should have thin top border");
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getRow() == null )
      throw new SsTemplateException( "Context has no current row" );
  }
}

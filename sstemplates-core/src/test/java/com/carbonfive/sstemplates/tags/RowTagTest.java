package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class RowTagTest extends TagTestBase
{
  public RowTagTest( String name )
  {
    super(name);
  }

  public void testCreateRow() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("row_test1.sst?row1=1&row2=4&row3=8");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    assertNotNull( "Row should exist at explicit index 1", sheet.getRow(1));
    assertNotNull( "Row should exist at explicit index 4", sheet.getRow(4));
    assertNotNull( "Row should exist at explicit index 8", sheet.getRow(8));
    assertNotNull( "Row should exist at contextual index 9", sheet.getRow(9));
  }

  public void testSheetContext() throws Exception
  {
    WorkbookTag renderTree = getRenderTree("row_test1.sst");

    SheetTag sheetTag = (SheetTag) renderTree.getChildTags().iterator().next();
    RowTag rowTag = (RowTag) sheetTag.getChildTags().iterator().next();
    TestContextTag testTag = new TestContextTag(this);
    rowTag.addChildTag(testTag);

    renderTree.render(getHssfTemplateContext());
    assertTrue( "Row tag rendered children", testTag.hasTagBeenRendered());
  }

  public void testDefinesStyle() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("row_test2.sst?style=fred");
    HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0).getCellStyle();

    assertNotNull( "Style defined in row is not null", cellStyle );
    assertEquals( "Style should have thin top border", BorderStyle.THIN, cellStyle.getBorderTopEnum() );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getRow() == null )
      throw new SsTemplateException( "Context has no current row" );
  }
}

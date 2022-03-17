package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
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
public class WorkbookTagTest
    extends TagTestBase
{
  @Test
  public void testReadWorkbookTemplate() throws Exception
  {
    WorkbookTag tag = getRenderTree("workbook_test1.sst");
    HSSFWorkbook workbook = tag.loadTemplateWorkbook(getHssfTemplateContext());

    assertNotNull( workbook, "Workbook should not be null" );
    assertEquals( "test", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue(),
            "first value of workbook should be 'test'" );
  }

  @Test
  public void testCreateEmptyWorkbook() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("workbook_test2.sst");
    assertNotNull( templateContext.getWorkbook(), "Workbook tag with no template should create empty workbook" );
  }

  @Test
  public void testWorkbookContext() throws Exception
  {
    WorkbookTag workbookTag = getRenderTree("workbook_test2.sst");

    TestContextTag testTag = new TestContextTag(this);
    workbookTag.addChildTag(testTag);

    workbookTag.render(getHssfTemplateContext());
    assertTrue( testTag.hasTagBeenRendered(), "Workbook tag rendered children" );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getWorkbook() == null )
      throw new SsTemplateException( "Error: workbook is null within workbook tag");
  }
}

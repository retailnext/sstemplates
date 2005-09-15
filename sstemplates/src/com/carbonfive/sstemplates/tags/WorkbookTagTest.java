package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class WorkbookTagTest
    extends TagTestBase
{
  public WorkbookTagTest( String name )
  {
    super(name);
  }

  public void testReadWorkbookTemplate() throws Exception
  {
    WorkbookTag tag = getRenderTree("workbook_test1.sst");
    HSSFWorkbook workbook = tag.loadTemplateWorkbook(getHssfTemplateContext());

    assertNotNull( "Workbook should not be null", workbook );
    assertEquals( "first value of workbook should be 'test'", "test",
                  workbook.getSheetAt(0).getRow(0).getCell((short) 0).getStringCellValue());
  }

  public void testCreateEmptyWorkbook() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("workbook_test2.sst");
    assertNotNull( "Workbook tag with no template should create empty workbook", templateContext.getWorkbook() );
  }

  public void testWorkbookContext() throws Exception
  {
    WorkbookTag workbookTag = getRenderTree("workbook_test2.sst");

    TestContextTag testTag = new TestContextTag(this);
    workbookTag.addChildTag(testTag);

    workbookTag.render(getHssfTemplateContext());
    assertTrue( "Workbook tag rendered children", testTag.hasTagBeenRendered());
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    if ( context.getWorkbook() == null )
      throw new SsTemplateException( "Error: workbook is null within workbook tag");
  }
}

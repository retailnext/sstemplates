package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class CellTagTest extends TagTestBase
{
  public CellTagTest( String name )
  {
    super(name);
  }

  public void testCreateCell() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_column.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    HSSFRow row = sheet.getRow(0);
    assertNotNull( "Cell should exist at explicit index 1", row.getCell(1));
    assertNotNull( "Cell should exist at explicit index 4", row.getCell(4));
    assertNotNull( "Cell should exist at explicit index 8", row.getCell(8));
    assertNotNull( "Cell should exist at contextual index 9", row.getCell(9));
  }

  public void testCellContents() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_content.sst");

    HSSFCell cell = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0);
    assertEquals( "Cell content has been set", "Test Cell Contents", cell.getStringCellValue() );
  }

  public void testCellType() throws Exception
  {
    SsTemplateContext templateContext =
      renderWorkbook("cell_type.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertEquals( "Cell type has been set to blank", CellType.BLANK, row.getCell(0).getCellType() );

    assertEquals( "Cell type has been set to boolean", CellType.BOOLEAN, row.getCell(1).getCellType() );
    assertTrue( "Boolean cell is set to true", row.getCell(1).getBooleanCellValue() );

    assertEquals( "Cell type has been set to error", CellType.ERROR, row.getCell(2).getCellType() );

    assertEquals( "Cell type has been set to formula", CellType.FORMULA, row.getCell(3).getCellType() );

    assertEquals( "Cell type has been set to numeric", CellType.NUMERIC, row.getCell(4).getCellType() );
    assertTrue( "Numeric cell is set to test value", 2342.32 == row.getCell(4).getNumericCellValue() );

    assertEquals( "Cell type has been set to string", CellType.STRING, row.getCell(5).getCellType() );
    assertEquals( "String cell is set to test value", "test string", row.getCell(5).getStringCellValue() );
  }

  public void testBlankCell() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_blank.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    HSSFRow row = sheet.getRow(0);
    assertEquals( "Cell content has been set", "stuff", row.getCell(0).getStringCellValue() );
    assertEquals( "Blank cell", "", row.getCell(1).getStringCellValue() );
    assertEquals( "Blank cell", CellType.BLANK, row.getCell(1).getCellType());
  }

  public void testRegion() throws Exception
  {
    String[][] cellValues = {{ "00", "01", "02", "03", "04", "05", "06" },
                             { "10", null, "12", "13", null, null, "16" },
                             { "20", null, "22", null, null, null, "26" },
                             { "30", "31", "32", "33", "34", "35", "36" },
                             { "40", null, null, "43", "44", "45", "46" },
                             { "50", "51", "52", "53", "54", "55", "56" }};


    SsTemplateContext templateContext = renderWorkbook("cell_region.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    for (int i=0; i < cellValues.length; i++ )
    {
      HSSFRow row = sheet.getRow(i);
      assertNotNull("row should not be null", row );
      for ( int j=0; j < cellValues[i].length; j++ )
      {
        if ( cellValues[i][j] != null )
        {
          assertNotNull("spreadsheet with regions has non-null value at " + i + "," + j, row.getCell(j) );
          assertEquals("spreadsheet with regions has incorrect value at " + i + "," + j, cellValues[i][j],
                       row.getCell(j).getRichStringCellValue().getString() );
        }
      }
    }

    assertEquals( "Sheet should have 3 regions", 3, sheet.getNumMergedRegions() );
    assertRegionEquals(sheet.getMergedRegion(0), 0, 2, 1, 1);
    assertRegionEquals(sheet.getMergedRegion(1), 1, 2, 3, 5);
    assertRegionEquals(sheet.getMergedRegion(2), 4, 4, 0, 2);
  }

  private void assertRegionEquals(CellRangeAddress region, int startRow, int endRow, int startColumn, int endColumn)
  {
    assertEquals( "Region should start at row " + startRow, startRow, region.getFirstRow() );
    assertEquals( "Region should start at column " + startColumn, startColumn, region.getFirstColumn() );
    assertEquals( "Region should end at row " + endRow, endRow, region.getLastRow() );
    assertEquals( "Region should end at column " + endColumn, endColumn, region.getLastColumn() );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    // cells do not have children so this always passes
  }
}

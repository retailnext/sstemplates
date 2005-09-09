package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
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
    assertNotNull( "Cell should exist at explicit index 1", row.getCell((short) 1));
    assertNotNull( "Cell should exist at explicit index 4", row.getCell((short) 4));
    assertNotNull( "Cell should exist at explicit index 8", row.getCell((short) 8));
    assertNotNull( "Cell should exist at contextual index 9", row.getCell((short) 9));
  }

  public void testCellContents() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_content.sst");

    HSSFCell cell = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell((short) 0);
    assertEquals( "Cell content has been set", "Test Cell Contents", cell.getStringCellValue() );
  }

  public void testCellType() throws Exception
  {
    SsTemplateContext templateContext =
      renderWorkbook("cell_type.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertEquals( "Cell type has been set to blank", HSSFCell.CELL_TYPE_BLANK, row.getCell((short) 0).getCellType() );

    assertEquals( "Cell type has been set to boolean", HSSFCell.CELL_TYPE_BOOLEAN, row.getCell((short) 1).getCellType() );
    assertTrue( "Boolean cell is set to true", row.getCell((short) 1).getBooleanCellValue() );

    assertEquals( "Cell type has been set to error", HSSFCell.CELL_TYPE_ERROR, row.getCell((short) 2).getCellType() );

    assertEquals( "Cell type has been set to formula", HSSFCell.CELL_TYPE_FORMULA, row.getCell((short) 3).getCellType() );

    assertEquals( "Cell type has been set to numeric", HSSFCell.CELL_TYPE_NUMERIC, row.getCell((short) 4).getCellType() );
    assertTrue( "Numeric cell is set to test value", 2342.32 == row.getCell((short) 4).getNumericCellValue() );

    assertEquals( "Cell type has been set to string", HSSFCell.CELL_TYPE_STRING, row.getCell((short) 5).getCellType() );
    assertEquals( "String cell is set to test value", "test string", row.getCell((short) 5).getStringCellValue() );
  }

  public void testBlankCell() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_blank.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    HSSFRow row = sheet.getRow(0);
    assertEquals( "Cell content has been set", "stuff", row.getCell((short)0).getStringCellValue() );
    assertEquals( "Blank cell", "", row.getCell((short)1).getStringCellValue() );
    assertEquals( "Blank cell", HSSFCell.CELL_TYPE_BLANK, row.getCell((short)1).getCellType());
  }

  public void testRegion() throws Exception
  {
    String[][] cellValues = {{ "00", null, "02", "03", "04", "05", "06" },
                             { "10", null, "12", null, null, null, "16" },
                             { "20", null, "22", null, null, null, "26" },
                             { "30", "31", "32", "33", "34", "35", "36" },
                             { null, null, null, "43", "44", "45", "46" },
                             { "50", "51", "52", "53", "54", "55", "56" }};


    SsTemplateContext templateContext = renderWorkbook("cell_region.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    for (int i=0; i < cellValues.length; i++ )
    {
      HSSFRow row = sheet.getRow(i);
      assertNotNull("row should not be null", row );
      for ( short j=0; j < cellValues[i].length; j++ )
      {
        if ( cellValues[i][j] != null )
        {
          assertNotNull("spreadsheet with regions has non-null value at " + i + "," + j, row.getCell(j) );
          assertEquals("spreadsheet with regions has incorrect value at " + i + "," + j, cellValues[i][j],
                       row.getCell(j).getStringCellValue() );
        }
      }
    }

    assertEquals( "Sheet should have 3 regions", 3, sheet.getNumMergedRegions() );
    assertRegionEquals(sheet.getMergedRegionAt(0), 0, (short) 1, 2, (short) 1);
    assertRegionEquals(sheet.getMergedRegionAt(1), 1, (short) 3, 2, (short) 5);
    assertRegionEquals(sheet.getMergedRegionAt(2), 4, (short) 0, 4, (short) 2);
  }

  private void assertRegionEquals(Region region, int startRow, short startColumn, int endRow, short endColumn)
  {
    assertEquals( "Region should start at row " + startRow, startRow, region.getRowFrom() );
    assertEquals( "Region should start at column " + startColumn, startColumn, region.getColumnFrom() );
    assertEquals( "Region should end at row " + endRow, endRow, region.getRowTo() );
    assertEquals( "Region should end at column " + endColumn, endColumn, region.getColumnTo() );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    // cells do not have children so this always passes
  }
}

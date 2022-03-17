package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class CellTagTest extends TagTestBase
{
  @Test
  public void testCreateCell() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_column.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    HSSFRow row = sheet.getRow(0);
    assertNotNull(row.getCell(1), "Cell should exist at explicit index 1");
    assertNotNull(row.getCell(4), "Cell should exist at explicit index 4");
    assertNotNull(row.getCell(8), "Cell should exist at explicit index 8");
    assertNotNull(row.getCell(9), "Cell should exist at contextual index 9");
  }

  @Test
  public void testCellContents() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_content.sst");

    HSSFCell cell = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell(0);
    assertEquals( "Test Cell Contents", cell.getStringCellValue(), "Cell content has been set" );
  }

  @Test
  public void testCellType() throws Exception
  {
    SsTemplateContext templateContext =
      renderWorkbook("cell_type.sst");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);

    assertEquals( CellType.BLANK, row.getCell(0).getCellType(), "Cell type has been set to blank" );

    assertEquals( CellType.BOOLEAN, row.getCell(1).getCellType(), "Cell type has been set to boolean" );
    assertTrue( row.getCell(1).getBooleanCellValue(), "Boolean cell is set to true" );

    assertEquals( CellType.ERROR, row.getCell(2).getCellType(), "Cell type has been set to error" );

    assertEquals( CellType.FORMULA, row.getCell(3).getCellType(), "Cell type has been set to formula" );

    assertEquals( CellType.NUMERIC, row.getCell(4).getCellType(), "Cell type has been set to numeric" );
    assertTrue( 2342.32 == row.getCell(4).getNumericCellValue(), "Numeric cell is set to test value" );

    assertEquals( CellType.STRING, row.getCell(5).getCellType(), "Cell type has been set to string" );
    assertEquals( "test string", row.getCell(5).getStringCellValue(), "String cell is set to test value" );
  }

  @Test
  public void testBlankCell() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("cell_blank.sst");

    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
    HSSFRow row = sheet.getRow(0);
    assertEquals( "stuff", row.getCell(0).getStringCellValue(), "Cell content has been set" );
    assertEquals( "", row.getCell(1).getStringCellValue(), "Blank cell" );
    assertEquals( CellType.BLANK, row.getCell(1).getCellType(), "Blank cell" );
  }

  @Test
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
      assertNotNull(row, "row should not be null");
      for ( int j=0; j < cellValues[i].length; j++ )
      {
        if ( cellValues[i][j] != null )
        {
          assertNotNull(row.getCell(j), "spreadsheet with regions has non-null value at " + i + "," + j);
          assertEquals(cellValues[i][j], row.getCell(j).getRichStringCellValue().getString(),
                "spreadsheet with regions has incorrect value at " + i + "," + j);
        }
      }
    }

    assertEquals(3, sheet.getNumMergedRegions(), "Sheet should have 3 regions");
    assertRegionEquals(sheet.getMergedRegion(0), 0, 2, 1, 1);
    assertRegionEquals(sheet.getMergedRegion(1), 1, 2, 3, 5);
    assertRegionEquals(sheet.getMergedRegion(2), 4, 4, 0, 2);
  }

  private void assertRegionEquals(CellRangeAddress region, int startRow, int endRow, int startColumn, int endColumn)
  {
    assertEquals( startRow, region.getFirstRow(), "Region should start at row " + startRow );
    assertEquals( startColumn, region.getFirstColumn(), "Region should start at column " + startColumn );
    assertEquals( endRow, region.getLastRow(), "Region should end at row " + endRow );
    assertEquals( endColumn, region.getLastColumn(), "Region should end at column " + endColumn );
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
    // cells do not have children so this always passes
  }
}

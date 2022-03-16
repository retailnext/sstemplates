package com.carbonfive.sstemplates.tags;

import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class ForEachTagTest extends TagTestBase
{
  public ForEachTagTest( String name )
  {
    super(name);
  }

  public void testArray() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("foreach_array.sst?x=A&x=B&x=C&x=D");

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "Cell 0 should be A", "A", row.getCell(0).getStringCellValue() );
    assertEquals( "Cell 1 should be B", "B", row.getCell(1).getStringCellValue() );
    assertEquals( "Cell 2 should be C", "C", row.getCell(2).getStringCellValue() );
    assertEquals( "Cell 3 should be D", "D", row.getCell(3).getStringCellValue() );
  }

  public void testCollection() throws Exception
  {
    List<Integer> c = new ArrayList<Integer>();
    c.add( Integer.valueOf(3) );
    c.add( Integer.valueOf(5) );
    c.add( Integer.valueOf(8) );
    Map<String, List<Integer>> attrs = new HashMap<String, List<Integer>>();
    attrs.put("columnIndexes", c);

    SsTemplateContext templateContext = renderWorkbook("foreach_collection.sst", attrs);

    HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(0);
    assertEquals( "Cell 3 should be 0", 0, (int) row.getCell(3).getNumericCellValue() );
    assertEquals( "Cell 5 should be 1", 1, (int) row.getCell(5).getNumericCellValue() );
    assertEquals( "Cell 8 should be 2", 2, (int) row.getCell(8).getNumericCellValue() );
  }

  public void testMap() throws Exception
  {
    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("1", "One");
    map.put("2", "Two");
    map.put("3", "Three");
    Map<String, Map<String, String>> attrs = new HashMap<String, Map<String, String>>();
    attrs.put("items", map);
    SsTemplateContext templateContext = renderWorkbook("foreach_map.sst", attrs);
    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);

    assertEquals("1", sheet.getRow(0).getCell(0).getStringCellValue());
    assertEquals("One", sheet.getRow(0).getCell(1).getStringCellValue());
    assertEquals("2", sheet.getRow(1).getCell(0).getStringCellValue());
    assertEquals("Two", sheet.getRow(1).getCell(1).getStringCellValue());
    assertEquals("3", sheet.getRow(2).getCell(0).getStringCellValue());
    assertEquals("Three", sheet.getRow(2).getCell(1).getStringCellValue());
  }

  public void testAvoidIndexConflict() throws Exception
  {
    Object[] o = new Object[10];
    Map<String, Object[]> attrs = new HashMap<String, Object[]>();
    attrs.put("emptyArray", o);

    SsTemplateContext templateContext = renderWorkbook("foreach_indexes.sst", attrs);

    for ( int i=0; i < 10; i++ )
    {
      HSSFRow row = templateContext.getWorkbook().getSheetAt(0).getRow(i);
      for (int j=0; j < 10; j++ )
      {
        assertEquals( "Cell "+i+","+j+" should be " + (5*i+7*j), (5*i+7*j), (int) row.getCell(j).getNumericCellValue() );
      }
    }
  }

  public void testHandleCollectionNotFound() throws Exception
  {
    try
    {
      renderWorkbook("iterator_test2.sst");
      fail( "Should have thrown a SsTemplateException for unspecified collection" );
    }
    catch ( SsTemplateException e )
    {
      // what we're expecting
    }
  }

  public void testForEachArray() throws Exception
  {
    Object[] arr = new Object[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    Map<String, Object[]> attrs = new HashMap<String, Object[]>();
    attrs.put("items", arr);
    SsTemplateContext templateContext = renderWorkbook("foreach_status.sst", attrs);
    HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);

    assertEquals(2, (int) sheet.getRow(0).getCell(0).getNumericCellValue());
    assertEquals(2, (int) sheet.getRow(0).getCell(1).getNumericCellValue());
    assertEquals(1, (int) sheet.getRow(0).getCell(2).getNumericCellValue());
    assertEquals("true",  sheet.getRow(0).getCell(3).getStringCellValue());
    assertEquals("false", sheet.getRow(0).getCell(4).getStringCellValue());

    assertEquals(5, (int) sheet.getRow(1).getCell(0).getNumericCellValue());
    assertEquals(5, (int) sheet.getRow(1).getCell(1).getNumericCellValue());
    assertEquals(2, (int) sheet.getRow(1).getCell(2).getNumericCellValue());
    assertEquals("false", sheet.getRow(1).getCell(3).getStringCellValue());
    assertEquals("false", sheet.getRow(1).getCell(4).getStringCellValue());

    assertEquals(8, (int) sheet.getRow(2).getCell(0).getNumericCellValue());
    assertEquals(8, (int) sheet.getRow(2).getCell(1).getNumericCellValue());
    assertEquals(3, (int) sheet.getRow(2).getCell(2).getNumericCellValue());
    assertEquals("false", sheet.getRow(2).getCell(3).getStringCellValue());
    assertEquals("true",  sheet.getRow(2).getCell(4).getStringCellValue());
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {

  }
}

package com.carbonfive.sstemplates.tags;

import org.apache.poi.hssf.usermodel.*;
import com.carbonfive.sstemplates.*;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class FunctionTagTest extends TagTestBase
{
  public FunctionTagTest( String name )
  {
    super(name);
  }

  public void testFunction() throws Exception
  {
    SsTemplateContext templateContext = renderWorkbook("function.sst");
    HSSFWorkbook workbook = templateContext.getWorkbook();
    assertEquals( "Function invocation", "I heart nothingness", workbook.getSheetName(0));
    assertEquals( "Function invocation", "I heart functions", workbook.getSheetName(1));
    assertEquals( "Function invocation", "I heart apples and oranges", workbook.getSheetName(2));
  }

  public static String test()
  {
    return "I heart nothingness";
  }

  public static String test(String value)
  {
    return "I heart " + value;
  }

  public static String test(String value1, String value2)
  {
    return "I heart " + value1 + " and " + value2;
  }

  public void childRenderTest( SsTemplateContext context )
    throws SsTemplateException
  {
  }
}

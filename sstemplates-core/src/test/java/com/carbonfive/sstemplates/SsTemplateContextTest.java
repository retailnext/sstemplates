package com.carbonfive.sstemplates;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.mock.web.*;
import com.carbonfive.sstemplates.servlet.*;

/**
 * 
 * @author Alex Cruikshank
 * @version $REVISION
 */
public class SsTemplateContextTest
    extends SsTemplateServletTestBase
{

  private MockHttpServletRequest request;

  public void setUp() throws Exception
  {
    super.setUp();

    request = new MockHttpServletRequest(getServletContext());
    request.setMethod("POST");
  }

  public void testFontCaching() throws Exception
  {
    request.setServletPath("/templates/servlet_basic.sst");

    SsTemplateContext context = getServlet().createTemplateContext(request);
    context.setWorkbook( new HSSFWorkbook() );

    String fontName = "Arial";
    short fontHeight = 240;
    short color = HSSFFont.COLOR_RED;
    boolean bold = true;
    boolean italic = true;
    boolean strikeout = true;
    byte underline = HSSFFont.U_DOUBLE;
    short typeOffset = 3;

    short initialFontCount = context.getWorkbook().getNumberOfFonts();
    context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    HSSFFont font = context.createFont(fontName,fontHeight,color,bold,italic,strikeout,underline,typeOffset);
    assertNotNull( "context should have created a font", font );
    assertEquals( "Only one font should have been added to the workbook", initialFontCount+1,
                  context.getWorkbook().getNumberOfFonts() );
  }
}
